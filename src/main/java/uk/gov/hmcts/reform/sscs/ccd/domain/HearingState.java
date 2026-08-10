package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_hearingState", generate = true)
@Getter
@AllArgsConstructor
public enum HearingState {
    ADJOURN_CREATE_HEARING("adjournCreateHearing"),
    @CCD(label = "Create Hearing")
    CREATE_HEARING("createHearing"),
    @CCD(label = "Case Updated")
    UPDATED_CASE("updatedCase"),
    @CCD(label = "Update Hearing")
    UPDATE_HEARING("updateHearing"),
    @CCD(label = "Cancel Hearing")
    CANCEL_HEARING("cancelHearing"),
    @CCD(label = "Parties Notified")
    PARTY_NOTIFIED("partyNotified");

    private final String state;

    @Override
    @JsonValue
    public String toString() {
        return state;
    }

}
