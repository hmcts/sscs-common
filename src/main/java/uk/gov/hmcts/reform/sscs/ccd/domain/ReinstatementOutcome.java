package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReinstatementOutcome {

    @JsonProperty("inProgress")
    IN_PROGRESS("inProgress"),
    @JsonProperty("granted")
    GRANTED("granted"),
    @JsonProperty("refused")
    REFUSED("refused");

    private final String value;

    ReinstatementOutcome(String value) {
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
