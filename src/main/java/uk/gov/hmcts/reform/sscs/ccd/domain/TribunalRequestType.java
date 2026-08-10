package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_tribunalRequestType", generate = true)
public enum TribunalRequestType {

    @CCD(label = "New Request")
    @JsonProperty("newRequest")
    NEW_REQUEST("newRequest"),

    @CCD(label = "Reply to Tribunal Query")
    @JsonProperty("replyToTribunalQuery")
    REPLY_TO_TRIBUNAL_QUERY("replyToTribunalQuery"),

    @CCD(label = "Review Tribunal Reply")
    @JsonProperty("reviewTribunalReply")
    REVIEW_TRIBUNAL_REPLY("reviewTribunalReply");

    private final String value;

    TribunalRequestType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}