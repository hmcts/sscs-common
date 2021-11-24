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
    DynamicList hearingRecordingStatus;

    @JsonCreator
    public OtherPartyHearingRecordingReqUiDetails(@JsonProperty("otherPartyName") String otherPartyName,
                                                  @JsonProperty("hearingRecordingStatus") DynamicList hearingRecordingStatus) {

        this.otherPartyName = otherPartyName;
        this.hearingRecordingStatus = hearingRecordingStatus;
    }
}
