package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.*;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class BenefitTest {

    @Test
    public void givenAValidBenefitString_thenReturnTrue() {
        assertTrue(Benefit.isBenefitTypeValid("PIP"));
    }

    @Test
    public void givenAnInvalidBenefitString_thenReturnTrue() {
        assertFalse(Benefit.isBenefitTypeValid("BLA"));
    }

    @Test
    @Parameters({
            "PIP, Personal Independence Payment (PIP)",
            "ESA, Employment and Support Allowance (ESA)",
            "UC, Universal Credit (UC)",
            "JSA, Job Seekers Allowance (JSA)",
            "DLA, Disability Living Allowance (DLA)"
    })
    public void givenABenefitCodeWithAcronym_thenBuildLongBenefitNameDescriptionWithAcronym(String benefitCode, String expected) {
        assertEquals(expected, Benefit.getLongBenefitNameDescriptionWithOptionalAcronym(benefitCode, true));
    }

    @Test
    public void givenABenefitCodeWithNoAcronym_thenBuildLongBenefitNameDescriptionWithNoAcronym() {
        assertEquals("Carer's Allowance", Benefit.getLongBenefitNameDescriptionWithOptionalAcronym("carersAllowance", true));
    }

    @Test
    @Parameters(method = "welshBenefitScenarios")
    public void givenAWelshBenefitCodeWithAcronym_thenBuildLongBenefitNameDescriptionWithAcronym(String benefitCode, String expected) {
        assertEquals(expected, Benefit.getLongBenefitNameDescriptionWithOptionalAcronym(benefitCode, false));
    }

    private Object[] welshBenefitScenarios() {

        return new Object[]{
            new Object[]{"PIP", "Taliad Annibyniaeth Personol (PIP)"},
            new Object[]{"ESA", "Lwfans Cyflogaeth a Chymorth (ESA)"},
            new Object[]{"UC", "Credyd Cynhwysol (UC)"},
            new Object[]{"JSA", "Job Seekers Allowance (JSA)"},
            new Object[]{"DLA", "Lwfans Byw i’r Anabl (DLA)"}
        };
    }

    @Test
    public void givenAWelshBenefitCodeWithNoAcronym_thenBuildLongBenefitNameDescriptionWithNoAcronym() {
        assertEquals("Lwfans Gofalwr", Benefit.getLongBenefitNameDescriptionWithOptionalAcronym("carersAllowance", false));
    }
}