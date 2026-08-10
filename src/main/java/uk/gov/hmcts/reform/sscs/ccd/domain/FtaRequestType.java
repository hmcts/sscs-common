package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_ftaRequestType", generate = true)
public enum FtaRequestType {

    @CCD(label = "New Request")
    @JsonProperty("newRequest")
    NEW_REQUEST("newRequest"),
    
    @CCD(label = "Reply to FTA Query")
    @JsonProperty("replyToFtaQuery")
    REPLY_TO_FTA_QUERY("replyToFtaQuery"),
    
    @CCD(label = "Review FTA Reply")
    @JsonProperty("reviewFtaReply")
    REVIEW_FTA_REPLY("reviewFtaReply"),
    
    @CCD(label = "Delete a request/reply")
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
