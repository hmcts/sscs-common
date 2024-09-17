package uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.callback.ValidationType;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appeal;
import uk.gov.hmcts.reform.sscs.ccd.domain.Contact;
import uk.gov.hmcts.reform.sscs.ccd.domain.Name;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static uk.gov.hmcts.reform.sscs.config.SscsConstants.ARE_EMPTY;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.HAS_REPRESENTATIVE_FIELD_MISSING;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.IS_INVALID;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.REPRESENTATIVE_NAME_OR_ORGANISATION_DESCRIPTION;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.REPRESENTATIVE_VALUE;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.TITLE;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.YES_LITERAL;
import static uk.gov.hmcts.reform.sscs.config.WarningMessage.getMessageByValidationType;

@Component
@Slf4j
public class RepresentativeValidator extends PartyValidator {

    private final AddressValidator addressValidator;

    public RepresentativeValidator(AddressValidator addressValidator,
                                   ValidationType validationType, List<String> titles) {
        super(validationType, titles);
        this.addressValidator = addressValidator;
    }

    public Map<String, List<String>> checkRepresentative(Appeal appeal, Map<String, Object> ocrCaseData, Map<String, Object> caseData) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        if (appeal.getRep() == null || StringUtils.isBlank(appeal.getRep().getHasRepresentative())) {
            errors.add(HAS_REPRESENTATIVE_FIELD_MISSING);
        }
        if (appeal.getRep() != null && StringUtils.equals(appeal.getRep().getHasRepresentative(), YES_LITERAL)) {
            final Contact repsContact = appeal.getRep().getContact();
            appendErrorsAndWarnings(errors, warnings,
                    addressValidator.checkPersonAddress(appeal.getRep().getAddress(), REPRESENTATIVE_VALUE, validationType, ocrCaseData, caseData, appeal.getAppellant()));

            warnings.addAll(checkPersonDob(null, validationType, REPRESENTATIVE_VALUE, appeal.getAppellant()));

            Name name = appeal.getRep().getName();
            if (!isTitleValid(name.getTitle())) {
                warnings.add(getMessageByValidationType(validationType, REPRESENTATIVE_VALUE, getWarningMessageName(REPRESENTATIVE_VALUE, null) + TITLE, IS_INVALID));
            }

            if (!doesFirstNameExist(name) && !doesLastNameExist(name) && appeal.getRep().getOrganisation() == null) {
                warnings.add(getMessageByValidationType(validationType, "", REPRESENTATIVE_NAME_OR_ORGANISATION_DESCRIPTION, ARE_EMPTY));
            }

            errors.addAll(checkMobileNumber(repsContact, validationType, REPRESENTATIVE_VALUE));
        }
        return Map.of("errors", errors, "warnings", warnings);
    }
}
