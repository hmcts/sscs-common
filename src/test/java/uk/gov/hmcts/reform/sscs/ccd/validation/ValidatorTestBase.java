package uk.gov.hmcts.reform.sscs.ccd.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;

public class ValidatorTestBase {

    protected Validator validator;

    @BeforeEach
    public void setUp() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    protected <B> void assertSingleViolationWithMessage(Set<ConstraintViolation<B>> violations, String expectedMessage) {
        assertEquals(1, violations.size());
        assertEquals(expectedMessage, violations.stream().map(ConstraintViolation::getMessage).findFirst().orElse(""));
    }
}
