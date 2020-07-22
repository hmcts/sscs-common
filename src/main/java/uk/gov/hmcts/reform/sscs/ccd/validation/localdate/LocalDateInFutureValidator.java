package uk.gov.hmcts.reform.sscs.ccd.validation.localdate;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocalDateInFutureValidator implements ConstraintValidator<LocalDateInFuture, String> {

    @Override
    public boolean isValid(String strDate, ConstraintValidatorContext context) {
        boolean isInvalid;
        if (!isBlank(strDate)) {
            LocalDate decisionNoticeDecisionDate = LocalDate.parse(strDate);
            LocalDate today = LocalDate.now();
            return !decisionNoticeDecisionDate.isAfter(today);
        }
        return true;
    }
}