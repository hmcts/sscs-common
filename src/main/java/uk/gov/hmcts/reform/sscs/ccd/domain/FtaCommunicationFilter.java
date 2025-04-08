package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FtaCommunicationFilter {

    @JsonProperty("awaitingInfoFromFta")
    AWAITING_INFO_FROM_FTA("awaitingInfoFromFta"),
    
    @JsonProperty("infoProvidedByFta")
    INFO_PROVIDED_BY_FTA("infoProvidedByFta"),
    
    @JsonProperty("infoRequestFromFta")
    INFO_REQUEST_FROM_FTA("infoRequestFromFta");

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
