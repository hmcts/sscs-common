package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdjournCaseNextHearingPeriod {
    NINETY_DAYS("90", 90, "90 days"),
    FORTY_TWO_DAYS("42", 42, "42 days"),
    TWENTY_EIGHT_DAYS("28", 28, "28 days");

    private final String ccdDefinition;
    private final Integer number;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(ccdDefinition);
    }

    public static AdjournCaseNextHearingPeriod getPeriodByNumber(Integer number) {
        return Arrays.stream(AdjournCaseNextHearingPeriod.values())
            .filter(adjournCaseNextHearingPeriod -> Objects.equals(adjournCaseNextHearingPeriod.number, number))
            .findFirst()
            .orElse(null);
    }

    public static AdjournCaseNextHearingPeriod getPeriodByCcdDefinition(String ccdDefinition) {
        return Arrays.stream(AdjournCaseNextHearingPeriod.values())
            .filter(adjournCaseNextHearingPeriod -> Objects.equals(adjournCaseNextHearingPeriod.ccdDefinition, ccdDefinition))
            .findFirst()
            .orElse(null);
    }
}