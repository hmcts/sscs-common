package uk.gov.hmcts.reform.sscs.ccd.domain;

public enum ReasonableAdjustmentStatus {
    REQUIRED("required"),
    ACTIONED("actioned");
    private String id;
    ReasonableAdjustmentStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
