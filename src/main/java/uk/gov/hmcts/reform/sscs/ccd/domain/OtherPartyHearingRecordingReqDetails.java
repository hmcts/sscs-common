package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class OtherPartyHearingRecordingReqDetails {

    HearingRecordingRequestDetails hearingRecordingRequest;
    String otherPartyId;

    @JsonCreator
    public OtherPartyHearingRecordingReqDetails(@JsonProperty("hearingRecordingRequest") HearingRecordingRequestDetails hearingRecordingRequest,
                                                  @JsonProperty("otherPartyId") String otherPartyId) {

        this.hearingRecordingRequest = hearingRecordingRequest;
        this.otherPartyId = otherPartyId;
    }
}