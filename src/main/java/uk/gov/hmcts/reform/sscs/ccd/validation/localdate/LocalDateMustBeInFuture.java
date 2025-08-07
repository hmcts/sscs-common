package uk.gov.hmcts.reform.sscs.ccd.validation.localdate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LocalDateMustBeInFutureValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalDateMustBeInFuture {

    String message() default "LocalDate must be in the future!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}