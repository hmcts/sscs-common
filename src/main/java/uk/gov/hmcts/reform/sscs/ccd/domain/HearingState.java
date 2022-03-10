package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum HearingState {

    @JsonEnumDefaultValue
    @JsonProperty
    HEARING_CREATED("hearingCreated");

    private final String state;

    HearingState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return this.state;
    }
}
