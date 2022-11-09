package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdjournCaseNextHearingPeriod {
    NINETY_DAYS(90, "90 days"),
    FORTY_TWO_DAYS(42, "42 days"),
    TWENTY_EIGHT_DAYS(28, "28 days");

    private final int ccdDefinition;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(ccdDefinition);
    }
}