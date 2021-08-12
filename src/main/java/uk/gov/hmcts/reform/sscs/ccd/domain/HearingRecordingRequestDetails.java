package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@EqualsAndHashCode
public class HearingRecordingRequestDetails {
    private String requestingParty;
    private String dateRequested;
    private DocumentLink requestDocument;
    private List<SscsHearingRecording> sscsHearingRecordingList;


    @JsonCreator
    public HearingRecordingRequestDetails(@JsonProperty("requestingParty") String requestingParty,
                                   @JsonProperty("dateRequested") String dateRequested,
                                   @JsonProperty("requestDocument") DocumentLink requestDocument,
                                   @JsonProperty("sscsHearingRecordingList") List<SscsHearingRecording> sscsHearingRecordingList) {
        this.requestingParty = requestingParty;
        this.dateRequested = dateRequested;
        this.requestDocument = requestDocument;
        this.sscsHearingRecordingList = sscsHearingRecordingList;
    }
}
