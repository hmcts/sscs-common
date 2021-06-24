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

    @JsonProperty("hearingRecordings")
    private List<HearingRecording> hearingRecordings;

    @JsonProperty("sscsHearingRecordings")
    private List<SscsHearingRecording> sscsHearingRecordings;

    @JsonProperty("showHearingRecordings")
    private YesNo showHearingRecordings;

}