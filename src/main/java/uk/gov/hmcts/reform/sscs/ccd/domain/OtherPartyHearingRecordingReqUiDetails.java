package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class OtherPartyHearingRecordingReqUiDetails {

    String otherPartyName;
    String otherPartyId;
    DynamicList hearingRecordingStatus;

    @JsonCreator
    public OtherPartyHearingRecordingReqUiDetails(@JsonProperty("otherPartyName") String otherPartyName,
                                                  @JsonProperty("otherPartyId") String otherPartyId,
                                                  @JsonProperty("hearingRecordingStatus") DynamicList hearingRecordingStatus) {

        this.otherPartyName = otherPartyName;
        this.otherPartyId = otherPartyId;
        this.hearingRecordingStatus = hearingRecordingStatus;
    }
}
