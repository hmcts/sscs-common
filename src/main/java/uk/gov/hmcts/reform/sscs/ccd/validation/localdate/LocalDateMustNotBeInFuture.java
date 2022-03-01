package uk.gov.hmcts.reform.sscs.ccd.validation.localdate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = LocalDateMustNotBeInFutureValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalDateMustNotBeInFuture {

    String message() default "LocalDate must not be in the future!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}