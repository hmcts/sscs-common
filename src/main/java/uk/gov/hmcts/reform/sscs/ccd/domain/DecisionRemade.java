package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DecisionRemade {
    REMADE("Remade");

    private final String state;

    @Override
    @JsonValue
    public String toString() {
        return state;
    }
}
