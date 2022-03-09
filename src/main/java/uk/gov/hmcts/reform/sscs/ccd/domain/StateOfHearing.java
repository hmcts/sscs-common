package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Getter;

@Getter
public enum StateOfHearing {

    @JsonEnumDefaultValue
    HEARING_CREATED("Hearing Created");

    private final String state;

    StateOfHearing(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return this.state;
    }
}
