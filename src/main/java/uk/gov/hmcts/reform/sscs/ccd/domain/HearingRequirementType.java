package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingRequirementType {

    MUSTINC("mustinc", "MUSTINC", "Must Include"),
    EXCLUDE("exclude", "EXCLUDE", "Must Exclude"),
    OPTINC("optinc", "OPTINC", "Optional Include");

    private final String type;
    private final String key;
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
