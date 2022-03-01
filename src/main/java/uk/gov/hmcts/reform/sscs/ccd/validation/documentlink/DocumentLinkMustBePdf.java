package uk.gov.hmcts.reform.sscs.ccd.validation.documentlink;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = DocumentLinkMustBePdfValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DocumentLinkMustBePdf {

    String message() default "Document link must be a PDF!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}