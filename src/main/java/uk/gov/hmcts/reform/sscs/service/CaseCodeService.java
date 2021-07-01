package uk.gov.hmcts.reform.sscs.service;

import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;

import static com.microsoft.applicationinsights.boot.dependencies.apachecommons.lang3.StringUtils.isNotBlank;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelComposition.JUDGE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelComposition.JUDGE_AND_A_DOCTOR;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelComposition.JUDGE_DOCTOR_AND_DISABILITY_EXPERT;

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
