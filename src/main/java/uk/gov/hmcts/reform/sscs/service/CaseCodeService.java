package uk.gov.hmcts.reform.sscs.service;

import org.apache.commons.lang.StringUtils;

public final class CaseCodeService {

    private CaseCodeService() {
        // Empty
    }

    public static String generateBenefitCode(String benefit) {
        if (StringUtils.equalsIgnoreCase("esa", benefit)) {
            return "051";
        } else if (StringUtils.equalsIgnoreCase("pip", benefit)) {
            return "002";
        } else if (StringUtils.equalsIgnoreCase("uc", benefit)) {
            return "001";
        } else {
            return "";
        }
    }

    public static String generateIssueCode() {
        return "DD";
    }

    public static String generateCaseCode(String benefitCode, String issueCode) {
        return (benefitCode != null && issueCode != null) ? benefitCode + issueCode : null;
    }
}
