package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class SscsHearingRecordingsData {

    private DynamicList selectHearingDetails;
    private DynamicList requestableHearingDetails;
    private List<HearingRecordingRequest> requestedHearings;
    private List<HearingRecordingRequest> hearingRecordingRequests;
    private List<HearingRecordingRequest> releasedHearings;
}
