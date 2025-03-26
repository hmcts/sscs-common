package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TribunalCommunicationFilter {

    @JsonProperty("newtribunalfilter")
    NEW_TRIBUNAL_FILTER("New Tribunal Filter"),
    
    @JsonProperty("newftafilter")
    NEW_FTA_FILTER("New FTA Filter"),
    
    @JsonProperty("abilitytosearchbydate")
    SEARCH_BY_DATE("Ability to search by date");

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
