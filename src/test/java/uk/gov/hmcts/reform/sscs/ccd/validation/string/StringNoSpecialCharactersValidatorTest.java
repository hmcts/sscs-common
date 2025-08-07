package uk.gov.hmcts.reform.sscs.ccd.validation.string;

import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import java.util.Set;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sscs.ccd.validation.ValidatorTestBase;

/**
 * This test makes assertions in line with the regex configured for no special characters validation.
 * There are currently inconsistencies in this validation - eg. £ is allowed but $ is not
 * - these inconsistencies are covered by the below tests.
 */
public class StringNoSpecialCharactersValidatorTest extends ValidatorTestBase {

    private class TestBean {

        @StringNoSpecialCharacters(fieldName = "My field")
        private String testString;

        @StringNoSpecialCharacters(fieldName = "My field", message = "Overridden Message")
        private String testStringWithCustomMessage;
    }

    @Test
    public void testWhenStringNotSet_IsValid() {

        TestBean testBean = new TestBean();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenStringDoesNotContainSpecialCharacters_IsValid() {

        TestBean testBean = new TestBean();
        testBean.testString = "some text";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenStringContainsAtSign_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testString = "some @ text";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "My field must not contain special characters");
    }

    @Test
    public void testWhenStringContainsAtSignWithCustomMessage_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testStringWithCustomMessage = "some @ text";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Overridden Message");
    }



    @Test
    public void testWhenStringContainsShriek_IsValid() {

        TestBean testBean = new TestBean();
        testBean.testString = "some ! text";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenStringContainsPound_IsValid() {

        TestBean testBean = new TestBean();
        testBean.testString = "some £ text";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenStringContainsDollar_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testString = "some $ text";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "My field must not contain special characters");
    }

    @Test
    public void testWhenStringContainsPercent_Valid() {

        TestBean testBean = new TestBean();
        testBean.testString = "some % text";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenStringContainsHat_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testString = "some ^ text";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "My field must not contain special characters");
    }

    @Test
    public void testWhenStringContainsAmpersand_IsValid() {

        TestBean testBean = new TestBean();
        testBean.testString = "some & text";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenStringContainsStar_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testString = "some * text";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "My field must not contain special characters");
    }

    @Test
    public void testWhenStringContainsLessThan_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testString = "some < text";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "My field must not contain special characters");
    }

    @Test
    public void testWhenStringContainsGreaterThan_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testString = "some > text";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "My field must not contain special characters");
    }

    @Test
    public void testWhenStringContainsForwardSlash_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testString = "some > text";

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "My field must not contain special characters");
    }
}
