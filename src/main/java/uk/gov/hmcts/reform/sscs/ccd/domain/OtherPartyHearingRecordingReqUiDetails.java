package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "otherPartyHearingRecordingReqUi", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class OtherPartyHearingRecordingReqUiDetails {

    @CCD(label = "Other party name")
    String otherPartyName;
    @CCD(label = "Other party id")
    String otherPartyId;
    @CCD(label = "Requesting Party", typeOverride = FieldType.FixedList, typeParameterOverride = "FL_parties")
    String requestingParty;
    @CCD(label = "Status", typeOverride = FieldType.DynamicList)
    DynamicList hearingRecordingStatus;

    @JsonCreator
    public OtherPartyHearingRecordingReqUiDetails(@JsonProperty("otherPartyName") String otherPartyName,
                                                  @JsonProperty("otherPartyId") String otherPartyId,
                                                  @JsonProperty("requestingParty") String requestingParty,
                                                  @JsonProperty("hearingRecordingStatus") DynamicList hearingRecordingStatus) {

        this.otherPartyName = otherPartyName;
        this.otherPartyId = otherPartyId;
        this.requestingParty = requestingParty;
        this.hearingRecordingStatus = hearingRecordingStatus;
    }
}
