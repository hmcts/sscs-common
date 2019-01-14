package uk.gov.hmcts.reform.sscs.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import org.junit.Test;

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
        assertTrue(ASSERTION_TRUE_MESSAGE, PhoneNumbersUtil.isValidMobileNumber(UK_MOBILE_NUMBER));
        assertTrue(ASSERTION_TRUE_MESSAGE, PhoneNumbersUtil.isValidMobileNumber(UK_MOBILE_NUMBER_WITH_COUNTRY_CODE));
    }

    @Test
    public void isValidMobileNumberWillReturnFalseForLandLineOrInvalidValues() {
        assertFalse(ASSERTION_FALSE_MESSAGE, PhoneNumbersUtil.isValidMobileNumber(""));
        assertFalse(ASSERTION_FALSE_MESSAGE, PhoneNumbersUtil.isValidMobileNumber(null));
        assertFalse(ASSERTION_FALSE_MESSAGE, PhoneNumbersUtil.isValidMobileNumber(UK_LAND_LINE));
        assertFalse(ASSERTION_FALSE_MESSAGE, PhoneNumbersUtil.isValidMobileNumber(UK_LAND_LINE_WITH_COUNTRY_CODE));
    }

    @Test
    public void isValidUkMobileNumberWillBeTrueForValidUkMobileNumbers() {
        assertTrue(ASSERTION_TRUE_MESSAGE, PhoneNumbersUtil.isValidUkMobileNumber(UK_MOBILE_NUMBER));
        assertTrue(ASSERTION_TRUE_MESSAGE, PhoneNumbersUtil.isValidUkMobileNumber(UK_MOBILE_NUMBER_WITH_COUNTRY_CODE));
        assertTrue(ASSERTION_TRUE_MESSAGE, PhoneNumbersUtil.isValidUkMobileNumber("07123456789"));
        assertTrue(ASSERTION_TRUE_MESSAGE, PhoneNumbersUtil.isValidUkMobileNumber("07123 456789"));
        assertTrue(ASSERTION_TRUE_MESSAGE, PhoneNumbersUtil.isValidUkMobileNumber("07123 456 789"));
        assertTrue(ASSERTION_TRUE_MESSAGE, PhoneNumbersUtil.isValidUkMobileNumber("+447123456789"));
        assertTrue(ASSERTION_TRUE_MESSAGE, PhoneNumbersUtil.isValidUkMobileNumber("+44 7123 456 789"));
    }

    @Test
    public void isValidUkMobileNumberWillBeFalseForLandLineNumbers() {
        assertFalse(ASSERTION_FALSE_MESSAGE, PhoneNumbersUtil.isValidUkMobileNumber(UK_LAND_LINE));
        assertFalse(ASSERTION_FALSE_MESSAGE, PhoneNumbersUtil.isValidUkMobileNumber(UK_LAND_LINE_WITH_COUNTRY_CODE));
        assertFalse(ASSERTION_FALSE_MESSAGE, PhoneNumbersUtil.isValidUkMobileNumber("01234567890"));
        assertFalse(ASSERTION_FALSE_MESSAGE, PhoneNumbersUtil.isValidUkMobileNumber("08001234567"));
    }

    @Test
    public void isValidUkMobileNumberWillReturnFalseForInvalidValues() {
        assertFalse(ASSERTION_FALSE_MESSAGE, PhoneNumbersUtil.isValidUkMobileNumber("0744"));
        assertFalse(ASSERTION_FALSE_MESSAGE, PhoneNumbersUtil.isValidUkMobileNumber(null));
        assertFalse(ASSERTION_FALSE_MESSAGE, PhoneNumbersUtil.isValidUkMobileNumber(""));
    }

    @Test
    public void isValidLandLineNumberReturnsTrueForLandlineNumbers() {
        assertTrue(ASSERTION_TRUE_MESSAGE, PhoneNumbersUtil.isValidLandLineNumber(US_LAND_LINE_WITH_COUNTRY_CODE));
        assertTrue(ASSERTION_TRUE_MESSAGE, PhoneNumbersUtil.isValidLandLineNumber(UK_LAND_LINE));
    }

    @Test
    public void cleanNumberForUkMobile() {
        assertEquals(ASSERTION_EQUALS_MESSAGE, Optional.of("+447861675377"), PhoneNumbersUtil.cleanPhoneNumber(UK_MOBILE_NUMBER));
    }

    @Test
    public void cleanNumberForUkLandLine() {
        assertEquals(ASSERTION_EQUALS_MESSAGE, Optional.of("+442037374299"), PhoneNumbersUtil.cleanPhoneNumber(UK_LAND_LINE));
    }

    @Test
    public void cleanNumberForUkLandLineWithCountryCode() {
        assertEquals(ASSERTION_EQUALS_MESSAGE, Optional.of("+442037374299"), PhoneNumbersUtil.cleanPhoneNumber(UK_LAND_LINE_WITH_COUNTRY_CODE));
    }

    @Test
    public void cleanNumberForAnAmericanLandLine() {
        assertEquals(ASSERTION_EQUALS_MESSAGE, Optional.of("+15417543010"), PhoneNumbersUtil.cleanPhoneNumber(US_LAND_LINE_WITH_COUNTRY_CODE));
    }

    @Test
    public void cleanNumberForAnInvalidNumberReturnsEmptyOption() {
        assertEquals(ASSERTION_EQUALS_MESSAGE, Optional.empty(), PhoneNumbersUtil.cleanPhoneNumber("99a"));
    }
}
