package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AudioVideoStatus {

    @JsonProperty("awaitingAction")
    AWAITING_ACTION("awaitingAction"),
    @JsonProperty("included")
    INCLUDED("included"),
    @JsonProperty("excluded")
    EXCLUDED("excluded");

    private final String value;

    AudioVideoStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static AudioVideoStatus fromValue(String text) {

        for (AudioVideoStatus status : AudioVideoStatus.values()) {
            if (text != null && status.getValue() != null && status.getValue().equalsIgnoreCase(text)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
