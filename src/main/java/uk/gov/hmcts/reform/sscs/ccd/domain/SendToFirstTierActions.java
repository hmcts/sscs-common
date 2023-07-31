package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SendToFirstTierActions implements CcdCallbackMap {

    DECISION_REMADE("remade", "Decision remade by UT", EventType.UPPER_TRIBUNAL_DECISION, "Decision remade by UT", "Decision remade by UT"),
    DECISION_REFUSED("refused", "Decision refused by UT", EventType.UPPER_TRIBUNAL_DECISION, "Decision refused by UT", "Decision refused by UT");

    private final String ccdDefinition;
    private final String descriptionEn;
    private final EventType callbackEvent;
    private final String callbackSummary;
    private final String callbackDescription;
    private final DwpState postCallbackDwpState = null;
    private final InterlocReviewState postCallbackInterlocState = null;
    private final InterlocReferralReason postCallbackInterlocReason = null;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}