package uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.callback.ValidationType;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appeal;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appellant;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appointee;
import uk.gov.hmcts.reform.sscs.ccd.domain.Contact;
import uk.gov.hmcts.reform.sscs.ccd.domain.HearingSubtype;
import uk.gov.hmcts.reform.sscs.ccd.domain.Identity;
import uk.gov.hmcts.reform.sscs.ccd.domain.Name;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static uk.gov.hmcts.reform.sscs.config.SscsConstants.DOB;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.IS_INVALID;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.IS_IN_FUTURE;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.IS_IN_PAST;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.MOBILE;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.OTHER_PARTY_VALUE;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.REPRESENTATIVE_VALUE;
import static uk.gov.hmcts.reform.sscs.config.WarningMessage.getMessageByValidationType;
import static uk.gov.hmcts.reform.sscs.utility.SscsOcrDataUtil.getField;

@Component
@Slf4j
public class PartyValidator {

    @SuppressWarnings("squid:S5843")
    private static final String PHONE_REGEX =
            "^((?:(?:\\(?(?:0(?:0|11)\\)?[\\s-]?\\(?|\\+)\\d{1,4}\\)?[\\s-]?(?:\\(?0\\)?[\\s-]?)?)|(?:\\(?0))(?:"
                    + "(?:\\d{5}\\)?[\\s-]?\\d{4,5})|(?:\\d{4}\\)?[\\s-]?(?:\\d{5}|\\d{3}[\\s-]?\\d{3}))|(?:\\d{3}\\)"
                    + "?[\\s-]?\\d{3}[\\s-]?\\d{3,4})|(?:\\d{2}\\)?[\\s-]?\\d{4}[\\s-]?\\d{4}))(?:[\\s-]?(?:x|ext\\.?|\\#)"
                    + "\\d{3,4})?)?$";
    @SuppressWarnings("squid:S5843")
    private static final String UK_NUMBER_REGEX =
            "^\\(?(?:(?:0(?:0|11)\\)?[\\s-]?\\(?|\\+)44\\)?[\\s-]?\\(?(?:0\\)?[\\s-]?\\(?)?|0)(?:\\d{2}\\)?[\\s-]?\\d{4}"
                    + "[\\s-]?\\d{4}|\\d{3}\\)?[\\s-]?\\d{3}[\\s-]?\\d{3,4}|\\d{4}\\)?[\\s-]?(?:\\d{5}|\\d{3}[\\s-]?\\d{3})|"
                    +
                    "\\d{5}\\)?[\\s-]?\\d{4,5}|8(?:00[\\s-]?11[\\s-]?11|45[\\s-]?46[\\s-]?4\\d))(?:(?:[\\s-]?(?:x|ext\\.?\\s?|"
                    + "\\#)\\d+)?)$";
    private static final String PERSON1_VALUE = "person1";
    private static final String PERSON2_VALUE = "person2";

    ValidationType validationType;
    List<String> titles;

    public PartyValidator(ValidationType validationType, List<String> titles) {
        this.validationType = validationType;
        this.titles = titles;
    }

    public boolean isTitleValid(String title) {
        if (StringUtils.isNotBlank(title)) {
            String strippedTitle = title.replaceAll("[-+.^:,'_]", "");
            return this.titles.stream().anyMatch(strippedTitle::equalsIgnoreCase);
        }
        return true;
    }

    public static String getPerson1OrPerson2(Appellant appellant) {
        if (appellant == null || isAppointeeDetailsEmpty(appellant.getAppointee())) {
            return PERSON1_VALUE;
        } else {
            return PERSON2_VALUE;
        }
    }

    public static Boolean isContactEmpty(Contact contact) {
        return contact == null
                || (contact.getEmail() == null
                && contact.getPhone() == null
                && contact.getMobile() == null);
    }

    public static Boolean isIdentityEmpty(Identity identity) {
        return identity == null
                || (identity.getDob() == null
                && identity.getNino() == null);
    }

    public static Boolean isNameEmpty(Name name) {
        return name == null
                || (name.getFirstName() == null
                && name.getLastName() == null
                && name.getTitle() == null);
    }

    public static Boolean doesTitleExist(Name name) {
        if (name != null) {
            return StringUtils.isNotEmpty(name.getTitle());
        }
        return false;
    }

    public static Boolean doesFirstNameExist(Name name) {
        if (name != null) {
            return StringUtils.isNotEmpty(name.getFirstName());
        }
        return false;
    }

    public static Boolean doesLastNameExist(Name name) {
        if (name != null) {
            return StringUtils.isNotEmpty(name.getLastName());
        }
        return false;
    }

    public static Boolean doesIssuingOfficeExist(Appeal appeal) {
        if (appeal.getMrnDetails() != null) {
            return StringUtils.isNotEmpty(appeal.getMrnDetails().getDwpIssuingOffice());
        }
        return false;
    }

    public static Boolean doesMrnDateExist(Appeal appeal) {
        if (appeal.getMrnDetails() != null) {
            return appeal.getMrnDetails().getMrnDate() != null;
        }
        return false;
    }

    public static String getWarningMessageName(String personType) {
        return getWarningMessageName(personType, null);
    }

    public static String getWarningMessageName(Appellant appellant) {
        return getWarningMessageName(getPerson1OrPerson2(appellant), appellant);
    }
    public static String getWarningMessageName(String personType, Appellant appellant) {
        if (personType.equals(REPRESENTATIVE_VALUE)) {
            return "REPRESENTATIVE";
        } else if (personType.equals(OTHER_PARTY_VALUE)) {
            return "OTHER_PARTY";
        } else if (personType.equals(PERSON2_VALUE) || appellant == null
                || isAppointeeDetailsEmpty(appellant.getAppointee())) {
            return "APPELLANT";
        } else {
            return "APPOINTEE";
        }
    }

    public static Boolean isAppointeeDetailsEmpty(Appointee appointee) {
        return appointee == null
                || (isAddressEmpty(appointee.getAddress())
                && isContactEmpty(appointee.getContact())
                && isIdentityEmpty(appointee.getIdentity())
                && isNameEmpty(appointee.getName()));
    }

    public static Boolean isAddressEmpty(Address address) {
        return address == null
                || (address.getLine1() == null
                && address.getLine2() == null
                && address.getTown() == null
                && address.getCounty() == null
                && address.getPostcode() == null);
    }
    
    public static List<String> checkMobileNumber(Contact contact, ValidationType validationType, String personType) {
        List<String> errors = new ArrayList<>();
        if (contact != null && contact.getMobile() != null && !isMobileNumberValid(contact.getMobile())) {
            errors.add(getMessageByValidationType(validationType, personType, getWarningMessageName(personType) + MOBILE, IS_INVALID));
            return errors;
        }
        return errors;
    }
    public static boolean isMobileNumberValid(String number) {
        if (number != null) {
            return number.matches(PHONE_REGEX);
        }
        return true;
    }
    public static boolean isUkNumberValid(String number) {
        if (number != null) {
            return number.matches(UK_NUMBER_REGEX);
        }
        return true;
    }

    public static String getDwpIssuingOffice(Appeal appeal, Map<String, Object> ocrCaseData) {
        if (Boolean.TRUE.equals(doesIssuingOfficeExist(appeal))) {
            return appeal.getMrnDetails().getDwpIssuingOffice();
        } else if (!ocrCaseData.isEmpty()) {
            return getField(ocrCaseData, "office");
        } else {
            return null;
        }
    }

    public static List<String> checkPersonDob(Identity identity, ValidationType validationType, String personType, Appellant appellant) {
        if (identity != null) {
            return validateDate(validationType, personType, identity.getDob(), getWarningMessageName(personType, appellant) + DOB, true);
        }
        return null;
    }

    public static List<String> validateDate(ValidationType validationType, String personType,
                                            String dateField, String fieldName, Boolean isInFutureCheck) {
        List<String> warnings = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (!StringUtils.isEmpty(dateField)) {
            try {
                LocalDate date = LocalDate.parse(dateField, formatter);

                if (isInFutureCheck && date.isAfter(LocalDate.now())) {
                    warnings.add(getMessageByValidationType(validationType, personType, fieldName, IS_IN_FUTURE));
                } else if (!isInFutureCheck && date.isBefore(LocalDate.now())) {
                    warnings.add(getMessageByValidationType(validationType, personType,fieldName, IS_IN_PAST));

                }
            } catch (DateTimeParseException ex) {
                log.error("Date time error", ex);
            }
        }
        return warnings;
    }
    
    public static void appendErrorsAndWarnings(List<String> errors, List<String> warnings, Map<String, List<String>> errsWarnsMap) {
        errors.addAll(errsWarnsMap.get("errors"));
        warnings.addAll(errsWarnsMap.get("warnings"));
    }

    public static boolean isValidHearingSubType(Appeal appeal) {
        boolean isValid = true;
        HearingSubtype hearingSubType = appeal.getHearingSubtype();
        if (hearingSubType == null
                || !(hearingSubType.isWantsHearingTypeTelephone() || hearingSubType.isWantsHearingTypeVideo()
                || hearingSubType.isWantsHearingTypeFaceToFace())) {
            isValid = false;
        }
        return isValid;
    }
}
