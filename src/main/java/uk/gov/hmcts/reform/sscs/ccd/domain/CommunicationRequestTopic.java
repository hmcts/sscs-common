package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum CommunicationRequestTopic {

    @JsonProperty("appealType")
    APPEAL_TYPE("Appeal Type"),

    @JsonProperty("appellantPersonalInfo")
    APPELLANT_PERSONAL_INFORMATION("Appellant personal information"),

    @JsonProperty("appointeePersonalInfo")
    APPOINTEE_PERSONAL_INFORMATION("Appointee personal information"),

    @JsonProperty("issuingOffice")
    ISSUING_OFFICE("Issuing office"),

    @JsonProperty("jointPartyPersonalInfo")
    JOINT_PARTY_PERSONAL_INFORMATION("Joint party personal information"),

    @JsonProperty("mrnDetails")
    MRN_REVIEW_DECISION_NOTICE_DETAILS("MRN/Review Decision Notice Details"),

    @JsonProperty("otherPartyPersonalInfo")
    OTHER_PARTY_PERSONAL_INFORMATION("Other party personal information");

    private final String value;

    CommunicationRequestTopic(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
    
}
