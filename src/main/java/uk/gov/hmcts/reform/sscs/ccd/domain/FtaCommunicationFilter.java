package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FtaCommunicationFilter {

    @JsonProperty("awaitingInfoFromTribunal")
    AWAITING_INFO_FROM_TRIBUNAL("awaitingInfoFromTribunal"),

    @JsonProperty("infoProvidedByTribunal")
    INFO_PROVIDED_BY_TRIBUNAL("infoProvidedByTribunal"),

    @JsonProperty("infoRequestFromTribunal")
    INFO_REQUEST_FROM_TRIBUNAL("infoRequestFromTribunal");

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