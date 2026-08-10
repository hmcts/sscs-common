package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_adjournCaseNextHearingDateType", generate = true)
@Getter
@AllArgsConstructor
public enum AdjournCaseNextHearingDateType {
    @CCD(label = "Date to be fixed")
    DATE_TO_BE_FIXED("dateToBeFixed", "Date to be fixed"),
    @CCD(label = "First available date after")
    FIRST_AVAILABLE_DATE_AFTER("firstAvailableDateAfter", "First available date after"),
    @CCD(label = "First available date")
    FIRST_AVAILABLE_DATE("firstAvailableDate", "First available date");

    private final String ccdDefinition;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }

}