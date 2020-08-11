package uk.gov.hmcts.reform.sscs.ccd.validation.localdate;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocalDateYearMustBeInPastValidator implements ConstraintValidator<LocalDateYearMustBeInPast, String> {

    @Override
    public boolean isValid(String strDate, ConstraintValidatorContext context) {
        if (!isBlank(strDate)) {
            int testYear = LocalDate.parse(strDate).getYear();
            int thisYear = LocalDate.now().getYear();
            return testYear < thisYear;
        }
        return true;
    }
}
