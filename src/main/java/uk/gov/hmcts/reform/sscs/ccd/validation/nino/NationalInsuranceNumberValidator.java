package uk.gov.hmcts.reform.sscs.ccd.validation.nino;

import static org.apache.commons.lang3.StringUtils.isBlank;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NationalInsuranceNumberValidator implements ConstraintValidator<NationalInsuranceNumber, String> {

    private static final String NINO_REGEX = "^(?!BG)(?!GB)(?!NK)(?!KN)(?!TN)(?!NT)(?!ZZ)\\s?(?:[A-CEGHJ-PR-TW-Z]\\s?[A-CEGHJ-NPR-TW-Z])\\s?(?:\\d\\s?){6}([A-D]|\\s)\\s?$";

    @Override
    public boolean isValid(String nino, ConstraintValidatorContext context) {
        if (!isBlank(nino)) {
            return isNinoValid(nino);
        }
        return true;
    }

    private boolean isNinoValid(String nino) {
        return nino.matches(NINO_REGEX);
    }
}
