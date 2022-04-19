package uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata;

import java.time.LocalDate;
import java.util.Set;
import javax.validation.ConstraintViolation;
import org.junit.Assert;
import org.junit.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.*;
import uk.gov.hmcts.reform.sscs.ccd.validation.ValidatorTestBase;

public class JointPartyIdentityAndAppellantIdentityValidationTest extends ValidatorTestBase {

    @Test
    public void testWhenNoIdentitiesSet_IsValid() {

        SscsCaseData testBean = SscsCaseData.builder().build();
        Set<ConstraintViolation<SscsCaseData>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenJointPartyHasEmptyIdentity_IsValid() {

        Identity jointPartyIdentity = Identity.builder().build();
        SscsCaseData testBean = SscsCaseData.builder().jointParty(JointParty.builder().identity(jointPartyIdentity).build()).build();
        Set<ConstraintViolation<SscsCaseData>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenJointPartyHasIdentityWithWithValidNinoAndDob_IsValid() {

        Identity jointPartyIdentity = Identity.builder().nino("BB000000B").dob(LocalDate.now().minusYears(2).toString()).build();
        SscsCaseData testBean = SscsCaseData.builder().jointParty(JointParty.builder().identity(jointPartyIdentity).build()).build();
        Set<ConstraintViolation<SscsCaseData>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    /**
     * Ensure that validation fails if the Joint Party Address contains special characters.
     */
    @Test
    public void testWhenJointPartyHasIdentityWithInvalidNino_IsInValid() {

        Identity jointPartyIdentity = Identity.builder().nino("blah").build();
        SscsCaseData testBean = SscsCaseData.builder().jointParty(JointParty.builder().identity(jointPartyIdentity).build()).build();
        Set<ConstraintViolation<SscsCaseData>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Invalid National Insurance number");
    }

    /**
     * Ensure that validation fails if the Joint Party postcode is invalid.
     */
    @Test
    public void testWhenJointPartyHasIdentityWithInvalidDob_IsInValid() {

        Identity jointPartyIdentity = Identity.builder().dob(LocalDate.now().toString()).build();
        SscsCaseData testBean = SscsCaseData.builder().jointParty(JointParty.builder().identity(jointPartyIdentity).build()).build();
        Set<ConstraintViolation<SscsCaseData>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "You’ve entered an invalid date of birth");
    }

    /**
     * Ensure that validation does not fail if the Appellant dob is invalid
     * - we do not to introduce possibility of affecting existing SscsCaseData Identity attributes
     * with the nested Identity validator annotations.
     */
    @Test
    public void testWhenAppellantHasIdentityWithInvalidDob_IsValid() {

        Identity identity = Identity.builder().dob(LocalDate.now().toString()).build();
        Appellant appellant = Appellant.builder().identity(identity).build();
        Appeal appeal = Appeal.builder().appellant(appellant).build();

        SscsCaseData testBean = SscsCaseData.builder().appeal(appeal)
            .build();

        Set<ConstraintViolation<SscsCaseData>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

    /**
     * Ensure that validation does not fail if the Appellant nino is invalid
     * - we do not to introduce possibility of affecting existing SscsCaseData Identity attributes
     * with the nested Identity validator annotations.
     */
    @Test
    public void testWhenAppellantHasIdentityWithInvalidInvalidNinp_IsValid() {

        Identity identity = Identity.builder().nino("blah").build();
        Appellant appellant = Appellant.builder().identity(identity).build();
        Appeal appeal = Appeal.builder().appellant(appellant).build();

        SscsCaseData testBean = SscsCaseData.builder().appeal(appeal)
            .build();

        Set<ConstraintViolation<SscsCaseData>> violations = validator.validate(testBean);
        Assert.assertTrue(violations.isEmpty());
    }

}
