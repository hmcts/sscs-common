package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_actionSendToFirstTier", generate = true)
@Getter
@AllArgsConstructor
public enum SendToFirstTierActions implements CcdCallbackMap {
    @CCD(label = "Remade")
    DECISION_REMADE("remade", "Decision remade by UT", EventType.UPPER_TRIBUNAL_DECISION, "Decision remade by UT", "Decision remade by UT"),
    @CCD(label = "Refused")
    DECISION_REFUSED("refused", "Decision refused by UT", EventType.UPPER_TRIBUNAL_DECISION, "Decision refused by UT", "Decision refused by UT"),
    @CCD(label = "Remit from UT")
    DECISION_REMITTED("remitted", "Remit from UT", EventType.UPPER_TRIBUNAL_DECISION, "Remit from UT", "Remit from UT");

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