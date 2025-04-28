package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TribunalRequestType {

    @JsonProperty("newRequest")
    NEW_REQUEST("newRequest"),

    @JsonProperty("replyToTribunalQuery")
    REPLY_TO_TRIBUNAL_QUERY("replyToTribunalQuery"),

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