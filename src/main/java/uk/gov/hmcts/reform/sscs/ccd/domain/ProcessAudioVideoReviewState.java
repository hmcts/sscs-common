package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_processAudioVideoReviewState", generate = true)
public enum ProcessAudioVideoReviewState {
    @JsonProperty("awaitingInfo")
    AWAITING_INFORMATION("awaitingInfo"),
    @CCD(label = "Awaiting Admin Action")
    @JsonProperty("awaitingAdminResponse")
    AWAITING_ADMIN_ACTION("awaitingAdminAction"),
    @CCD(label = "Review by Judge")
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
