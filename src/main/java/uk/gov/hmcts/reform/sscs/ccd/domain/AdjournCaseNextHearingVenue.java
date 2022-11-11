package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdjournCaseNextHearingVenue {
    SOMEWHERE_ELSE("somewhereElse", "Somewhere else"),
    SAME_VENUE("sameVenue", "Same venue");

    private final String ccdDefinition;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }

    public static AdjournCaseNextHearingVenue getNextHearingVenueByCcdDefinition(String ccdDefinition) {
        return Arrays.stream(AdjournCaseNextHearingVenue.values())
            .filter(adjournCaseNextHearingVenue -> Objects.equals(adjournCaseNextHearingVenue.ccdDefinition, ccdDefinition))
            .findFirst()
            .orElse(null);
    }
}
