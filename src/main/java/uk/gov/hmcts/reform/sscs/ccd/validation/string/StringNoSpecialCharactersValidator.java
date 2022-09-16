package uk.gov.hmcts.reform.sscs.ccd.validation.string;

import static org.apache.commons.lang3.StringUtils.isBlank;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StringNoSpecialCharactersValidator implements ConstraintValidator<StringNoSpecialCharacters, String> {

    public static String ALLOW_LIST = "";

    private String fieldName;
    private String message;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!isBlank(value) && isContainingSpecialCharacters(value)) {
            if (StringNoSpecialCharacters.DEFAULT_MESSAGE.equals(message)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(fieldName + " must not contain special characters").addConstraintViolation();
            }
            return false;
        }
        return true;
    }

    @Override
    public void initialize(StringNoSpecialCharacters constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
        this.message = constraintAnnotation.message();
    }

    private boolean isContainingSpecialCharacters(String value) {
        return !value.matches(ALLOW_LIST);
    }
}
