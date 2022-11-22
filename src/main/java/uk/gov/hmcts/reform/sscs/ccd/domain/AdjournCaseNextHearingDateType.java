package uk.gov.hmcts.reform.sscs.ccd.domain;

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

}