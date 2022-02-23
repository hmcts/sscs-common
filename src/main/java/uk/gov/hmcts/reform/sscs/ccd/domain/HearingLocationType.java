package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingLocationType {
    COURT("court", "Court"),
    CLUSTER("cluster", "Cluster"),
    REGION("region", "Region");

    private final String type;
    private final String description;

    @JsonValue
    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
