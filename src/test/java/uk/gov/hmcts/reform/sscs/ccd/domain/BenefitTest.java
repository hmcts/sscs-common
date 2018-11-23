package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class BenefitTest {

    @Test
    public void givenAValidBenefitString_thenReturnTrue() {
        assertTrue(Benefit.isBenefitTypeValid("PIP"));
    }

    @Test
    public void givenAnInvalidBenefitString_thenReturnTrue() {
        assertFalse(Benefit.isBenefitTypeValid("BLA"));
    }
}