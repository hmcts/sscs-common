package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.reform.sscs.ccd.domain.HearingType;

@Getter
@RequiredArgsConstructor
public enum HearingTypeLov {
    SUBSTANTIVE("BBA3-substantive", "Substantive", "", "BBA3"),
    DIRECTION_HEARINGS("BBA3-directionHearings", "Direction Hearings", "", "BBA3"),
    CHAMBERS_OUTCOME("BBA3-chambersOutcome", "Chambers Outcome", "", "BBA3");

    private final String key;
    private final String valueEN;
    private final String valueCY;
    private final String serviceCode;

    public static HearingType getHearingType(String value) {
        for (HearingType ht : HearingType.values()) {
            if (ht.getValue().equals(value)) {
                return ht;
            }
        }
        return null;
    }
}
