package uk.gov.hmcts.reform.sscs.utility;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;

public final class PhoneNumbersUtil {

    private static final String DEFAULT_REGION = Locale.UK.getCountry();

    private PhoneNumbersUtil() {
        // Void
    }

    public static String cleanPhoneNumber(final String phone) {
        final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        final Optional<Phonenumber.PhoneNumber> phoneNumberOpt = parsePhoneNumber(phone, phoneNumberUtil);
        return phoneNumberOpt.map(phoneNumber -> String.format("+%d%d", phoneNumber.getCountryCode(), phoneNumber.getNationalNumber())).orElse("");
    }

    public static boolean isValidUkMobileNumber(final String phone) {
        final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        final Optional<Phonenumber.PhoneNumber> phoneNumberOpt = parsePhoneNumber(phone, phoneNumberUtil);
        return containsPhoneNumberType(phoneNumberUtil, phoneNumberOpt,
                PhoneNumberUtil.PhoneNumberType.MOBILE, PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE) &&
                phoneNumberOpt.map(phoneNumber -> 44 == phoneNumber.getCountryCode()).orElse(false);
    }

    public static boolean isValidMobileNumber(final String phone) {
        return isValidNumber(phone, PhoneNumberUtil.PhoneNumberType.MOBILE, PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE);
    }

    public static boolean isValidLandLineNumber(final String phone) {
        return isValidNumber(phone, PhoneNumberUtil.PhoneNumberType.FIXED_LINE, PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE);
    }

    private static boolean isValidNumber(final String phone, final PhoneNumberUtil.PhoneNumberType... phoneNumberTypes) {
        final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        final Optional<Phonenumber.PhoneNumber> phoneNumberOpt = parsePhoneNumber(phone, phoneNumberUtil);
        return containsPhoneNumberType(phoneNumberUtil, phoneNumberOpt, phoneNumberTypes);
    }

    private static boolean containsPhoneNumberType(final PhoneNumberUtil phoneNumberUtil,
                                                   final Optional<Phonenumber.PhoneNumber> phoneNumberOpt,
                                                   final PhoneNumberUtil.PhoneNumberType... phoneNumberTypes) {
        return phoneNumberOpt.map(phoneNumber ->
                ArrayUtils.contains(phoneNumberTypes, phoneNumberUtil.getNumberType(phoneNumber))).orElse(false);
    }

    private static Optional<Phonenumber.PhoneNumber> parsePhoneNumber(final String phone,
                                                                      final PhoneNumberUtil phoneNumberUtil) {
        Optional<Phonenumber.PhoneNumber> phoneNumberOpt = Optional.empty();
        try {
            phoneNumberOpt = Optional.of(phoneNumberUtil.parse(phone, DEFAULT_REGION));
        } catch (final Exception ignored) {
            // ignored
        }
        return phoneNumberOpt;
    }
}
