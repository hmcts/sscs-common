package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FtaRequestType {

    @JsonProperty("newrequest")
    NEW_REQUEST("New Request"),
    
    @JsonProperty("replyftaquery")
    REPLY_TO_FTA_QUERY("Reply to FTA Query"),
    
    @JsonProperty("reviewftareply")
    REVIEW_FTA_REPLY("Review FTA Reply"),
    
    @JsonProperty("deleterequestorreply")
    DELETE_REQUEST_REPLY("Delete a request/reply");

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
