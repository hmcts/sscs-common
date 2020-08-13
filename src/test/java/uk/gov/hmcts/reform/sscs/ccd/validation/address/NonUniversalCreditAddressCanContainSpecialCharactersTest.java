package uk.gov.hmcts.reform.sscs.ccd.validation.address;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import org.junit.Assert;
import org.junit.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.validation.ValidatorTestBase;

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
