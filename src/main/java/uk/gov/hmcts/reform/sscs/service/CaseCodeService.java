package uk.gov.hmcts.reform.sscs.service;

import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;

public final class CaseCodeService {

    private CaseCodeService() {
        // Empty
    }

    public static String generateBenefitCode(String benefit) {
        return Benefit.getBenefitByCode(benefit).getBenefitCode();
    }

    public static String generateIssueCode() {
        return "DD";
    }

    public static String generateCaseCode(String benefitCode, String issueCode) {
        return (benefitCode != null && issueCode != null) ? benefitCode + issueCode : null;
    }
}
