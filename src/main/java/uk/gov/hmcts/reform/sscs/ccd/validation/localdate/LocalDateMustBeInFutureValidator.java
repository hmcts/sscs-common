package uk.gov.hmcts.reform.sscs.ccd.validation.localdate;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.time.LocalDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LocalDateMustBeInFutureValidator implements ConstraintValidator<LocalDateMustBeInFuture, String> {

    @Override
    public boolean isValid(String strDate, ConstraintValidatorContext context) {
        if (!isBlank(strDate)) {
            LocalDate testDate = LocalDate.parse(strDate);
            LocalDate today = LocalDate.now();
            return testDate.isAfter(today);
        }
        return true;
    }
}
