package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdjournCaseNextHearingDateType {
    DATE_TO_BE_FIXED("dateToBeFixed", "Date to be fixed"),
    FIRST_AVAILABLE_DATE_AFTER("firstAvailableDateAfter", "First available date after"),
    FIRST_AVAILABLE_DATE("firstAvailableDate", "First available date");

    private final String ccdDefinition;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }

    public static AdjournCaseNextHearingDateType getDateTypeByCcdDefinition(String ccdDefinition) {
        return Arrays.stream(AdjournCaseNextHearingDateType.values())
            .filter(adjournCaseNextHearingDateType -> Objects.equals(adjournCaseNextHearingDateType.ccdDefinition, ccdDefinition))
            .findFirst()
            .orElse(null);
    }
}