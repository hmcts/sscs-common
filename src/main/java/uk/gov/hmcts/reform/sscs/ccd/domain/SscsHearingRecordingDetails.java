package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscsHearingRecordingDetails {
    private DocumentLink documentLink;
    private String fileName;
    private String hearingType;
    private String hearingDate;
    private String uploadDate;

    @JsonCreator
    public SscsHearingRecordingDetails(@JsonProperty("documentLink") DocumentLink documentLink,
                                       @JsonProperty("fileName") String fileName,
                                       @JsonProperty("hearingType") String hearingType,
                                       @JsonProperty("hearingDate") String hearingDate,
                                       @JsonProperty("uploadDate") String uploadDate) {
        this.documentLink = documentLink;
        this.fileName = fileName;
        this.hearingType = hearingType;
        this.hearingDate = hearingDate;
        this.uploadDate = uploadDate;
    }

}
