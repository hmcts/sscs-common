package uk.gov.hmcts.reform.sscs.service;

import static com.microsoft.applicationinsights.boot.dependencies.apachecommons.lang3.StringUtils.isNotBlank;

import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;

public final class CaseCodeService {

    private CaseCodeService() {
        // Empty
    }

    public static String generateBenefitCode(String benefit, String addressName) {
        Benefit selectedBenefit = Benefit.getBenefitByCode(benefit);
        if (selectedBenefit.equals(Benefit.SOCIAL_FUND) && isNotBlank(addressName)) {
            switch (addressName) {
                case "St Helens Sure Start Maternity Grant":
                    return "088";
                case "Funeral Payment Dispute Resolution Team":
                    return "089";
                case "Pensions Dispute Resolution Team":
                    return "061";
                default: return "088";
            }
        }
        return selectedBenefit.getBenefitCode();
    }

    public static String generateIssueCode() {
        return "DD";
    }

    public static String generateCaseCode(String benefitCode, String issueCode) {
        return (benefitCode != null && issueCode != null) ? benefitCode + issueCode : null;
    }
}
