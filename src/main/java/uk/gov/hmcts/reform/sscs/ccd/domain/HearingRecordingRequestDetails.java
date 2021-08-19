package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode
public class HearingRecordingRequestDetails {
    private String requestingParty;
    private String dateRequested;
    private String dateApproved;
    private DocumentLink requestDocument;
    private SscsHearingRecording sscsHearingRecording;


    @JsonCreator
    public HearingRecordingRequestDetails(@JsonProperty("requestingParty") String requestingParty,
                                          @JsonProperty("dateRequested") String dateRequested,
                                          @JsonProperty("dateApproved") String dateApproved,
                                          @JsonProperty("requestDocument") DocumentLink requestDocument,
                                          @JsonProperty("sscsHearingRecording") SscsHearingRecording sscsHearingRecording) {
        this.requestingParty = requestingParty;
        this.dateRequested = dateRequested;
        this.dateApproved = dateApproved;
        this.requestDocument = requestDocument;
        this.sscsHearingRecording = sscsHearingRecording;
    }
}
