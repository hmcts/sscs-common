package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum DocumentTabChoice {
    REGULAR("document"),
    INTERNAL("internalDocument");

    private final String value;

    DocumentTabChoice(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }
}
