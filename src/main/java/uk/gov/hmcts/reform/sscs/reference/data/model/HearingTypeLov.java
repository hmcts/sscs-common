package uk.gov.hmcts.reform.sscs.reference.data.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingTypeLov {

    SUBSTANTIVE("BBA3-SUB", "Substantive", null),
    DIRECTION_HEARINGS("BBA3-DIR", "Direction Hearings", null),
    CHAMBERS_OUTCOME("BBA3-CHA", "Chambers Outcome", null);

    private final String hmcReference;
    private final String valueEn;
    private final String valueCy;

}
