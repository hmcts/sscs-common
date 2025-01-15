package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TypeOfHearing {
    SUBSTANTIVE("substantive", "BBA3-SUB", "Substantive", null),
    DIRECTION_HEARINGS("direction", "BBA3-DIR", "Direction Hearings", null),
    CHAMBERS_OUTCOME("chamber", "BBA3-CHA", "Chambers Outcome", null);

    private final String ccdDefinition;
    private final String hmcReference;
    private final String valueEn;
    private final String valueCy;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}
