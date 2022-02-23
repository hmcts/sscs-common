package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum HearingRequirementType {

    MUSTINC("mustinc", "MUSTINC", "Must Include"),
    EXCLUDE("exclude", "EXCLUDE", "Must Exclude"),
    OPTINC("optinc", "OPTINC", "Optional Include"),
    @JsonEnumDefaultValue
    UNKNOWN("unknown", "unknown", "Unknown");

    private final String type;
    private final String key;
    private final String description;

    HearingRequirementType(String type, String key, String description) {
        this.type = type;
        this.key = key;
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
