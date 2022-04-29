package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HearingState {

    CREATE_HEARING("createHearing"),
    UPDATED_CASE("updatedCase"),
    UPDATE_HEARING("updateHearing"),
    CANCEL_HEARING("cancelHearing"),
    PARTY_NOTIFIED("partyNotified");

    private final String state;

    @Override
    public String toString() {
        return state;
    }

}
