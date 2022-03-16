package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingPriorityType {
    HIGH("high", "High Priority"),
    NORMAL("normal", "Normal Priority");

    private final String type;
    private final String description;

    @JsonValue
    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
