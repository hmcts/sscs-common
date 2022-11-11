package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdjournCaseNextHearingDurationUnits {
    SESSIONS("sessions", "Session(s)"),
    MINUTES("minutes", "Minutes");

    private final String ccdDefinition;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
    
    public static AdjournCaseNextHearingDurationUnits getDurationUnitsByCcdDefinition(String ccdDefinition) {
        return Arrays.stream(AdjournCaseNextHearingDurationUnits.values())
            .filter(adjournCaseNextHearingDurationUnits -> Objects.equals(adjournCaseNextHearingDurationUnits.ccdDefinition, ccdDefinition))
            .findFirst()
            .orElse(null);
    }
}