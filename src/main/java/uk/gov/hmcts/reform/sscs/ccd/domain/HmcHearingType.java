package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_hmcHearingType", generate = true)
@Getter
@RequiredArgsConstructor
public enum HmcHearingType {
    SUBSTANTIVE("BBA3-SUB", "Substantive", null),
    DIRECTION_HEARINGS("BBA3-DIR", "Direction Hearings", null),
    CHAMBERS_OUTCOME("BBA3-CHA", "Chambers Outcome", null);

    private final String hmcReference;
    private final String valueEn;
    private final String valueCy;

    @Override
    @JsonValue
    public String toString() {
        return hmcReference;
    }
}
