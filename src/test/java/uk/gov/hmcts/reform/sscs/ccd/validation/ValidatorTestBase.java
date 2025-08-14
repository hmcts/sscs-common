package uk.gov.hmcts.reform.sscs.ccd.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertTrue(violations.size() == 1);
        assertEquals(expectedMessage, violations.stream().map(v -> v.getMessage()).findFirst().orElse(""));
    }
}
