package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HearingRoute {

    LIST_ASSIST("listAssist"),
    GAPS("gaps");

    private final String state;

    @Override
    public String toString() {
        return state;
    }
}
