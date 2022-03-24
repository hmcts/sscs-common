package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingTypeLov {
    SUBSTANTIVE("BBA3-substantive", "Substantive"),
    DIRECTION_HEARINGS("BBA3-directionHearings", "Direction Hearings"),
    CHAMBERS_OUTCOME("BBA3-chambersOutcome", "Chambers Outcome");

    private final String key;
    private final String value;

    public static HearingType getHearingType(String value) {
        for (HearingType ht : HearingType.values()) {
            if (ht.getValue().equals(value)) {
                return ht;
            }
        }
        return null;
    }
}