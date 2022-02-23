package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum HearingRequirementType {
    @JsonProperty
    MUSTINC("mustinc", "Must Include"),
    @JsonProperty
    EXCLUDE("exclude", "Must Exclude"),
    @JsonEnumDefaultValue
    @JsonProperty
    OPTINC("optinc", "Optional Include");

    private final String type;
    private final String description;

    HearingRequirementType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
