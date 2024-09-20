package uk.gov.hmcts.reform.sscs.ccd.validation.appeal;

import java.util.Set;
import javax.validation.ConstraintViolation;
import org.junit.Assert;
import org.junit.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.*;
import uk.gov.hmcts.reform.sscs.ccd.validation.ValidatorTestBase;

public class JointPartyAddressAndAppellantAddressValidationTest extends ValidatorTestBase {

    @Test
    public void testWhenNoAddressesSet_IsValid() {

        SscsCaseData testBean = SscsCaseData.builder().build();
        Set<ConstraintViolation<SscsCaseData>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenJointPartyHasEmptyAddress_IsValid() {

        Address jointPartyAddress = Address.builder().build();
        SscsCaseData testBean = SscsCaseData.builder().jointParty(JointParty.builder().address(jointPartyAddress).build()).build();
        Set<ConstraintViolation<SscsCaseData>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenJointPartyHasAddressWithLine1WithNoSpecialCharacters_IsValid() {

        Address jointPartyAddress = Address.builder().line1("some text").build();
        SscsCaseData testBean = SscsCaseData.builder().jointParty(JointParty.builder().address(jointPartyAddress).build()).build();
        Set<ConstraintViolation<SscsCaseData>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    /**
     * Ensure that validation fails if the Joint Party Address contains special characters.
     */
    @Test
    public void testWhenJointPartyHasAddressWithLine1WithSpecialCharacters_IsInValid() {

        Address jointPartyAddress = Address.builder().line1("some @text").build();
        SscsCaseData testBean = SscsCaseData.builder().jointParty(JointParty.builder().address(jointPartyAddress).build()).build();
        Set<ConstraintViolation<SscsCaseData>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Line 1 must not contain special characters");
    }

    /**
     * Ensure that validation fails if the Joint Party postcode is invalid.
     */
    @Test
    public void testWhenJointPartyHasInvalidPostcode_IsInValid() {

        Address jointPartyAddress = Address.builder().postcode("some text").build();
        SscsCaseData testBean = SscsCaseData.builder().jointParty(JointParty.builder().address(jointPartyAddress).build()).build();
        Set<ConstraintViolation<SscsCaseData>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Please enter a valid postcode");
    }

    /**
     * Ensure that validation does not fail if the Appellant address contains special characters
     * - we do not to introduce possibility of affecting existing SscsCaseData Address attributes
     * with the nested Address validator annotations.
     */
    @Test
    public void testWhenAppellantHasAddressWithLine1WithSpecialCharacters_IsValid() {

        Address appellantAddress = Address.builder().line1("some @text").build();
        Appellant appellant = Appellant.builder().address(appellantAddress).build();
        Appeal appeal = Appeal.builder().appellant(appellant).build();

        SscsCaseData testBean = SscsCaseData.builder().appeal(appeal)
            .build();

        Set<ConstraintViolation<SscsCaseData>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    /**
     * Ensure that validation does not fail if the Appellant postcode is invalid
     * - we do not to introduce possibility of affecting existing SscsCaseData Address attributes
     * with the nested Address validator annotations.
     */
    @Test
    public void testWhenAppellantHasInvalidPostcode_IsValid() {

        Address appellantAddress = Address.builder().postcode("invalid postcode").build();
        Appellant appellant = Appellant.builder().address(appellantAddress).build();
        Appeal appeal = Appeal.builder().appellant(appellant).build();

        SscsCaseData testBean = SscsCaseData.builder().appeal(appeal)
            .build();

        Set<ConstraintViolation<SscsCaseData>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

}
