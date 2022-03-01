package uk.gov.hmcts.reform.sscs.ccd.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;

public class ValidatorTestBase {

    protected Validator validator;

    @Before
    public void setUp() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    protected <B> void assertSingleViolationWithMessage(Set<ConstraintViolation<B>> violations, String expectedMessage) {
        Assert.assertEquals(violations.size(), 1);
        Assert.assertEquals(expectedMessage, violations.stream().map(v -> v.getMessage()).findFirst().orElse(""));
    }
}
