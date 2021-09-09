package uk.gov.hmcts.reform.sscs.service;

import static java.util.Optional.of;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.Optional;
import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;

public final class CaseCodeService {

    private CaseCodeService() {
        //not called
    }

    public static Optional<String> generateBenefitCode(String benefit, String addressName) {
        Optional<Benefit> benefitOptional = Benefit.getBenefitOptionalByCode(benefit);
        if (Benefit.SOCIAL_FUND.equals(benefitOptional.orElse(null)) && isNotEmpty(addressName)) {
            return socialFundAddressNameBenefitCode(addressName);
        }
        return benefitOptional.map(Benefit::getBenefitCode);
    }

    private static Optional<String> socialFundAddressNameBenefitCode(String addressName) {
        switch (addressName) {
            case "St Helens Sure Start Maternity Grant":
                return of("088");
            case "Funeral Payment Dispute Resolution Team":
                return of("089");
            case "Pensions Dispute Resolution Team":
                return of("061");
            default: return of("088");
        }
    }

    public static String generateIssueCode() {
        return "DD";
    }

    public static String generateCaseCode(String benefitCode, String issueCode) {
        return (benefitCode != null && issueCode != null) ? benefitCode + issueCode : null;
    }
}
