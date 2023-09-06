package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RemitFromUpperTribunalActions implements CcdCallbackMap {

    REMITTED_FROM_UPPER_TRIBUNAL("remitted", "Remitted from UT", EventType.UPPER_TRIBUNAL_DECISION_REMITTED, "Remitted from UT", "Remitted from UT");

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
