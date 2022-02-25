package uk.gov.hmcts.reform.sscs.ccd.validation.localdate;

import java.time.LocalDate;
import java.util.Set;
import javax.validation.ConstraintViolation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.sscs.ccd.validation.ValidatorTestBase;

@RunWith(MockitoJUnitRunner.class)
public class LocalDateYearMustBeInPastValidatorTest extends ValidatorTestBase {

    private class TestBean {
        @LocalDateYearMustBeInPast
        private String testDate;

        @LocalDateYearMustBeInPast(message = "Some Custom Message")
        private String testDateWithCustomMessage;
    }

    @Test
    public void testWhenLocalDateStringNotSet_IsValid() {

        TestBean testBean = new TestBean();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenLocalDateStringForYearAsLastYear_WithDefaultMessage_IsValid() {

        TestBean testBean = new TestBean();
        testBean.testDate = LocalDate.now().plusYears(-1).toString();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenLocalDateStringForYearAsThisYear_WithDefaultMessage_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testDate = LocalDate.now().toString();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "LocalDate year must be in the past!");
    }

    @Test
    public void testWhenLocalDateStringForYearInFuture_WithDefaultMessage_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testDate = LocalDate.now().plusYears(1).toString();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "LocalDate year must be in the past!");
    }

    @Test
    public void testWhenLocalDateStringForYearInFuture_WithCustomMessage_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testDateWithCustomMessage = LocalDate.now().plusYears(1).toString();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Some Custom Message");
    }
}
