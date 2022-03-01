package uk.gov.hmcts.reform.sscs.ccd.validation.address;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PostcodeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Postcode {

    String message() default "Please enter a valid postcode";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}