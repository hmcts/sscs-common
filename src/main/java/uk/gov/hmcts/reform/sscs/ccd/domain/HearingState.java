package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HearingState {

    @JsonProperty
    CREATE_HEARING("createHearing"),
    @JsonProperty
    UPDATED_CASE("updatedCase"),
    @JsonProperty
    UPDATE_HEARING("updateHearing"),
    @JsonProperty
    CANCEL_HEARING("cancelHearing"),
    @JsonProperty
    PARTY_NOTIFIED("partyNotified");

    private final String state;

    @JsonValue
    public String getState() {
        return state;
    }

}
