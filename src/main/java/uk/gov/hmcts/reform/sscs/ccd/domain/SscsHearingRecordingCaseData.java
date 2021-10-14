package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscsHearingRecordingCaseData {
    private DynamicList selectHearingDetails;
    private DynamicList requestableHearingDetails;
    private List<HearingRecordingRequest> requestedHearings;
    private List<HearingRecordingRequest> citizenReleasedHearings;
    private List<HearingRecordingRequest> dwpReleasedHearings;
    private List<HearingRecordingRequest> refusedHearings;
    private String requestedHearingsTextList;
    private String releasedHearingsTextList;
    private YesNo hearingRecordingRequestOutstanding;
    private DynamicList requestingParty;
    private YesNo showRequestingPartyPage;
    private List<ProcessHearingRecordingRequest> processHearingRecordingRequests;
    @JsonProperty("hearingRecording")
    private HearingRecording hearingRecording;
    @JsonProperty("sscsHearingRecordings")
    private List<SscsHearingRecording> sscsHearingRecordings;
    @JsonProperty("existingHearingRecordings")
    private SscsHearingRecordingDetails existingHearingRecordings;
    private YesNo hearingRecordingExist;
}
