package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TribunalCommunicationFilter {

    @JsonProperty("awaitinginfofromtribunal")
    AWAITING_INFO_FROM_TRIBUNAL("Awaiting info from tribunal"),

    @JsonProperty("infoprovidedbytribunal")
    INFO_PROVIDED_BY_TRIBUNAL("Info Provided by tribunal"),

    @JsonProperty("inforequestfromtribunal")
    INFO_REQUEST_FROM_TRIBUNAL("Info Request from FTA");

    private final String value;

    TribunalCommunicationFilter(String value) {
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