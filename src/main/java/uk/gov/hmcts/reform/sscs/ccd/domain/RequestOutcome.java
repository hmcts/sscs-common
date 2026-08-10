package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_requestOutcomes", generate = true)
public enum RequestOutcome {

    @CCD(label = "In progress")
    @JsonProperty("inProgress")
    IN_PROGRESS("inProgress"),
    @CCD(label = "Granted")
    @JsonProperty("granted")
    GRANTED("granted"),
    @CCD(label = "Refused")
    @JsonProperty("refused")
    REFUSED("refused");

    private final String value;

    RequestOutcome(String value) {
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
