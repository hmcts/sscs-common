package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum HearingRoute {

    @JsonEnumDefaultValue
    @JsonProperty
    LIST_ASSIST("listAssist"),
    @JsonProperty
    GAPS("gaps");

    private final String type;

    HearingRoute(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
