package uk.gov.hmcts.reform.sscs.service;

import static org.junit.Assert.*;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.converters.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class CaseCodeServiceTest {

    CaseCodeService caseCodeService;

    @Test
    @Parameters({"PIP, 002", "ESA, 051", "UC, 001", "DLA, 037"})
    public void givenABenefit_thenReturnTheCorrectBenefitCode(String benefit, String expected) {
        assertEquals(expected, caseCodeService.generateBenefitCode(benefit));
    }

    @Test
    public void generateIssueCode() {
        assertEquals("DD", caseCodeService.generateIssueCode());
    }

    @Test
    @Parameters({"002, DD, 002DD", "051, DD, 051DD", "001, DD, 001DD", "037, DD, 037DD", "null, DD, null", "001, null, null"})
    public void givenABenefitAndIssueCode_thenGenerateCaseCode(@Nullable String benefit, @Nullable String issueCode, @Nullable String expected) {
        assertEquals(expected, caseCodeService.generateCaseCode(benefit, issueCode));
    }
}