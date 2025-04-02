package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TribunalCommunicationFilter {

    @JsonProperty("newTribunalFilter")
    NEW_TRIBUNAL_FILTER("newTribunalFilter"),
    
    @JsonProperty("newFtaFilter")
    NEW_FTA_FILTER("newFtaFilter"),
    
    @JsonProperty("abilityToSearchByDate")
    SEARCH_BY_DATE("abilityToSearchByDate");

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
