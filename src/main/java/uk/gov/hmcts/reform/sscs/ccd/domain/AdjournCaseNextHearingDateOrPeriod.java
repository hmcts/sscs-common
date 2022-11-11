package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdjournCaseNextHearingDateOrPeriod {
    PROVIDE_PERIOD("providePeriod", "Provide period"),
    PROVIDE_DATE("provideDate", "Provide date");

    private final String ccdDefinition;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }

    public static AdjournCaseNextHearingDateOrPeriod getDateOrPeriodByCcdDefinition(String ccdDefinition) {
        return Arrays.stream(AdjournCaseNextHearingDateOrPeriod.values())
            .filter(adjournCaseNextHearingDateOrPeriod -> Objects.equals(adjournCaseNextHearingDateOrPeriod.ccdDefinition, ccdDefinition))
            .findFirst()
            .orElse(null);
    }
}