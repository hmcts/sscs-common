package uk.gov.hmcts.reform.sscs.ccd.domain;

public enum ReasonableAdjustmentStatus {
    REQUIRED("required"),
    DONE("done");
    private String id;
    ReasonableAdjustmentStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
