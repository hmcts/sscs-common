package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum HearingLocationType {
    @JsonProperty
    COURT("court"),
    @JsonProperty
    CLUSTER("cluster"),
    @JsonProperty
    REGION("region");

    private final String type;

    HearingLocationType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
