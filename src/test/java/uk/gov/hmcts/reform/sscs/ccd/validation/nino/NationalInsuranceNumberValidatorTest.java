package uk.gov.hmcts.reform.sscs.ccd.validation.nino;

import java.util.Set;
import jakarta.validation.ConstraintViolation;
import org.junit.Assert;
import org.junit.Test;
import uk.gov.hmcts.reform.sscs.ccd.validation.ValidatorTestBase;

public class NationalInsuranceNumberValidatorTest extends ValidatorTestBase {

    private class TestBean {

        @NationalInsuranceNumber
        private String testNino;

        @NationalInsuranceNumber(message = "Some Custom Message")
        private String testNinoWithCustomMessage;
    }

    @Test
    public void testWhenNinoNotSet_IsValid() {

        TestBean testBean = new TestBean();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenNinoInvalidFormat_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testNino = "blah";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        this.assertSingleViolationWithMessage(violations, "Invalid National Insurance number");
    }

    @Test
    public void testWhenNinoInvalidFormat_WithCustomMessage_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testNinoWithCustomMessage = "blah";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        this.assertSingleViolationWithMessage(violations, "Some Custom Message");
    }

    @Test
    public void testWhenNinoValidFormatNoSpaces_IsValid() {

        TestBean testBean = new TestBean();
        testBean.testNino = "BB000000B";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenNinoValidFormatWithSpaces_IsValid() {

        TestBean testBean = new TestBean();
        testBean.testNino = "BB 00 00 00 B";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }
}
