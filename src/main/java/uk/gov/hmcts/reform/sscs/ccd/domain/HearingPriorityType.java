package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum HearingPriorityType {
    HIGH("high", "High Priority"),
    @JsonEnumDefaultValue
    NORMAL("normal", "Normal Priority");

    private final String type;
    private final String description;

    HearingPriorityType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    @JsonValue
    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
