package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@lombok.Value
@Builder(toBuilder = true)
public class ProcessHearingRecordingRequestDetails {

    String hearingId;
    String hearingTitle;
    String hearingInformation;
    List<CcdValue<DocumentLink>> recordings;
    DynamicList dwp;
    DynamicList jointParty;
    DynamicList appellant;

    @JsonCreator
    public ProcessHearingRecordingRequestDetails(@JsonProperty("hearingId") String hearingId,
                                          @JsonProperty("hearingTitle") String hearingTitle,
                                          @JsonProperty("hearingInformation") String hearingInformation,
                                          @JsonProperty("recordings") List<CcdValue<DocumentLink>> recordings,
                                          @JsonProperty("dwp") DynamicList dwp,
                                          @JsonProperty("jointParty") DynamicList jointParty,
                                          @JsonProperty("appellant") DynamicList appellant) {

        this.hearingId = hearingId;
        this.hearingTitle = hearingTitle;
        this.hearingInformation = hearingInformation;
        this.recordings = recordings;
        this.dwp = dwp;
        this.jointParty = jointParty;
        this.appellant = appellant;
    }
}
