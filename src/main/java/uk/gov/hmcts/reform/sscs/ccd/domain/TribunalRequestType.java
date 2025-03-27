package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TribunalRequestType {

    @JsonProperty("newrequest")
    NEW_REQUEST("New Request"),

    @JsonProperty("replytribunalquery")
    REPLY_TO_FTA_QUERY("Reply to Tribunal Query"),

    @JsonProperty("reviewtribunalreply")
    REVIEW_FTA_REPLY("Review FTA Reply"),

    @JsonProperty("deletemessage")
    DELETE_REQUEST_REPLY("Delete a request/reply");

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