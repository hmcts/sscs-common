package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode
public class SscsHearingRecordingDetails {
    private List<HearingRecordingDetails> recordings;
    private String hearingType;
    private String hearingDate;
    private String uploadDate;
    private String hearingId;
    private String venue;

    @JsonCreator
    public SscsHearingRecordingDetails(@JsonProperty("recordings") List<HearingRecordingDetails> recordings,
                                       @JsonProperty("hearingType") String hearingType,
                                       @JsonProperty("hearingDate") String hearingDate,
                                       @JsonProperty("uploadDate") String uploadDate,
                                       @JsonProperty("hearingId") String hearingId,
                                       @JsonProperty("venue") String venue) {
        this.recordings = recordings;
        this.hearingType = hearingType;
        this.hearingDate = hearingDate;
        this.uploadDate = uploadDate;
        this.hearingId = hearingId;
        this.venue = venue;
    }

}
