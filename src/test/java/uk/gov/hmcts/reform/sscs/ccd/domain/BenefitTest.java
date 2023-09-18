package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.List.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.gov.hmcts.reform.sscs.exception.BenefitMappingException;

@RunWith(JUnitParamsRunner.class)
public class BenefitTest {

    @Test
    @Parameters({
        "ESA, ESA",
        "JSA, JSA",
        "Employment and Support Allowance, ESA",
        "Jobseeker’s Allowance, JSA"
    })
    public void getBenefitOptionalByCodeReturnsTheBenefit(String code, Benefit expectedBenefit) {
        assertThat(Benefit.getBenefitOptionalByCode(code), is(Optional.of(expectedBenefit)));
    }

    @Test
    @Parameters({
        "051, ESA",
        "073, JSA",
        "002, PIP",
        "045, PENSION_CREDIT"
    })
    public void test(String benefitCode, Benefit expectedBenefit) {
        assertThat(Benefit.findBenefitOptionalByBenefitCode(benefitCode), is(Optional.of(expectedBenefit)));
    }

    public void getBenefitOptionalByCodeReturnsEmptyIfInvalidBenefit() {
        assertThat(Benefit.getBenefitOptionalByCode("invalid"), is(Optional.empty()));

    }

    @Test(expected = BenefitMappingException.class)
    public void getBenefitByCodeOrThrowExceptionThrowsExceptionForInvalidBenefit() {
        Benefit.getBenefitByCodeOrThrowException("invalid");
    }

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
        "JSA, Jobseeker’s Allowance (JSA)",
        "DLA, Disability Living Allowance (DLA)"
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
        "incomeSupport, Income Support",
        "bereavementSupportPaymentScheme, Bereavement Support Payment Scheme",
        "industrialDeathBenefit, Industrial Death Benefit",
        "pensionCredit, Pension Credit",
        "retirementPension, Retirement Pension",
        "taxCredit, Tax Credit",
        "guardiansAllowance, Guardians Allowance",
        "taxFreeChildcare, Tax-Free Childcare",
        "homeResponsibilitiesProtection, Home Responsibilities Protection",
        "childBenefit, Child Benefit",
        "thirtyHoursFreeChildcare, 30 Hours Free Childcare",
        "guaranteedMinimumPension, Guaranteed Minimum Pension (COEG)",
        "nationalInsuranceCredits, National Insurance Credits",
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
            new Object[]{"Income Support", "Cymhorthdal Incwm"}
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
        "incomeSupport, Cymhorthdal Incwm",
        "Bereavement Support Payment Scheme, Cynllun Taliad Cymorth Profedigaeth",
        "Industrial Death Benefit, Budd Marwolaeth Ddiwydiannol",
        "Pension Credit, Credydau Pensiwn",
        "Tax Credit, Credyd Treth",
        "Guardians Allowance, Lwfans Gwarcheidwad",
        "Tax-Free Childcare, Gofal Plant Di-dreth",
        "Home Responsibilities Protection, Diogelu Cyfrifoldebau Cartref",
        "Child Benefit, Budd-dal Plant",
        "30 Hours Free Childcare, Gofal Plant am ddim - 30 awr",
        "Guaranteed Minimum Pension (COEG), Isafswm Pensiwn Gwarantedig",
        "National Insurance Credits, Credydau Yswiriant Gwladol"
    })
    public void givenAWelshBenefitCodeWithNoAcronym_thenBuildLongBenefitNameDescriptionWithNoAcronym(String benefitCode, String expected) {
        assertEquals(expected, Benefit.getLongBenefitNameDescriptionWithOptionalAcronym(benefitCode, false));
    }

    @Test
    @Parameters({
        "PIP, 002, 003", "ESA, 051",
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
        "BEREAVEMENT_SUPPORT_PAYMENT_SCHEME, 095",
        "INDUSTRIAL_DEATH_BENEFIT, 064",
        "PENSION_CREDIT, 045",
        "RETIREMENT_PENSION, 082",
        "TAX_CREDIT, 053, 054, 055",
        "GUARDIANS_ALLOWANCE, 015",
        "TAX_FREE_CHILDCARE, 057",
        "HOME_RESPONSIBILITIES_PROTECTION, 050",
        "CHILD_BENEFIT, 016",
        "THIRTY_HOURS_FREE_CHILDCARE, 058",
        "GUARANTEED_MINIMUM_PENSION, 034",
        "NATIONAL_INSURANCE_CREDITS, 030",
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
        "INCOME_SUPPORT, JUDGE",
        "BEREAVEMENT_SUPPORT_PAYMENT_SCHEME, JUDGE",
        "INDUSTRIAL_DEATH_BENEFIT, JUDGE_AND_ONE_OR_TWO_DOCTORS",
        "PENSION_CREDIT, JUDGE",
        "RETIREMENT_PENSION, JUDGE",
        "CHILD_SUPPORT, JUDGE_AND_FINANCIALLY_QUALIFIED_PANEL_MEMBER",
        "TAX_CREDIT, JUDGE_AND_FINANCIALLY_QUALIFIED_PANEL_MEMBER",
        "GUARDIANS_ALLOWANCE, JUDGE_AND_FINANCIALLY_QUALIFIED_PANEL_MEMBER",
        "TAX_FREE_CHILDCARE, JUDGE_AND_FINANCIALLY_QUALIFIED_PANEL_MEMBER",
        "HOME_RESPONSIBILITIES_PROTECTION, JUDGE_AND_FINANCIALLY_QUALIFIED_PANEL_MEMBER",
        "CHILD_BENEFIT, JUDGE_AND_FINANCIALLY_QUALIFIED_PANEL_MEMBER",
        "THIRTY_HOURS_FREE_CHILDCARE, JUDGE_AND_FINANCIALLY_QUALIFIED_PANEL_MEMBER",
        "GUARANTEED_MINIMUM_PENSION, JUDGE_AND_FINANCIALLY_QUALIFIED_PANEL_MEMBER",
        "NATIONAL_INSURANCE_CREDITS, JUDGE_AND_FINANCIALLY_QUALIFIED_PANEL_MEMBER"
    })
    public void panelComposition(Benefit benefit, PanelComposition expectedPanelComposition) {
        assertThat(benefit.getPanelComposition(), is(expectedPanelComposition));
    }
}
