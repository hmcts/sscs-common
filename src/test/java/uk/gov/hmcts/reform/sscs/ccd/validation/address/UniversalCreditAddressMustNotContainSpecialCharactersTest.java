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

public class UniversalCreditAddressMustNotContainSpecialCharactersTest extends ValidatorTestBase {

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
    public void testWhenAddressDoesNotContainSpecialCharacters_IsValid() {

        TestBean testBean = new TestBean();
        testBean.testAddress = Address.builder()
            .line1("some text")
            .line2("some text")
            .town("some text")
            .county("some text").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenAddressLine1ContainsSpecialCharacters_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testAddress = Address.builder()
            .line1("some @ text")
            .line2("some text")
            .town("some text")
            .county("some text").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Line 1 must not contain special characters");

    }

    @Test
    public void testWhenAddressLine2ContainsSpecialCharacters_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testAddress = Address.builder()
            .line1("some text")
            .line2("some @ text")
            .town("some text")
            .county("some text").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Line 2 must not contain special characters");

    }

    @Test
    public void testWhenAddressTownContainsSpecialCharacters_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testAddress = Address.builder()
            .line1("some text")
            .line2("some text")
            .town("some @ text")
            .county("some text").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Town must not contain special characters");

    }

    @Test
    public void testWhenAddressCountyContainsSpecialCharacters_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testAddress = Address.builder()
            .line1("some text")
            .line2("some text")
            .town("some text")
            .county("some @ text").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "County must not contain special characters");

    }
}
