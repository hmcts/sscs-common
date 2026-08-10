package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_hearingRoute", generate = true)
@Getter
@AllArgsConstructor
public enum HearingRoute {

    @CCD(label = "List Assist")
    LIST_ASSIST("listAssist"),
    @CCD(label = "Gaps")
    GAPS("gaps");

    private final String state;

    @Override
    @JsonValue
    public String toString() {
        return state;
    }
}
