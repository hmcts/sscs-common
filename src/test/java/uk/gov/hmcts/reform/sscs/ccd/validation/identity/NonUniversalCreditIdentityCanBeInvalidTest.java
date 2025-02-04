package uk.gov.hmcts.reform.sscs.ccd.validation.identity;

import java.time.LocalDate;
import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import org.junit.Assert;
import org.junit.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.Identity;
import uk.gov.hmcts.reform.sscs.ccd.validation.ValidatorTestBase;

/**
 * This test is to ensure that we do not use the Nino/DOB validation on non-UC
 * use-cases of Identity, for example the Identity of the appellant.
 * We are introducing new validation for UC that at this time we do not
 * wish to apply to other scenarios.  This validation is the previous SYA
 * front end validation ported to Java,  and while the validation rules should
 * in theory be the same across usages of Identity, this would be a separate task to standardise
 * in future.  For now we want to ensure that we not affect existing use-cases
 */
public class NonUniversalCreditIdentityCanBeInvalidTest extends ValidatorTestBase {

    private class TestBean {

        @Valid
        private Identity identity;

    }

    @Test
    public void testWhenIdentityNotSet_IsValid() {

        TestBean testBean = new TestBean();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenIdentityHasInvalidNino_IsValid() {

        TestBean testBean = new TestBean();
        testBean.identity = Identity.builder()
            .nino("something").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenIdentityHasInvalidDob_IsValid() {

        TestBean testBean = new TestBean();
        testBean.identity = Identity.builder()
            .dob(LocalDate.now().toString()).build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

}
