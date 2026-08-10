package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;


@ComplexType(name = "hearingRecordingRequest", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode
public class HearingRecordingRequestDetails {
    @CCD(
            label = "Requesting Party",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_parties",
            typeParameterClass = Parties.class
    )
    private String requestingParty;
    @CCD(label = "Status")
    private String status;
    @CCD(label = "Other Party Id", showCondition = "requestingParty=\"AnyValueToFailThisCondition\"")
    private String otherPartyId;
    @CCD(label = "Date requested", typeOverride = FieldType.Date)
    private String dateRequested;
    @CCD(label = "Date approved", typeOverride = FieldType.Date)
    private String dateApproved;
    @CCD(label = "Document", typeOverride = FieldType.Document)
    private DocumentLink requestDocument;
    @CCD(label = "Hearing recording")
    private SscsHearingRecordingDetails sscsHearingRecording;


    @JsonCreator
    public HearingRecordingRequestDetails(@JsonProperty("requestingParty") String requestingParty,
                                          @JsonProperty("status") String status,
                                          @JsonProperty("otherPartyId") String otherPartyId,
                                          @JsonProperty("dateRequested") String dateRequested,
                                          @JsonProperty("dateApproved") String dateApproved,
                                          @JsonProperty("requestDocument") DocumentLink requestDocument,
                                          @JsonProperty("sscsHearingRecording") SscsHearingRecordingDetails sscsHearingRecording) {
        this.requestingParty = requestingParty;
        this.status = status;
        this.otherPartyId = otherPartyId;
        this.dateRequested = dateRequested;
        this.dateApproved = dateApproved;
        this.requestDocument = requestDocument;
        this.sscsHearingRecording = sscsHearingRecording;
    }
}
