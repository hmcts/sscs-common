package uk.gov.hmcts.reform.sscs.ccd.validation.address;

import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import org.junit.Assert;
import org.junit.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.validation.ValidatorTestBase;
import uk.gov.hmcts.reform.sscs.ccd.validation.groups.UniversalCreditValidationGroup;

public class UniversalCreditAddressPostcodeMustBeValidTest extends ValidatorTestBase {

    private class TestBean {

        @Valid
        @ConvertGroup(to = UniversalCreditValidationGroup.class)
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
    public void testWhenAddressHasInvalidPostcode_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testAddress = Address.builder()
            .postcode("something").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Please enter a valid postcode");

    }

}
