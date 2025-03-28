package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FtaCommunicationFilter {

    @JsonProperty("awaitinginfofromtribunal")
    AWAITING_INFO_FROM_TRIBUNAL("Awaiting info from Tribunal"),

    @JsonProperty("infoprovidedbytribunal")
    INFO_PROVIDED_BY_TRIBUNAL("Info Provided by Tribunal"),

    @JsonProperty("inforequestfromtribunal")
    INFO_REQUEST_FROM_TRIBUNAL("Info Request from Tribunal");

    private final String value;

    FtaCommunicationFilter(String value) {
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