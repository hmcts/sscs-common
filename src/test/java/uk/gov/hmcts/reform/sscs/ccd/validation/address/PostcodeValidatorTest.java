package uk.gov.hmcts.reform.sscs.ccd.validation.address;

import java.util.Set;
import jakarta.validation.ConstraintViolation;
import org.junit.Assert;
import org.junit.Test;
import uk.gov.hmcts.reform.sscs.ccd.validation.ValidatorTestBase;

public class PostcodeValidatorTest extends ValidatorTestBase {

    private class TestBean {

        @Postcode
        private String postcode;

        @Postcode
        private String postcodeWithCustomMessage;
    }

    @Test
    public void testWhenPostcodeNotSet_IsValid() {

        TestBean testBean = new TestBean();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenValidPostcodeInLowercase_IsValid() {

        TestBean testBean = new TestBean();
        testBean.postcode = "sw1p 4df";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenValidPostcodeInUppercase_IsValid() {

        TestBean testBean = new TestBean();
        testBean.postcode = "SW1P 4df";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenValidPostcodeWithoutSpaces_IsValid() {

        TestBean testBean = new TestBean();
        testBean.postcode = "WV112HE";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenValidPostcodeWithSpaces_IsValid() {

        TestBean testBean = new TestBean();
        testBean.postcode = "WV11 2HE";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenValidLargeUserPostcodeInUpperCase_IsValid() {

        TestBean testBean = new TestBean();
        testBean.postcode = "GIR 0AA";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenValidLargeUserPostcodeInLowerCase_IsValid() {

        TestBean testBean = new TestBean();
        testBean.postcode = "gir 0aa";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenInvalidPostcodeInLowerCase_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.postcode = "invalid postcode";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Please enter a valid postcode");
    }

    @Test
    public void testWhenInvalidPostcodeInLowerCaseWithCustomMessage_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.postcodeWithCustomMessage = "invalid postcode";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Please enter a valid postcode");
    }

    @Test
    public void testWhenValidPostcodeWithSpecialCharacterAtBeginning_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.postcode = "@Wv11 2HE";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Please enter a valid postcode");
    }


}
