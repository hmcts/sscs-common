package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static uk.gov.hmcts.reform.sscs.ccd.domain.InterlocReviewState.REVIEW_BY_JUDGE;

@Getter
@AllArgsConstructor
public enum SendToFirstTierActions implements CcdCallbackMap {
    DECISION_REMADE("remade", "Decision remade by UT", EventType.UPPER_TRIBUNAL_DECISION, "Decision remade by UT", "Decision remade by UT"),
    DECISION_REFUSED("refused", "Decision refused by UT", EventType.UPPER_TRIBUNAL_DECISION, "Decision refused by UT", "Decision refused by UT"),
    DECISION_REMITTED("remitted", "Remit from UT", EventType.UPPER_TRIBUNAL_DECISION, "Remit from UT", "Remit from UT");

    private final String ccdDefinition;
    private final String descriptionEn;
    private final EventType callbackEvent;
    private final String callbackSummary;
    private final String callbackDescription;
    private final DwpState postCallbackDwpState;
    private final InterlocReviewState postCallbackInterlocState;
    private final InterlocReferralReason postCallbackInterlocReason = null;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}