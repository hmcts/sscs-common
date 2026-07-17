package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HearingRecording {

    @CCD(
            label = "Recording",
            hint = "You can only upload MP3 and MP4 files up to 500MB",
            regex = ".mp3,.mp4,.MP3,.MP4",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "Document"
    )
    private List<HearingRecordingDetails> recordings;
    @CCD(
            label = "Hearing type",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_hearingRecordingType"
    )
    private String hearingType;
    @CCD(ignore = true)
    private List<SscsHearingRecording> existingHearingRecordingList;

    @JsonCreator
    public HearingRecording(@JsonProperty("recordings") List<HearingRecordingDetails> recordings,
                            @JsonProperty("hearingType") String hearingType,
                            @JsonProperty("existingHearingRecordingList") List<SscsHearingRecording> existingHearingRecordingList) {
        this.recordings = recordings;
        this.hearingType = hearingType;
        this.existingHearingRecordingList = existingHearingRecordingList;
    }
}

