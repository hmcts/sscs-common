package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@EqualsAndHashCode
public class HearingRecordingRequest {
    @CCD(ignore = true)
    private HearingRecordingRequestDetails value;

    @JsonCreator
    public HearingRecordingRequest(@JsonProperty("value") HearingRecordingRequestDetails value) {
        this.value = value;
    }
}
