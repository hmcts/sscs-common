package uk.gov.hmcts.reform.sscs.ccd.validation.address;

import static org.apache.commons.lang3.StringUtils.isBlank;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PostcodeValidator implements ConstraintValidator<Postcode, String> {

    public static String POSTCODE_REGEX = "^((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y]"
        + "[0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9]?[A-Za-z])|([Gg][Ii][Rr]))))\\s?([0-9][A-Za-z]{2})|(0[Aa]{2}))$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!isBlank(value) && !isPostcodeFormat(value)) {
            return false;
        }
        return true;
    }

    private boolean isPostcodeFormat(String value) {
        return value.matches(POSTCODE_REGEX);
    }
}
