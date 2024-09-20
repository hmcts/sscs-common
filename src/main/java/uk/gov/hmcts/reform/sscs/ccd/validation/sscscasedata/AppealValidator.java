package uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.callback.ValidationType;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appeal;
import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitType;
import uk.gov.hmcts.reform.sscs.ccd.domain.ExcludeDate;
import uk.gov.hmcts.reform.sscs.ccd.domain.FormType;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsDocument;
import uk.gov.hmcts.reform.sscs.model.dwp.OfficeMapping;
import uk.gov.hmcts.reform.sscs.service.DwpAddressLookupService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata.PartyValidator.appendErrorsAndWarnings;
import static uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata.PartyValidator.doesMrnDateExist;
import static uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata.PartyValidator.getDwpIssuingOffice;
import static uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata.PartyValidator.isValidHearingSubType;
import static uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata.PartyValidator.validateDate;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.ARE_EMPTY;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.BENEFIT_TYPE_DESCRIPTION;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.HEARING_OPTIONS_EXCLUDE_DATES_LITERAL;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.HEARING_SUB_TYPE_TELEPHONE_OR_VIDEO_FACE_TO_FACE_DESCRIPTION;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.HEARING_TYPE_DESCRIPTION;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.HEARING_TYPE_ORAL;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.HEARING_TYPE_PAPER;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.ISSUING_OFFICE;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.IS_EMPTY;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.IS_INVALID;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.MRN_DATE;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.PERSON_1_CHILD_MAINTENANCE_NUMBER;
import static uk.gov.hmcts.reform.sscs.config.WarningMessage.getMessageByValidationType;

@Component
@Slf4j
public class AppealValidator {

    static final String ERRORS = "errors";
    static final String WARNINGS = "warnings";

    private final AppellantValidator appellantValidator;
    private final RepresentativeValidator repValidator;
    private final OtherPartyValidator otherPartyValidator;
    private final DwpAddressLookupService dwpAddressLookupService;
    private final ValidationType validationType;

    public AppealValidator(DwpAddressLookupService dwpAddressLookupService,
                           AddressValidator addressValidator,
                           ValidationType validationType,
                           @Value("#{'${validation.titles}'.split(',')}") List<String> titles) {
        this.dwpAddressLookupService = dwpAddressLookupService;
        this.appellantValidator = new AppellantValidator(addressValidator, validationType, titles);
        this.repValidator = new RepresentativeValidator(addressValidator, validationType, titles);
        this.otherPartyValidator = new OtherPartyValidator(validationType, titles);
        this.validationType = validationType;
    }

    public Map<String, List<String>> validateAppeal(Map<String, Object> ocrCaseData, Map<String, Object> caseData,
                                                    boolean ignoreMrnValidation, boolean ignoreWarnings,
                                                    boolean ignorePartyRoleValidation) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        appendErrorsAndWarnings(errors, warnings,
                appellantValidator.checkAppellant(ocrCaseData, caseData, ignorePartyRoleValidation, ignoreWarnings));

        Appeal appeal = (Appeal) caseData.get("appeal");
        appendErrorsAndWarnings(errors, warnings, repValidator.checkRepresentative(appeal, ocrCaseData, caseData));

        FormType formType = (FormType) caseData.get("formType");
        warnings.addAll(checkMrnDetails(appeal, ocrCaseData, ignoreMrnValidation, formType));

        if (formType != null && formType.equals(FormType.SSCS2)) {
            warnings.addAll(checkChildMaintenance(caseData, ignoreWarnings));
            warnings.addAll(otherPartyValidator.checkOtherParty(caseData, ignoreWarnings));
        }

        warnings.addAll(checkExcludedDates(appeal));

        appendErrorsAndWarnings(errors, warnings, checkBenefitType(appeal, formType));

        warnings.addAll(validateHearing(appeal, caseData));

        if (caseData.get("sscsDocument") != null) {
            @SuppressWarnings("unchecked")
            List<SscsDocument> lists = ((List<SscsDocument>) caseData.get("sscsDocument"));
            errors.addAll(checkAdditionalEvidence(lists));
        }

        return Map.of(ERRORS, errors, WARNINGS, warnings);
    }

    private List<String> checkMrnDetails(Appeal appeal, Map<String, Object> ocrCaseData, boolean ignoreMrnValidation, FormType formType) {
        List<String> warnings = new ArrayList<>();

        // if Appeal to Proceed direction type for direction Issue event and mrn date is blank then ignore mrn date validation
        if (!ignoreMrnValidation && !doesMrnDateExist(appeal)) {
            warnings.add(getMessageByValidationType(validationType,"", MRN_DATE, IS_EMPTY));
        } else if (!ignoreMrnValidation) {
            warnings.addAll(validateDate(validationType, "", appeal.getMrnDetails().getMrnDate(), MRN_DATE, true));
        }

        String dwpIssuingOffice = getDwpIssuingOffice(appeal, ocrCaseData);
        if (dwpIssuingOffice != null && appeal.getBenefitType() != null && appeal.getBenefitType().getCode() != null) {

            Optional<OfficeMapping> officeMapping =
                    dwpAddressLookupService.getDwpMappingByOffice(appeal.getBenefitType().getCode(), dwpIssuingOffice);

            if (officeMapping.isEmpty()) {
                log.info("DwpHandling handling office is not valid");
                warnings.add(getMessageByValidationType(validationType,"",ISSUING_OFFICE, IS_INVALID));
            }
        } else if (dwpIssuingOffice == null && !FormType.SSCS2.equals(formType) && !FormType.SSCS5.equals(formType)) {
            warnings.add(getMessageByValidationType(validationType,"",ISSUING_OFFICE, IS_EMPTY));
        }
        return warnings;
    }

    private List<String> checkChildMaintenance(Map<String, Object> caseData, boolean ignoreWarnings) {
        List<String> warnings = new ArrayList<>();
        String childMaintenanceNumber = (String) caseData.get("childMaintenanceNumber");
        if (!ignoreWarnings && StringUtils.isBlank(childMaintenanceNumber)) {
            warnings.add(getMessageByValidationType(validationType, "", PERSON_1_CHILD_MAINTENANCE_NUMBER, IS_EMPTY));
        } else if (ignoreWarnings) {
            caseData.remove("childMaintenanceNumber");
        }
        return warnings;
    }

    private List<String> checkExcludedDates(Appeal appeal) {
        List<String> warnings = new ArrayList<>();
        if (appeal.getHearingOptions() != null && appeal.getHearingOptions().getExcludeDates() != null) {
            for (ExcludeDate excludeDate : appeal.getHearingOptions().getExcludeDates()) {
                warnings.addAll(
                        validateDate(validationType, "", excludeDate.getValue().getStart(), HEARING_OPTIONS_EXCLUDE_DATES_LITERAL, false));
            }
        }
        return warnings;
    }

    private Map<String, List<String>> checkBenefitType(Appeal appeal, FormType formType) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        BenefitType benefitType = appeal.getBenefitType();
        if (benefitType != null && benefitType.getCode() != null) {
            final Optional<Benefit> benefitOptional = Benefit.findBenefitByShortName(benefitType.getCode());
            if (benefitOptional.isEmpty()) {
                List<String> benefitNameList = new ArrayList<>();
                for (Benefit be : Benefit.values()) {
                    benefitNameList.add(be.getShortName());
                }
                errors.add(getMessageByValidationType(validationType, "", BENEFIT_TYPE_DESCRIPTION,
                        "invalid. Should be one of: " + String.join(", ", benefitNameList)));
            } else {
                Benefit benefit = benefitOptional.get();
                appeal.setBenefitType(BenefitType.builder()
                        .code(benefit.getShortName())
                        .description(benefit.getDescription())
                        .build());
            }
        } else {
            if (formType == null || (!formType.equals(FormType.SSCS1U) && !formType.equals(FormType.SSCS5))) {
                warnings.add(getMessageByValidationType(validationType, "", BENEFIT_TYPE_DESCRIPTION, IS_EMPTY));
            }
        }
        return Map.of("errors", errors, "warnings", warnings);
    }

    private List<String> validateHearing(Appeal appeal, Map<String, Object> caseData) {
        List<String> warnings = new ArrayList<>();
        String hearingType = appeal.getHearingType();

        if (hearingType == null
                || (!hearingType.equals(HEARING_TYPE_ORAL) && !hearingType.equals(HEARING_TYPE_PAPER))) {
            warnings.add(getMessageByValidationType(validationType, "", HEARING_TYPE_DESCRIPTION, IS_INVALID));
        }

        FormType formType = (FormType) caseData.get("formType");
        log.info("Bulk-scan form type: {}", formType != null ? formType.toString() : null);
        if ((FormType.SSCS1PEU.equals(formType) || FormType.SSCS2.equals(formType) || FormType.SSCS5.equals(formType))
                && hearingType != null && hearingType.equals(HEARING_TYPE_ORAL)
                && !isValidHearingSubType(appeal)) {
            warnings.add(
                    getMessageByValidationType(validationType, "", HEARING_SUB_TYPE_TELEPHONE_OR_VIDEO_FACE_TO_FACE_DESCRIPTION,
                            ARE_EMPTY));
        }
        return warnings;
    }

    private List<String> checkAdditionalEvidence(List<SscsDocument> sscsDocuments) {
        List<String> errors = new ArrayList<>();
        sscsDocuments.stream()
                .filter(sscsDocument -> sscsDocument.getValue().getDocumentFileName() == null)
                .forEach(sscsDocument -> errors.add(
                        "There is a file attached to the case that does not have a filename, add a filename, e.g. filename.pdf"));

        sscsDocuments.stream()
                .filter(sscsDocument -> sscsDocument.getValue().getDocumentLink() != null
                        && sscsDocument.getValue().getDocumentLink().getDocumentFilename() != null
                        && sscsDocument.getValue().getDocumentLink().getDocumentFilename().indexOf('.') == -1)
                .forEach(sscsDocument ->
                        errors.add("There is a file attached to the case called "
                                + sscsDocument.getValue().getDocumentLink().getDocumentFilename()
                                + ", filenames must have extension, e.g. filename.pdf")
                );
        return errors;
    }
}
