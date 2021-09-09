package uk.gov.hmcts.reform.sscs.service;

import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Optional;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.converters.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class CaseCodeServiceTest {

    @Test
    @Parameters({"PIP, 002", "ESA, 051", "UC, 001", "DLA, 037", "Carer's Allowance, 070"})
    public void givenABenefit_thenReturnTheCorrectBenefitCode(String benefit, String expected) {
        assertEquals(of(expected), CaseCodeService.generateBenefitCode(benefit, null));
    }

    @Test
    @Parameters({"St Helens Sure Start Maternity Grant, 088", "Funeral Payment Dispute Resolution Team, 089", "Pensions Dispute Resolution Team, 061"})
    public void givenSocialFundBenefit_thenReturnTheCorrectBenefitCodeBasedOnAddress(String address, String expected) {
        assertEquals(of(expected), CaseCodeService.generateBenefitCode("socialFund", address));
    }

    public void givenAnUnknownBenefit_thenReturnEmptyOptional() {
        String benefit = "random";
        final Optional<String> optional = CaseCodeService.generateBenefitCode(benefit, null);
        assertThat(optional.isEmpty(), is(true));
    }

    @Test
    public void generateIssueCode() {
        assertEquals("DD", CaseCodeService.generateIssueCode());
    }

    @Test
    @Parameters({"002, DD, 002DD", "null, DD, null", "001, null, null"})
    public void givenABenefitAndIssueCode_thenGenerateCaseCode(@Nullable String benefit, @Nullable String issueCode, @Nullable String expected) {
        assertEquals(expected, CaseCodeService.generateCaseCode(benefit, issueCode));
    }
}