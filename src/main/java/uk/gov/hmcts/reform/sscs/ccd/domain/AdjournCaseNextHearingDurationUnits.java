package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_adjournCaseNextHearingDurationUnits", generate = true)
@Getter
@AllArgsConstructor
public enum AdjournCaseNextHearingDurationUnits {
    @CCD(label = "Session(s)")
    SESSIONS("sessions", "Session(s)"),
    @CCD(label = "Minutes")
    MINUTES("minutes", "Minutes");

    private final String ccdDefinition;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }

}