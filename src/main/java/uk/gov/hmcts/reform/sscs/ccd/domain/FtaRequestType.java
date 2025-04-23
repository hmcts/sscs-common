package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FtaRequestType {

    @JsonProperty("newRequest")
    NEW_REQUEST("newRequest"),
    
    @JsonProperty("replyToFtaQuery")
    REPLY_TO_FTA_QUERY("replyToFtaQuery"),
    
    @JsonProperty("reviewFtaReply")
    REVIEW_FTA_REPLY("reviewFtaReply"),
    
    @JsonProperty("deleteRequestReply")
    DELETE_REQUEST_REPLY("deleteRequestReply");

    private final String value;

    FtaRequestType(String value) {
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
