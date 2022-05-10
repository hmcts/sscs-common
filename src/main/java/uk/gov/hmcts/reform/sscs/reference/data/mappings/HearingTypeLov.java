package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingTypeLov {

    SUBSTANTIVE("BBA3-substantive", "Substantive", "", "BBA3"),
    DIRECTION_HEARINGS("BBA3-directionHearings", "Direction Hearings", "", "BBA3"),
    CHAMBERS_OUTCOME("BBA3-chambersOutcome", "Chambers Outcome", "", "BBA3");

    private final String hmcReference;
    private final String valueEn;
    private final String valueCy;
    private final String serviceCode;

}