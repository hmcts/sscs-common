package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_reasonableAdjustmentStatus", generate = true)
public enum ReasonableAdjustmentStatus {
    @CCD(label = "Required")
    @JsonProperty("required")
    REQUIRED("required"),
    @CCD(label = "Actioned")
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
