package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_adjournCaseNextHearingDateOrPeriod", generate = true)
@Getter
@AllArgsConstructor
public enum AdjournCaseNextHearingDateOrPeriod {
    @CCD(label = "Provide period")
    PROVIDE_PERIOD("providePeriod", "Provide period"),
    @CCD(label = "Provide date")
    PROVIDE_DATE("provideDate", "Provide date");

    private final String ccdDefinition;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }

}