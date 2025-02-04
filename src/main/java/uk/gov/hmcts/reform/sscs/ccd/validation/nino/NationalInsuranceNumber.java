package uk.gov.hmcts.reform.sscs.ccd.validation.nino;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = NationalInsuranceNumberValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NationalInsuranceNumber {

    String message() default "Invalid National Insurance number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}