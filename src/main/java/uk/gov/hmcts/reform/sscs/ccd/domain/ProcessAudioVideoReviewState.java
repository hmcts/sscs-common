package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProcessAudioVideoReviewState {
    @JsonProperty("awaitingInfo")
    AWAITING_INFORMATION("awaitingInfo"),
    @JsonProperty("awaitingAdminResponse")
    AWAITING_ADMIN_RESPONSE("awaitingAdminResponse"),
    @JsonProperty("reviewByJudge")
    REVIEW_BY_JUDGE("reviewByJudge"),
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
