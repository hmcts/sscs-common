package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@EqualsAndHashCode
public class OtherPartyHearingRecordingReqUi {
    private OtherPartyHearingRecordingReqUiDetails value;

    @JsonCreator
    public OtherPartyHearingRecordingReqUi(@JsonProperty("value") OtherPartyHearingRecordingReqUiDetails value) {
        this.value = value;
    }
}
