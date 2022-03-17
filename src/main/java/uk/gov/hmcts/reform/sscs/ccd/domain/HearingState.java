package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HearingState {

    @JsonProperty
    CREATE_HEARING("createHearing", "Create Hearing"),
    @JsonProperty
    UPDATED_CASE("updatedCase", "Case Updated"),
    @JsonProperty
    UPDATE_HEARING("updateHearing", "Update Hearing"),
    @JsonProperty
    CANCEL_HEARING("cancelHearing", "Cancel Hearing"),
    @JsonProperty
    PARTY_NOTIFIED("partyNotified", "Parties Notified");

    private final String state;
    private final String description;

    @JsonValue
    public String getState() {
        return state;
    }

}
