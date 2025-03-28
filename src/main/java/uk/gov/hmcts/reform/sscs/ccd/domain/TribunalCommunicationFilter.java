package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TribunalCommunicationFilter {

    @JsonProperty("awaitinginfofromfta")
    AWAITING_INFO_FROM_FTA("Awaiting info from FTA"),

    @JsonProperty("infoprovidedbyfta")
    INFO_PROVIDED_BY_FTA("Info Provided by FTA"),

    @JsonProperty("inforequestfromfta")
    INFO_REQUEST_FROM_FTA("Info Request from FTA");

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