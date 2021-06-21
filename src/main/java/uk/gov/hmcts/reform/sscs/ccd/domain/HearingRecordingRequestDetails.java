package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class HearingRecordingRequestDetails {
    String requestingParty;
    String requestedHearing;
    String status;
    String requestedHearingName;
    LocalDateTime dateRequested;


    @JsonCreator
    public HearingRecordingRequestDetails(@JsonProperty("requestingParty") String requestingParty,
                                   @JsonProperty("requestedHearing") String requestedHearing,
                                   @JsonProperty("status") String status,
                                   @JsonProperty("requestedHearingName") String requestedHearingName,
                                   @JsonProperty("dateRequested") LocalDateTime dateRequested) {
        this.requestingParty = requestingParty;
        this.requestedHearing = requestedHearing;
        this.status = status;
        this.requestedHearingName = requestedHearingName;
        this.dateRequested = dateRequested;
    }
}
