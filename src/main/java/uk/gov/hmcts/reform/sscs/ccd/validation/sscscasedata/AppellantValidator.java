package uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.callback.ValidationType;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appeal;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appellant;
import uk.gov.hmcts.reform.sscs.ccd.domain.AppellantRole;
import uk.gov.hmcts.reform.sscs.ccd.domain.FormType;
import uk.gov.hmcts.reform.sscs.ccd.domain.HearingSubtype;
import uk.gov.hmcts.reform.sscs.ccd.domain.Name;
import uk.gov.hmcts.reform.sscs.ccd.domain.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static uk.gov.hmcts.reform.sscs.config.SscsConstants.ADDRESS_LINE1;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.ADDRESS_LINE3;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.ADDRESS_LINE4;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.ADDRESS_POSTCODE;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.EMAIL_SELECTED_NOT_PROVIDED;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.FIRST_NAME;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.HEARING_TELEPHONE_NUMBER_MULTIPLE_LITERAL;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.HEARING_TYPE_TELEPHONE_LITERAL;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.HEARING_TYPE_VIDEO_LITERAL;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.IS_EMPTY;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.IS_INVALID;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.IS_MISSING;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.LAST_NAME;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.NINO;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.PHONE_SELECTED_NOT_PROVIDED;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.TITLE;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.YES_LITERAL;
import static uk.gov.hmcts.reform.sscs.config.WarningMessage.APPELLANT_PARTY_DESCRIPTION;
import static uk.gov.hmcts.reform.sscs.config.WarningMessage.APPELLANT_PARTY_NAME;
import static uk.gov.hmcts.reform.sscs.config.WarningMessage.getMessageByValidationType;

@Component
@Slf4j
public class AppellantValidator extends PartyValidator {
    
    private static final String NINO_REGEX =
            "^(?!BG)(?!GB)(?!NK)(?!KN)(?!TN)(?!NT)(?!ZZ)\\s?(?:[A-CEGHJ-PR-TW-Z]\\s?[A-CEGHJ-NPR-TW-Z])\\s?(?:\\d\\s?){6}([A-D]|\\s)\\s?$";
    public static final String PERSON1_VALUE = "person1";

    private final AddressValidator addressValidator;

    public AppellantValidator(AddressValidator addressValidator,
                              ValidationType validationType, List<String> titles) {
        super(validationType, titles);
        this.addressValidator = addressValidator;
    }

    public Map<String, List<String>> checkAppellant(Map<String, Object> ocrCaseData,
                                                              Map<String, Object> caseData,
                                                              boolean ignorePartyRoleValidation, boolean ignoreWarnings) {
        List<String> warnings = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        Appeal appeal = (Appeal) caseData.get("appeal");
        Appellant appellant = appeal.getAppellant();

        if (appellant == null) {
            warnings.add(
                    getMessageByValidationType(validationType, PERSON1_VALUE, getWarningMessageName(PERSON1_VALUE) + TITLE, IS_EMPTY));
            warnings.add(
                    getMessageByValidationType(validationType, PERSON1_VALUE, getWarningMessageName(PERSON1_VALUE) + FIRST_NAME, IS_EMPTY));
            warnings.add(
                    getMessageByValidationType(validationType, PERSON1_VALUE, getWarningMessageName(PERSON1_VALUE) + LAST_NAME, IS_EMPTY));
            warnings.add(
                    getMessageByValidationType(validationType, PERSON1_VALUE, getWarningMessageName(PERSON1_VALUE) + ADDRESS_LINE1, IS_EMPTY));
            warnings.add(
                    getMessageByValidationType(validationType, PERSON1_VALUE, getWarningMessageName(PERSON1_VALUE) + ADDRESS_LINE3, IS_EMPTY));
            warnings.add(
                    getMessageByValidationType(validationType, PERSON1_VALUE, getWarningMessageName(PERSON1_VALUE) + ADDRESS_LINE4, IS_EMPTY));
            warnings.add(
                    getMessageByValidationType(validationType, PERSON1_VALUE, getWarningMessageName(PERSON1_VALUE) + ADDRESS_POSTCODE, IS_EMPTY));
            warnings.add(getMessageByValidationType(validationType, getPerson1OrPerson2(appellant),getWarningMessageName(PERSON1_VALUE) + NINO,IS_EMPTY));
        } else {
            String personType = getPerson1OrPerson2(appellant);
            appendErrorsAndWarnings(errors, warnings, checkAppointee(appellant, ocrCaseData, caseData));

            warnings.addAll(checkPersonName(appellant.getName(), appellant, getPerson1OrPerson2(appellant)));

            appendErrorsAndWarnings(errors, warnings,
                    addressValidator.checkPersonAddress(appellant.getAddress(), personType, validationType, ocrCaseData, caseData, appellant));

            warnings.addAll(checkPersonDob(appellant.getIdentity(), validationType, personType, appellant));

            warnings.addAll(checkAppellantNino(appellant));

            errors.addAll(checkMobileNumber(appellant.getContact(), validationType, personType));

            warnings.addAll(checkHearingSubtypeDetails(appeal.getHearingSubtype()));
            
            FormType formType = (FormType) caseData.get("formType");
            if (!ignorePartyRoleValidation && formType != null && formType.equals(FormType.SSCS2)) {
                warnings.addAll(checkAppellantRole(appellant.getRole(), ignoreWarnings));
            }
        }
        return Map.of("warnings", warnings, "errors", errors);
    }

    public Map<String, List<String>> checkAppointee(Appellant appellant, Map<String, Object> ocrCaseData, Map<String, Object> caseData) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        if (appellant != null && !isAppointeeDetailsEmpty(appellant.getAppointee())) {
            warnings.addAll(checkPersonName(appellant.getAppointee().getName(), appellant, PERSON1_VALUE));
            appendErrorsAndWarnings(errors, warnings,
                    addressValidator.checkPersonAddress(appellant.getAppointee().getAddress(), PERSON1_VALUE, validationType, ocrCaseData, caseData, appellant));
            warnings.addAll(checkPersonDob(appellant.getAppointee().getIdentity(), validationType, PERSON1_VALUE, appellant));
            errors.addAll(checkMobileNumber(appellant.getAppointee().getContact(), validationType, PERSON1_VALUE));
        }
        return Map.of("errors", errors, "warnings", warnings);
    }

    private List<String> checkAppellantNino(Appellant appellant) {
        List<String> warnings = new ArrayList<>();

        if (appellant != null && appellant.getIdentity() != null && appellant.getIdentity().getNino() != null) {
            if (!appellant.getIdentity().getNino().matches(NINO_REGEX)) {
                warnings.add(
                        getMessageByValidationType(validationType, getPerson1OrPerson2(appellant), getWarningMessageName(appellant) + NINO, IS_INVALID));
            }
        } else {
            warnings.add(
                    getMessageByValidationType(validationType, getPerson1OrPerson2(appellant), getWarningMessageName(appellant) + NINO, IS_EMPTY));
        }
        return warnings;
    }

    private List<String> checkAppellantRole(Role role, boolean ignoreWarnings) {
        List<String> warnings = new ArrayList<>();
        if (role == null && !ignoreWarnings) {
            warnings.add(getMessageByValidationType(validationType, "", APPELLANT_PARTY_NAME.toString(), IS_MISSING));
        } else if (!ignoreWarnings) {
            String name = role.getName();
            String description = role.getDescription();
            if (StringUtils.isEmpty(name)) {
                warnings.add(getMessageByValidationType(validationType, "", APPELLANT_PARTY_NAME.toString(), IS_MISSING));
            } else if (AppellantRole.OTHER.getName().equalsIgnoreCase(name) && StringUtils.isEmpty(description)) {
                warnings.add(getMessageByValidationType(validationType, "", APPELLANT_PARTY_DESCRIPTION.toString(), IS_MISSING));
            }
        }
        return warnings;
    }

    private List<String> checkHearingSubtypeDetails(HearingSubtype hearingSubtype) {
        List<String> warnings = new ArrayList<>();

        if (hearingSubtype != null) {
            if (YES_LITERAL.equals(hearingSubtype.getWantsHearingTypeTelephone())
                    && hearingSubtype.getHearingTelephoneNumber() == null) {

                warnings.add(getMessageByValidationType(validationType, "", HEARING_TYPE_TELEPHONE_LITERAL, PHONE_SELECTED_NOT_PROVIDED));

            } else if (hearingSubtype.getHearingTelephoneNumber() != null
                    && !isUkNumberValid(hearingSubtype.getHearingTelephoneNumber())) {

                warnings
                        .add(getMessageByValidationType(validationType, "", HEARING_TELEPHONE_NUMBER_MULTIPLE_LITERAL, null));
            }

            if (YES_LITERAL.equals(hearingSubtype.getWantsHearingTypeVideo())
                    && hearingSubtype.getHearingVideoEmail() == null) {

                warnings.add(getMessageByValidationType(validationType, "", HEARING_TYPE_VIDEO_LITERAL, EMAIL_SELECTED_NOT_PROVIDED));
            }
        }
        return warnings;
    }

    private List<String> checkPersonName(Name name, Appellant appellant, String personType) {
        List<String> warnings = new ArrayList<>();

        if (!doesTitleExist(name)) {
            warnings.add(getMessageByValidationType(validationType, personType, getWarningMessageName(appellant) + TITLE, IS_EMPTY));
        } else if (name != null && !isTitleValid(name.getTitle())) {
            warnings.add(getMessageByValidationType(validationType, personType,  getWarningMessageName(appellant) + TITLE, IS_INVALID));
        }

        if (!doesFirstNameExist(name)) {
            warnings.add(getMessageByValidationType(validationType, personType, getWarningMessageName(appellant) + FIRST_NAME, IS_EMPTY));
        }

        if (!doesLastNameExist(name)) {
            warnings.add(getMessageByValidationType(validationType, personType, getWarningMessageName(appellant) + LAST_NAME, IS_EMPTY));
        }
        return warnings;
    }
}
