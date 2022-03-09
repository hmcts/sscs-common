package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Getter;

@Getter
public enum HearingRoute {

    @JsonEnumDefaultValue
    LIST_ASSIST("List Assist"),
    GAPS("Gaps");

    private final String type;

    HearingRoute(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
