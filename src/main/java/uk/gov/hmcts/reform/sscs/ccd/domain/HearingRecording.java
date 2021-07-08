package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HearingRecording {

    private List<HearingRecordingDetails> recordings;
    private String hearingType;

    @JsonCreator
    public HearingRecording(@JsonProperty("recordings") List<HearingRecordingDetails> recordings,
                            @JsonProperty("hearingType") String hearingType) {
        this.recordings = recordings;
        this.hearingType = hearingType;
    }
}

