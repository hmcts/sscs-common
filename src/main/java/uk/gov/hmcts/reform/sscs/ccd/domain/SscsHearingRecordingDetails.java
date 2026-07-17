package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode
public class SscsHearingRecordingDetails {
    @CCD(label = "Recordings", typeOverride = FieldType.Collection, typeParameterOverride = "Document")
    private List<HearingRecordingDetails> recordings;
    @CCD(
            label = "Hearing type",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_hearingRecordingType"
    )
    private String hearingType;
    @CCD(label = "Hearing date and time")
    private String hearingDate;
    @CCD(label = "Upload date and time")
    private String uploadDate;
    @CCD(label = "Hearing ID")
    private String hearingId;
    @CCD(label = "Venue")
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
