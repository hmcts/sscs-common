package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdjournCaseNextHearingDurationType {
    NON_STANDARD("nonStandardTimeSlot", "Non standard time slot"),
    STANDARD("standardTimeSlot", "Standard time slot");

    private final String ccdDefinition;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }

    public static AdjournCaseNextHearingDurationType getDurationTypeByCcdDefinition(String ccdDefinition) {
        return Arrays.stream(AdjournCaseNextHearingDurationType.values())
            .filter(adjournCaseNextHearingDurationType -> Objects.equals(adjournCaseNextHearingDurationType.ccdDefinition, ccdDefinition))
            .findFirst()
            .orElse(null);
    }
}