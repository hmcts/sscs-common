package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DirectionType {

    @JsonProperty("appealToProceed")
    APPEAL_TO_PROCEED("appealToProceed", "Appeal to Proceed"),

    @JsonProperty("provideInformation")
    PROVIDE_INFORMATION("provideInformation", "Provide information"),

    @JsonProperty("issueAndSendToAdmin")
    ISSUE_AND_SEND_TO_ADMIN("issueAndSendToAdmin", "Issue direction and send to admin"),

    @JsonProperty("grantExtension")
    GRANT_EXTENSION("grantExtension", "Allow time extension"),

    @JsonProperty("refuseExtension")
    REFUSE_EXTENSION("refuseExtension", "Refuse time extension"),

    @JsonProperty("grantReinstatement")
    GRANT_REINSTATEMENT("grantReinstatement", "Grant reinstatement"),

    @JsonProperty("refuseReinstatement")
    REFUSE_REINSTATEMENT("refuseReinstatement", "Refuse reinstatement"),

    @JsonProperty("grantUrgentHearing")
    GRANT_URGENT_HEARING("grantUrgentHearing", "Grant urgent hearing"),

    @JsonProperty("refuseUrgentHearing")
    REFUSE_URGENT_HEARING("refuseUrgentHearing", "Refuse urgent hearing"),

    @JsonProperty("refuseHearingRecordingRequest")
    REFUSE_HEARING_RECORDING_REQUEST("refuseHearingRecordingRequest", "Refuse hearing recording request"),

    @JsonProperty("confidentialityGrantedSendToAdmin")
    CONFIDENTIALITY_GRANTED_SEND_TO_ADMIN("confidentialityGrantedSendToAdmin", "Confidentiality Granted – Send to Admin"),

    @JsonProperty("confidentialityRefusedSendToAdmin")
    CONFIDENTIALITY_REFUSED_SEND_TO_ADMIN("confidentialityRefusedSendToAdmin", "Confidentiality Refused – Send to Admin");

    // needed only for the toString method
    private final String id;

    private final String label;

    DirectionType(String id, String label) {
        this.id = id;
        this.label = label;
    }

    // todo: get rid of the need to override toString as READ_ENUMS_USING_TO_STRING is enabled in other projects (i.e. remove READ_ENUMS_USING_TO_STRING)
    @Override
    public String toString() {
        return id;
    }

    public String getLabel() {
        return label;
    }
}
