package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SscsHearingType {

    SUBSTANTIVE("substantive", "BBA3-substantive", "Substantive", "BBA3"),
    DIRECTION_HEARINGS("directionHearings", "BBA3-directionHearings", "Direction Hearings", "BBA3"),
    CHAMBERS_OUTCOME("chambersOutcome", "BBA3-chambersOutcome", "Chambers Outcome", "BBA3");

    private final String type;
    private final String key;
    private final String descriptionEn;
    private final String serviceCode;

    @JsonValue
    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
