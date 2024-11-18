package uk.gov.hmcts.reform.sscs.ccd.validation.address;

import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import org.junit.Assert;
import org.junit.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.validation.ValidatorTestBase;

/**
 * This test is to ensure that we do not use the special character validation on non-UC
 * use-cases of Address, for example the address of the appellant.
 * We are introducing new validation for UC that at this time we do not
 * wish to apply to other scenarios.  This validation is the previous SYA
 * front end validation ported to Java,  and while the validation rules should
 * in theory be the same across usages of Address, this would be a separate task to standardise
 * in future.  For now we want to ensure that we not affect existing use-cases
 */
public class NonUniversalCreditAddressCanContainSpecialCharactersTest extends ValidatorTestBase {

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
    public void testWhenAddressDoesNotContainSpecialCharacters_IsValid() {

        TestBean testBean = new TestBean();
        testBean.testAddress = Address.builder()
            .line1("some text")
            .line2("some text")
            .town("some text")
            .county("some text")
            .postcode("some text").build();


        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenAddressLine1ContainsSpecialCharacters_IsValid() {

        TestBean testBean = new TestBean();
        testBean.testAddress = Address.builder()
            .line1("some @ text")
            .line2("some text")
            .town("some text")
            .county("some text")
            .postcode("some text").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());

    }

    @Test
    public void testWhenAddressLine2ContainsSpecialCharacters_IsValid() {

        TestBean testBean = new TestBean();
        testBean.testAddress = Address.builder()
            .line1("some text")
            .line2("some @ text")
            .town("some text")
            .county("some text")
            .postcode("some text").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());

    }

    @Test
    public void testWhenAddressTownContainsSpecialCharacters_IsValid() {

        TestBean testBean = new TestBean();
        testBean.testAddress = Address.builder()
            .line1("some text")
            .line2("some text")
            .town("some @ text")
            .county("some text")
            .postcode("some text").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());

    }

    @Test
    public void testWhenAddressCountyContainsSpecialCharacters_IsValid() {

        TestBean testBean = new TestBean();
        testBean.testAddress = Address.builder()
            .line1("some text")
            .line2("some text")
            .town("some text")
            .county("some @ text")
            .postcode("some text").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());

    }

    @Test
    public void testWhenAddressPostcodeContainsSpecialCharacters_IsValid() {

        TestBean testBean = new TestBean();
        testBean.testAddress = Address.builder()
            .line1("some text")
            .line2("some text")
            .town("some text")
            .county("some text")
            .postcode("some @ text").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());

    }



}
