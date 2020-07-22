package uk.gov.hmcts.reform.sscs.ccd.validation.localdate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = LocalDateInFutureValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalDateInFuture {

    String message() default "LocalDate must be in the future!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
