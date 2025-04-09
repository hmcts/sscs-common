package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TribunalCommunicationFilter {

    @JsonProperty("awaitingInfoFromFta")
    AWAITING_INFO_FROM_FTA("awaitingInfoFromFta"),

    @JsonProperty("infoProvidedByFta")
    INFO_PROVIDED_BY_FTA("infoProvidedByFta"),

    @JsonProperty("infoRequestFromFta")
    INFO_REQUEST_FROM_FTA("infoRequestFromFta");

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