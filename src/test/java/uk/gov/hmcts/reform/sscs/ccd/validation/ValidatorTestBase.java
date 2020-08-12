package uk.gov.hmcts.reform.sscs.ccd.validation;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.Assert;
import org.junit.Before;

public class ValidatorTestBase {

    protected Validator validator;

    @Before
    public void setUp() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    protected <B> void assertSingleViolationWithMessage(Set<ConstraintViolation<B>> violations, String expectedMessage) {
        Assert.assertTrue(violations.size() == 1);
        Assert.assertEquals(expectedMessage, violations.stream().map(v -> v.getMessage()).findFirst().orElse(""));
    }
}
