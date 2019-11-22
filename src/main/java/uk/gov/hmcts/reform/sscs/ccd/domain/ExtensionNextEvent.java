package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ExtensionNextEvent {

    @JsonProperty("sendToListing")
    SEND_TO_LISTING("sendToListing"),

    @JsonProperty("sendToValidAppeal")
    SEND_TO_VALID_APPEAL("sendToValidAppeal");

    // needed only for the toString method
    private final String id;

    ExtensionNextEvent(String id) {
        this.id = id;
    }

    // todo: get rid of the need to override toString as READ_ENUMS_USING_TO_STRING is enabled in other projects (i.e. remove READ_ENUMS_USING_TO_STRING)
    @Override
    public String toString() {
        return id;
    }
}
