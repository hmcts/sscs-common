package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_adjournCaseNextHearingVenue", generate = true)
@Getter
@AllArgsConstructor
public enum AdjournCaseNextHearingVenue {
    @CCD(label = "Somewhere else")
    SOMEWHERE_ELSE("somewhereElse", "Somewhere else"),
    @CCD(label = "Same venue")
    SAME_VENUE("sameVenue", "Same venue");

    private final String ccdDefinition;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }

}
