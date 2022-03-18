package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HearingRoute {

    @JsonProperty
    LIST_ASSIST("listAssist"),
    @JsonProperty
    GAPS("gaps");

    private final String state;

    @JsonValue
    public String getState() {
        return state;
    }

}
