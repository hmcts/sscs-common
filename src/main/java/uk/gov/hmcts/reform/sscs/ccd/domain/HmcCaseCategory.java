package uk.gov.hmcts.reform.sscs.ccd.domain;

public class HmcCaseCategory {
    private HmcCategoryType categoryType;
    private String categoryValue;

    public enum HmcCategoryType {
        caseType,
        caseSubType
    }
}
