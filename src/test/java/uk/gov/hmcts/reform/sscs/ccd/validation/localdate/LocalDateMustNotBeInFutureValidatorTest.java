package uk.gov.hmcts.reform.sscs.ccd.validation.localdate;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sscs.ccd.validation.ValidatorTestBase;

public class LocalDateMustNotBeInFutureValidatorTest extends ValidatorTestBase {

    private class TestBean {

        @LocalDateMustNotBeInFuture
        private String testDate;

        @LocalDateMustNotBeInFuture(message = "Some Custom Message")
        private String testDateWithCustomMessage;
    }

    @Test
    public void testWhenLocalDateStringNotSet_IsValid() {

        TestBean testBean = new TestBean();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenLocalDateStringIsInPast_IsValid() {

        TestBean testBean = new TestBean();
        testBean.testDate = LocalDate.now().plusDays(-1).toString();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenLocalDateStringIsToday_IsValid() {

        TestBean testBean = new TestBean();
        testBean.testDate = LocalDate.now().toString();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenLocalDateStringIsInFuture_WithDefaultMessage_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testDate = LocalDate.now().plusDays(1).toString();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "LocalDate must not be in the future!");
    }

    @Test
    public void testWhenLocalDateStringIsInFuture_WithCustomMessage_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testDateWithCustomMessage = LocalDate.now().plusDays(1).toString();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Some Custom Message");
    }
}
