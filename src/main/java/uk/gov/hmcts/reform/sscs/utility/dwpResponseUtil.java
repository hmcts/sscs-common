package uk.gov.hmcts.reform.sscs.utility;

public class dwpResponseUtil {

    public static final int MAX_DWP_RESPONSE_DAYS = 28;
    public static final int MAX_DWP_RESPONSE_DAYS_CHILD_SUPPORT = 42;

    public int calculateMaxDwpResponseDays(String benefitCode) {
        if (benefitCode == "childSupport" ) {
            return MAX_DWP_RESPONSE_DAYS_CHILD_SUPPORT;
        }
        else {
            return MAX_DWP_RESPONSE_DAYS;
        }
    };
}
