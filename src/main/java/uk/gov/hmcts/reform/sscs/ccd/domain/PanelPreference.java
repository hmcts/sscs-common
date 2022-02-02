package uk.gov.hmcts.reform.sscs.ccd.domain;

public class PanelPreference {

    private String memberId;
    private String memberType;
    private RequirementType requirementType;

    public enum RequirementType {
        MUSTINC,
        OPTINC,
        EXCLUDE
    }
}
