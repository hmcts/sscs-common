package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.List.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
            "DLA, Disability Living Allowance (DLA)",
            "Carer's Allowance, Carer's Allowance",
            "Attendance Allowance, Attendance Allowance",
            "Bereavement Benefit, Bereavement Benefit",
            "Industrial Injuries Disablement Benefit, Industrial Injuries Disablement Benefit",
            "Maternity Allowance, Maternity Allowance",
            "Social Fund, Social Fund",
            "Income Support, Income Support"
    })
    public void givenABenefitCodeWithAcronym_thenBuildLongBenefitNameDescriptionWithAcronym(String benefitCode, String expected) {
        assertEquals(expected, Benefit.getLongBenefitNameDescriptionWithOptionalAcronym(benefitCode, true));
    }

    @Test
    @Parameters({
            "carersAllowance, Carer's Allowance",
            "attendanceAllowance, Attendance Allowance",
            "bereavementBenefit, Bereavement Benefit",
            "industrialInjuriesDisablement, Industrial Injuries Disablement Benefit",
            "maternityAllowance, Maternity Allowance",
            "socialFund, Social Fund",
            "incomeSupport, Income Support"
    })
    public void givenABenefitCodeWithNoAcronym_thenBuildLongBenefitNameDescriptionWithNoAcronym(String benefitCode, String expected) {
        assertEquals(expected, Benefit.getLongBenefitNameDescriptionWithOptionalAcronym(benefitCode, true));
    }

    @Test
    @Parameters(method = "welshBenefitScenarios")
    public void givenAWelshBenefitCodeWithAcronym_thenBuildLongBenefitNameDescriptionWithAcronym(String benefitCode, String expected) {
        assertEquals(expected, Benefit.getLongBenefitNameDescriptionWithOptionalAcronym(benefitCode, false));
    }

    @SuppressWarnings({"unused"})
    private Object[] welshBenefitScenarios() {

        return new Object[]{
            new Object[]{"PIP", "Taliad Annibyniaeth Personol (PIP)"},
            new Object[]{"ESA", "Lwfans Cyflogaeth a Chymorth (ESA)"},
            new Object[]{"UC", "Credyd Cynhwysol (UC)"},
            new Object[]{"JSA", "Lwfans Ceisio Gwaith (JSA)"},
            new Object[]{"DLA", "Lwfans Byw i’r Anabl (DLA)"},
            new Object[]{"Carer's Allowance", "Lwfans Gofalwr"},
            new Object[]{"Attendance Allowance", "Lwfans Gweini"},
            new Object[]{"Bereavement Benefit", "Budd-dal Profedigaeth"},
            new Object[]{"Industrial Injuries Disablement Benefit", "Budd-dal Anabledd Anafiadau Diwydiannol"},
            new Object[]{"Maternity Allowance", "Lwfans Mamolaeth"},
            new Object[]{"Social Fund", "Cronfa Gymdeithasol"},
            new Object[]{"Income Support", "Cymhorthdal Incwm"},
        };
    }

    @Test
    @Parameters({
            "carersAllowance, Lwfans Gofalwr",
            "attendanceAllowance, Lwfans Gweini",
            "bereavementBenefit, Budd-dal Profedigaeth",
            "industrialInjuriesDisablement, Budd-dal Anabledd Anafiadau Diwydiannol",
            "maternityAllowance, Lwfans Mamolaeth",
            "socialFund, Cronfa Gymdeithasol",
            "incomeSupport, Cymhorthdal Incwm"
    })
    public void givenAWelshBenefitCodeWithNoAcronym_thenBuildLongBenefitNameDescriptionWithNoAcronym(String benefitCode, String expected) {
        assertEquals(expected, Benefit.getLongBenefitNameDescriptionWithOptionalAcronym(benefitCode, false));
    }

    @Test
    @Parameters({"PIP, 002, 003", "ESA, 051",
            "ATTENDANCE_ALLOWANCE, 013",
            "UC, 001",
            "JSA, 073",
            "DLA, 037",
            "CARERS_ALLOWANCE, 070",
            "ESA, 051",
            "BEREAVEMENT_BENEFIT, 094",
            "MATERNITY_ALLOWANCE, 079",
            "SOCIAL_FUND, 088, 089, 061",
            "INCOME_SUPPORT, 061",
    })
    public void caseloaderKeyIds(Benefit benefit, String... caseloaderKeyIds) {
        assertThat(benefit.getCaseLoaderKeyId(), is(of(caseloaderKeyIds)));
    }

    @Test
    @Parameters({
            "PIP, JUDGE_DOCTOR_AND_DISABILITY_EXPERT",
            "ATTENDANCE_ALLOWANCE, JUDGE_DOCTOR_AND_DISABILITY_EXPERT",
            "UC, JUDGE_DOCTOR_AND_DISABILITY_EXPERT_IF_APPLICABLE",
            "JSA, JUDGE",
            "DLA, JUDGE_DOCTOR_AND_DISABILITY_EXPERT",
            "CARERS_ALLOWANCE, JUDGE",
            "BEREAVEMENT_BENEFIT, JUDGE",
            "ESA, JUDGE_AND_A_DOCTOR",
            "IIDB, JUDGE_AND_ONE_OR_TWO_DOCTORS",
            "MATERNITY_ALLOWANCE, JUDGE",
            "SOCIAL_FUND, JUDGE",
            "INCOME_SUPPORT, JUDGE"
    })
    public void panelComposition(Benefit benefit, PanelComposition expectedPanelComposition) {
        assertThat(benefit.getPanelComposition(), is(expectedPanelComposition));
    }
}
