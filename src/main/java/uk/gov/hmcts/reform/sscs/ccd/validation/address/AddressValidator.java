package uk.gov.hmcts.reform.sscs.ccd.validation.address;

import static java.util.Optional.ofNullable;
import static uk.gov.hmcts.reform.sscs.ccd.validation.appeal.PartyValidator.appendErrorsAndWarnings;
import static uk.gov.hmcts.reform.sscs.ccd.validation.appeal.PartyValidator.getPerson1OrPerson2;
import static uk.gov.hmcts.reform.sscs.ccd.validation.appeal.PartyValidator.getWarningMessageName;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.ADDRESS_LINE1;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.ADDRESS_LINE2;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.ADDRESS_LINE3;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.ADDRESS_LINE4;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.ADDRESS_POSTCODE;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.HAS_INVALID_ADDRESS;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.IS_EMPTY;
import static uk.gov.hmcts.reform.sscs.config.WarningMessage.getMessageByValidationType;
import static uk.gov.hmcts.reform.sscs.utility.SscsOcrDataUtil.findBooleanExists;
import static uk.gov.hmcts.reform.sscs.utility.SscsOcrDataUtil.getField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.callback.ValidationType;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appellant;
import uk.gov.hmcts.reform.sscs.ccd.domain.RegionalProcessingCenter;
import uk.gov.hmcts.reform.sscs.service.RegionalProcessingCenterService;

@Component
public class AddressValidator {
    public static final String IS_NOT_A_VALID_POSTCODE = "is not a valid postcode";

    @SuppressWarnings("squid:S5843")
    private static final String ADDRESS_REGEX =
            "^[a-zA-ZÀ-ž0-9]{1}[a-zA-ZÀ-ž0-9 \\r\\n\\.“”\",’\\?\\!\\[\\]\\(\\)/£:\\\\_+\\-%&;]{1,}$";
    @SuppressWarnings("squid:S5843")
    private static final String COUNTY_REGEX =
            "^\\.$|^[a-zA-ZÀ-ž0-9]{1}[a-zA-ZÀ-ž0-9 \\r\\n\\.“”\",’\\?\\!\\[\\]\\(\\)/£:\\\\_+\\-%&;]{1,}$";
    private final PostcodeValidator postcodeValidator;
    private final RegionalProcessingCenterService regionalProcessingCenterService;
    List<String> warnings;
    
    public AddressValidator(RegionalProcessingCenterService regionalProcessingCenterService,
                            PostcodeValidator postcodeValidator) {
        this.regionalProcessingCenterService = regionalProcessingCenterService;
        this.postcodeValidator = postcodeValidator;
    }

    public Map<String, List<String>> checkPersonAddress(Address address, String personType, ValidationType validationType,
                                                        Map<String, Object> ocrCaseData, Map<String, Object> caseData,
                                                        Appellant appellant) {
        
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        boolean isAddressLine4Present = findBooleanExists(getField(ocrCaseData, personType + "_address_line4"));

        if (!doesAddressLine1Exist(address)) {
            warnings.add(getMessageByValidationType(validationType, personType, getWarningMessageName(appellant) + ADDRESS_LINE1, IS_EMPTY));
        } else if (!address.getLine1().matches(ADDRESS_REGEX)) {
            warnings.add(getMessageByValidationType(validationType, personType, getWarningMessageName(appellant) + ADDRESS_LINE1, HAS_INVALID_ADDRESS));
        }

        String townLine = (isAddressLine4Present) ? ADDRESS_LINE3 : ADDRESS_LINE2;
        if (!doesAddressTownExist(address)) {

            warnings.add(getMessageByValidationType(validationType, personType, getWarningMessageName(appellant) + townLine, IS_EMPTY));
        } else if (!address.getTown().matches(ADDRESS_REGEX)) {
            warnings.add(getMessageByValidationType(validationType, personType, getWarningMessageName(appellant) + townLine, HAS_INVALID_ADDRESS));
        }

        String countyLine = (isAddressLine4Present) ? ADDRESS_LINE4 : "_ADDRESS_LINE3_COUNTY";
        if (!doesAddressCountyExist(address)) {
            warnings.add(getMessageByValidationType(validationType, personType, getWarningMessageName(appellant) + countyLine, IS_EMPTY));
        } else if (!address.getCounty().matches(COUNTY_REGEX)) {
            warnings.add(getMessageByValidationType(validationType, personType, getWarningMessageName(appellant) + countyLine, HAS_INVALID_ADDRESS));
        }

        ofNullable(validatePostcode(address, appellant, validationType, personType))
                .ifPresentOrElse(errsWarns -> appendErrorsAndWarnings(errors, warnings, errsWarns), () -> {
                    if (address != null) {
                        if (personType.equals(getPerson1OrPerson2(appellant))) {
                            RegionalProcessingCenter rpc = regionalProcessingCenterService.getByPostcode(address.getPostcode());
                            if (rpc != null) {
                                caseData.put("region", rpc.getName());
                                caseData.put("regionalProcessingCenter", rpc);
                            } else {
                                warnings.add(getMessageByValidationType(validationType, personType, getWarningMessageName(appellant)
                                        + ADDRESS_POSTCODE, "is not a postcode that maps to a regional processing center"));
                            }
                        }
                    }
                });

        return Map.of("errors", errors, "warnings", warnings);
    }

    private Map<String, List<String>> validatePostcode(Address address, Appellant appellant, ValidationType validationType, String personType) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        if (address != null && address.getPostcode() != null) {
            if (postcodeValidator.isValidPostcodeFormat(address.getPostcode())) {
                boolean isValidPostcode = postcodeValidator.isValid(address.getPostcode(), null);
                if (!isValidPostcode) {
                    warnings.add(getMessageByValidationType(validationType, personType, getWarningMessageName(appellant) + ADDRESS_POSTCODE, IS_NOT_A_VALID_POSTCODE));
                }
                return isValidPostcode ? null : Map.of("errors", errors, "warnings", warnings);
            }
            errors.add(getMessageByValidationType(validationType, personType, getWarningMessageName(appellant) + ADDRESS_POSTCODE, "is not in a valid format"));
            return Map.of("errors", errors, "warnings", warnings);
        }
        warnings.add(getMessageByValidationType(validationType, personType, getWarningMessageName(appellant) + ADDRESS_POSTCODE, IS_EMPTY));
        return Map.of("errors", errors, "warnings", warnings);
    }

    public static Boolean doesAddressLine1Exist(Address address) {
        if (address != null) {
            return StringUtils.isNotEmpty(address.getLine1());
        }
        return false;
    }

    public static Boolean doesAddressTownExist(Address address) {
        if (address != null) {
            return StringUtils.isNotEmpty(address.getTown());
        }
        return false;
    }

    public static Boolean doesAddressPostcodeExist(Address address) {
        if (address != null) {
            return StringUtils.isNotEmpty(address.getPostcode());
        }
        return false;
    }
    
    private Boolean doesAddressCountyExist(Address address) {
        if (address != null) {
            return StringUtils.isNotEmpty(address.getCounty());
        }
        return false;
    }
}
