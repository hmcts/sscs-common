package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HmcHearingType {
    SUBSTANTIVE("BBA3-SUB", "Substantive", null),
    DIRECTION_HEARINGS("BBA3-DIR", "Direction Hearings", null),
    CHAMBERS_OUTCOME("BBA3-CHA", "Chambers Outcome", null);

    @JsonValue
    private final String hmcReference;
    private final String valueEn;
    private final String valueCy;
}
