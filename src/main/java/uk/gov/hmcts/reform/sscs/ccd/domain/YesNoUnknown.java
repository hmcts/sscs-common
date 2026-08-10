package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_yesNoUnknown", generate = true)
@Getter
@AllArgsConstructor
public enum YesNoUnknown {
    @CCD(label = "Yes")
    YES("Yes"),
    @CCD(label = "No")
    NO("No"),
    @CCD(label = "Unknown")
    UNKNOWN("Unknown");
    private final String value;

    @Override
    @JsonValue
    public String toString() {
        return value;
    }
}
