package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class HearingRecordingRequest {
    private HearingRecordingRequestDetails value;

    @JsonCreator
    public HearingRecordingRequest(@JsonProperty("value") HearingRecordingRequestDetails value) {
        this.value = value;
    }
}
