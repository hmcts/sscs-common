package uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.callback.ValidationType;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.domain.CcdValue;
import uk.gov.hmcts.reform.sscs.ccd.domain.Name;
import uk.gov.hmcts.reform.sscs.ccd.domain.OtherParty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata.AddressValidator.doesAddressLine1Exist;
import static uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata.AddressValidator.doesAddressPostcodeExist;
import static uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata.AddressValidator.doesAddressTownExist;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.ADDRESS_LINE1;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.ADDRESS_LINE2;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.ADDRESS_POSTCODE;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.FIRST_NAME;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.IS_EMPTY;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.IS_INVALID;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.LAST_NAME;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.OTHER_PARTY_VALUE;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.TITLE;
import static uk.gov.hmcts.reform.sscs.config.WarningMessage.getMessageByValidationType;

@Component
@Slf4j
public class OtherPartyValidator extends PartyValidator {


    public OtherPartyValidator(ValidationType validationType, List<String> titles) {
        super(validationType, titles);
    }

    public List<String> checkOtherParty(Map<String, Object> caseData, boolean ignoreWarnings) {
        @SuppressWarnings("unchecked")
        List<CcdValue<OtherParty>> otherParties = ((List<CcdValue<OtherParty>>) caseData.get("otherParties"));

        List<String> warnings = new ArrayList<>();
        OtherParty otherParty;
        if (otherParties != null && !otherParties.isEmpty()) {
            otherParty = otherParties.get(0).getValue();
            Name name = otherParty.getName();
            Address address = otherParty.getAddress();

            if (!ignoreWarnings) {
                warnings.addAll(validateOtherPartyData(name, address));
            } else {
                if (!doesFirstNameExist(name) || !doesLastNameExist(name)) {
                    caseData.remove("otherParties");
                } else if (!doesAddressLine1Exist(address) || !doesAddressTownExist(address)
                        || !doesAddressPostcodeExist(address)) {
                    otherParty.setAddress(null);
                }
            }
        }
        return warnings;
    }

    private List<String> validateOtherPartyData(Name name, Address address) {
        List<String> warnings = new ArrayList<>();
        if (name != null && !isTitleValid(name.getTitle())) {
            warnings.add(
                    getMessageByValidationType(validationType, "", getWarningMessageName(OTHER_PARTY_VALUE, null) + TITLE, IS_INVALID));
        }

        if (doesFirstNameExist(name) || doesLastNameExist(name)) {
            warnings.addAll(validateOtherPartyName(name));
        }

        if (doesAddressLine1Exist(address) || doesAddressTownExist(address) || doesAddressPostcodeExist(address)) {
            warnings.addAll(validateOtherPartyAddress(address));
        }
        return warnings;
    }

    private List validateOtherPartyName(Name name) {
        List<String> warnings = new ArrayList<>();
        if (!doesFirstNameExist(name)) {
            warnings.add(getMessageByValidationType(validationType, OTHER_PARTY_VALUE, getWarningMessageName(OTHER_PARTY_VALUE, null) + FIRST_NAME, IS_EMPTY));
        }

        if (!doesLastNameExist(name)) {
            warnings.add(getMessageByValidationType(validationType, OTHER_PARTY_VALUE, getWarningMessageName(OTHER_PARTY_VALUE, null) + LAST_NAME, IS_EMPTY));
        }
        return warnings;
    }

    private List<String> validateOtherPartyAddress(Address address) {
        List<String> warnings = new ArrayList<>();

        if (!doesAddressLine1Exist(address)) {
            warnings.add(getMessageByValidationType(validationType, OTHER_PARTY_VALUE, getWarningMessageName(OTHER_PARTY_VALUE, null) + ADDRESS_LINE1, IS_EMPTY));
        }

        if (!doesAddressTownExist(address)) {
            warnings.add(getMessageByValidationType(validationType, OTHER_PARTY_VALUE, getWarningMessageName(OTHER_PARTY_VALUE, null) + ADDRESS_LINE2, IS_EMPTY));
        }

        if (!doesAddressPostcodeExist(address)) {
            warnings.add(getMessageByValidationType(validationType, OTHER_PARTY_VALUE, getWarningMessageName(OTHER_PARTY_VALUE, null) + ADDRESS_POSTCODE, IS_EMPTY));
        }
        return warnings;
    }
}
