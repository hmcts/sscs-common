package uk.gov.hmcts.reform.sscs.ccd.validation.identity;

import java.time.LocalDate;
import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import org.junit.Assert;
import org.junit.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.Identity;
import uk.gov.hmcts.reform.sscs.ccd.validation.ValidatorTestBase;
import uk.gov.hmcts.reform.sscs.ccd.validation.groups.UniversalCreditValidationGroup;

public class UniversalCreditIdentityMustBeValidTest extends ValidatorTestBase {

    private class TestBean {

        @Valid
        @ConvertGroup(to = UniversalCreditValidationGroup.class)
        private Identity identity;

    }

    @Test
    public void testWhenIdentityNotSet_IsValid() {

        TestBean testBean = new TestBean();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenIdentityHasValidNino_IsValid() {

        TestBean testBean = new TestBean();
        testBean.identity = Identity.builder()
            .nino("BB000000B").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenIdentityHasInvalidNino_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.identity = Identity.builder()
            .nino("something").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Invalid National Insurance number");
    }

    @Test
    public void testWhenIdentityHasValidDob_IsValid() {

        TestBean testBean = new TestBean();
        testBean.identity = Identity.builder()
            .dob(LocalDate.now().minusYears(2).toString()).build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenIdentityHasInvalidDob_IsValid() {

        TestBean testBean = new TestBean();
        testBean.identity = Identity.builder()
            .dob(LocalDate.now().toString()).build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "You’ve entered an invalid date of birth");
    }

}
