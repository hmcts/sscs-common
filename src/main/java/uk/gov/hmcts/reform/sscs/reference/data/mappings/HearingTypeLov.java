package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingTypeLov {

    SUBSTANTIVE("BBA3-substantive", "Substantive", null),
    DIRECTION_HEARINGS("BBA3-directionHearings", "Direction Hearings", null),
    CHAMBERS_OUTCOME("BBA3-chambersOutcome", "Chambers Outcome", null);

    private final String hmcReference;
    private final String valueEn;
    private final String valueCy;

}