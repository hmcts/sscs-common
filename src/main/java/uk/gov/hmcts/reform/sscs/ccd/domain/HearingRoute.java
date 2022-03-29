package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HearingRoute {

    LIST_ASSIST("listAssist"),
    GAPS("gaps");

    private final String state;

    @JsonValue
    public String getState() {
        return state;
    }

}
