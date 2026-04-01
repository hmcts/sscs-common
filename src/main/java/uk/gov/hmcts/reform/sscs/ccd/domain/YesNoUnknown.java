package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YesNoUnknown {
    YES("Yes"), NO("No"), UNKNOWN("Unknown");
    private final String value;

    @Override
    @JsonValue
    public String toString() {
        return value;
    }
}
