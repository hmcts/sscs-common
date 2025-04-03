package uk.gov.hmcts.reform.sscs.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;

@SuppressWarnings({"PMD.JUnitTestContainsTooManyAsserts", "PMD.LongVariable"})
public class PhoneNumbersUtilTest {

    private static final String UK_MOBILE_NUMBER = "07861675377";
    private static final String UK_MOBILE_NUMBER_WITH_COUNTRY_CODE = "(+44) 7861675377";
    private static final String UK_LAND_LINE = "0203 737 4299";
    private static final String UK_LAND_LINE_WITH_COUNTRY_CODE = "+44 203 737 4299";
    private static final String US_LAND_LINE_WITH_COUNTRY_CODE = "+1-541-754-3010";
    private static final String ASSERTION_FALSE_MESSAGE = "Does not equal false";
    private static final String ASSERTION_TRUE_MESSAGE = "Does not equal true";
    private static final String ASSERTION_EQUALS_MESSAGE = "Is not equal";

    @Test
    public void isValidMobileNumberWillReturnTrueForAUkMobileNumber() {
        assertTrue(PhoneNumbersUtil.isValidMobileNumber(UK_MOBILE_NUMBER), ASSERTION_TRUE_MESSAGE);
        assertTrue(PhoneNumbersUtil.isValidMobileNumber(UK_MOBILE_NUMBER_WITH_COUNTRY_CODE), ASSERTION_TRUE_MESSAGE);
    }

    @Test
    public void isValidMobileNumberWillReturnFalseForLandLineOrInvalidValues() {
        assertFalse(PhoneNumbersUtil.isValidMobileNumber(""), ASSERTION_FALSE_MESSAGE);
        assertFalse(PhoneNumbersUtil.isValidMobileNumber(null), ASSERTION_FALSE_MESSAGE);
        assertFalse(PhoneNumbersUtil.isValidMobileNumber(UK_LAND_LINE), ASSERTION_FALSE_MESSAGE);
        assertFalse(PhoneNumbersUtil.isValidMobileNumber(UK_LAND_LINE_WITH_COUNTRY_CODE), ASSERTION_FALSE_MESSAGE);
    }

    @Test
    public void isValidUkMobileNumberWillBeTrueForValidUkMobileNumbers() {
        assertTrue(PhoneNumbersUtil.isValidUkMobileNumber(UK_MOBILE_NUMBER), ASSERTION_TRUE_MESSAGE);
        assertTrue(PhoneNumbersUtil.isValidUkMobileNumber(UK_MOBILE_NUMBER_WITH_COUNTRY_CODE), ASSERTION_TRUE_MESSAGE);
        assertTrue(PhoneNumbersUtil.isValidUkMobileNumber("07123456789"), ASSERTION_TRUE_MESSAGE);
        assertTrue(PhoneNumbersUtil.isValidUkMobileNumber("07123 456789"), ASSERTION_TRUE_MESSAGE);
        assertTrue(PhoneNumbersUtil.isValidUkMobileNumber("07123 456 789"), ASSERTION_TRUE_MESSAGE);
        assertTrue(PhoneNumbersUtil.isValidUkMobileNumber("+447123456789"), ASSERTION_TRUE_MESSAGE);
        assertTrue(PhoneNumbersUtil.isValidUkMobileNumber("+44 7123 456 789"), ASSERTION_TRUE_MESSAGE);
    }

    @Test
    public void isValidUkMobileNumberWillBeFalseForLandLineNumbers() {
        assertFalse(PhoneNumbersUtil.isValidUkMobileNumber(UK_LAND_LINE), ASSERTION_FALSE_MESSAGE);
        assertFalse(PhoneNumbersUtil.isValidUkMobileNumber(UK_LAND_LINE_WITH_COUNTRY_CODE), ASSERTION_FALSE_MESSAGE);
        assertFalse(PhoneNumbersUtil.isValidUkMobileNumber("01234567890"), ASSERTION_FALSE_MESSAGE);
        assertFalse(PhoneNumbersUtil.isValidUkMobileNumber("08001234567"), ASSERTION_FALSE_MESSAGE);
    }

    @Test
    public void isValidUkMobileNumberWillReturnFalseForInvalidValues() {
        assertFalse(PhoneNumbersUtil.isValidUkMobileNumber("0744"), ASSERTION_FALSE_MESSAGE);
        assertFalse(PhoneNumbersUtil.isValidUkMobileNumber(null), ASSERTION_FALSE_MESSAGE);
        assertFalse(PhoneNumbersUtil.isValidUkMobileNumber(""), ASSERTION_FALSE_MESSAGE);
    }

    @Test
    public void isValidLandLineNumberReturnsTrueForLandlineNumbers() {
        assertTrue(PhoneNumbersUtil.isValidLandLineNumber(US_LAND_LINE_WITH_COUNTRY_CODE), ASSERTION_TRUE_MESSAGE);
        assertTrue(PhoneNumbersUtil.isValidLandLineNumber(UK_LAND_LINE), ASSERTION_TRUE_MESSAGE);
    }

    @Test
    public void cleanNumberForUkMobile() {
        assertEquals(Optional.of("+447861675377"), PhoneNumbersUtil.cleanPhoneNumber(UK_MOBILE_NUMBER), ASSERTION_EQUALS_MESSAGE);
    }

    @Test
    public void cleanNumberForUkLandLine() {
        assertEquals(Optional.of("+442037374299"), PhoneNumbersUtil.cleanPhoneNumber(UK_LAND_LINE), ASSERTION_EQUALS_MESSAGE);
    }

    @Test
    public void cleanNumberForUkLandLineWithCountryCode() {
        assertEquals(Optional.of("+442037374299"), PhoneNumbersUtil.cleanPhoneNumber(UK_LAND_LINE_WITH_COUNTRY_CODE), ASSERTION_EQUALS_MESSAGE);
    }

    @Test
    public void cleanNumberForAnAmericanLandLine() {
        assertEquals(Optional.of("+15417543010"), PhoneNumbersUtil.cleanPhoneNumber(US_LAND_LINE_WITH_COUNTRY_CODE), ASSERTION_EQUALS_MESSAGE);
    }

    @Test
    public void cleanNumberForAnInvalidNumberReturnsEmptyOption() {
        assertEquals(Optional.empty(), PhoneNumbersUtil.cleanPhoneNumber("99a"), ASSERTION_EQUALS_MESSAGE);
    }
}
