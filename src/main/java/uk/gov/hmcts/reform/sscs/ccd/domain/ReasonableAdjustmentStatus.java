package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ReasonableAdjustmentStatus {
    @JsonProperty("required")
    REQUIRED("required"),
    @JsonProperty("actioned")
    ACTIONED("actioned");

    private String id;
    
    ReasonableAdjustmentStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String toString() {
        return id;
    }
}
