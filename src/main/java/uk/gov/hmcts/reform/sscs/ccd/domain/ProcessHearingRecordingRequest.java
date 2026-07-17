package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.builder.CompareToBuilder;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class ProcessHearingRecordingRequest implements Comparable<ProcessHearingRecordingRequest> {

    @CCD(label = "Hearing id")
    String hearingId;
    @CCD(label = " ")
    String hearingTitle;
    @CCD(label = " ")
    String hearingInformation;
    @CCD(label = " ", typeOverride = FieldType.Collection, typeParameterOverride = "Document")
    List<CcdValue<DocumentLink>> recordings;
    @CCD(label = "FTA", typeOverride = FieldType.DynamicList)
    DynamicList dwp;
    @CCD(label = "Joint Party", typeOverride = FieldType.DynamicList)
    DynamicList jointParty;
    @CCD(label = "Appellant", typeOverride = FieldType.DynamicList)
    DynamicList appellant;
    @CCD(label = "Representative", typeOverride = FieldType.DynamicList)
    DynamicList rep;

    @JsonCreator
    public ProcessHearingRecordingRequest(@JsonProperty("hearingId") String hearingId,
                                                 @JsonProperty("hearingTitle") String hearingTitle,
                                                 @JsonProperty("hearingInformation") String hearingInformation,
                                                 @JsonProperty("recordings") List<CcdValue<DocumentLink>> recordings,
                                                 @JsonProperty("dwp") DynamicList dwp,
                                                 @JsonProperty("jointParty") DynamicList jointParty,
                                                 @JsonProperty("appellant") DynamicList appellant,
                                                 @JsonProperty("rep") DynamicList rep) {

        this.hearingId = hearingId;
        this.hearingTitle = hearingTitle;
        this.hearingInformation = hearingInformation;
        this.recordings = recordings;
        this.dwp = dwp;
        this.jointParty = jointParty;
        this.appellant = appellant;
        this.rep = rep;
    }

    @Override
    public int compareTo(ProcessHearingRecordingRequest o) {
        return new CompareToBuilder()
                .append(this.getHearingId(),
                        o.getHearingId())
                .toComparison();
    }

}
