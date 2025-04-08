package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TribunalCommunicationFilter {

    @JsonProperty("provideInfoToTribunal")
    PROVIDE_INFO_TO_TRIBUNAL("provideInfoToTribunal"),
    
    @JsonProperty("awaitingInfoFromTribunal")
    AWAITING_INFO_FROM_TRIBUNAL("awaitingInfoFromTribunal"),
    
    @JsonProperty("infoProvidedFromTribunal")
    INFO_PROVIDED_FROM_TRIBUNAL("infoProvidedFromTribunal");

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
