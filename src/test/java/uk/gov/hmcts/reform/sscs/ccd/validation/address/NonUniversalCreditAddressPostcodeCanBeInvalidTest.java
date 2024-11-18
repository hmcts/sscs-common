package uk.gov.hmcts.reform.sscs.ccd.validation.address;

import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import org.junit.Assert;
import org.junit.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.validation.ValidatorTestBase;

/**
 * This test is to ensure that we do not use the Postcode validation on non-UC
 * use-cases of Address, for example the address of the appellant.
 * We are introducing new validation for UC that at this time we do not
 * wish to apply to other scenarios.  This validation is the previous SYA
 * front end validation ported to Java,  and while the validation rules should
 * in theory be the same across usages of Address, this would be a separate task to standardise
 * in future.  For now we want to ensure that we not affect existing use-cases
 */
public class NonUniversalCreditAddressPostcodeCanBeInvalidTest extends ValidatorTestBase {

    private class TestBean {

        @Valid
        private Address testAddress;

    }

    @Test
    public void testWhenAddressNotSet_IsValid() {

        TestBean testBean = new TestBean();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenAddressHasValidPostcode_IsValid() {

        TestBean testBean = new TestBean();
        testBean.testAddress = Address.builder()
            .postcode("sw1p 4df").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenAddressHasInvalidPostcode_IsValid() {

        TestBean testBean = new TestBean();
        testBean.testAddress = Address.builder()
            .postcode("something").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());

    }

}
