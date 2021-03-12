package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProcessAudioVideoReviewState {
    @JsonProperty("awaiting-info")
    AWAITING_INFORMATION("awaiting-info"),
    @JsonProperty("awaiting-admin-response")
    AWAITING_ADMIN_RESPONSE("awaiting-admin-response"),
    @JsonProperty("review-by-judg")
    REVIEW_BY_JUDGE("review-by-judg"),
    @JsonProperty("clear")
    CLEAR_INTERLOC_REVIEW_STATE("clear");

    private final String value;

    ProcessAudioVideoReviewState(String value) {
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
