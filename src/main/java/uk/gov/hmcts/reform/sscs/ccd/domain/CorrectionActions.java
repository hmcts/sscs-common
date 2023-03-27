package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.CORRECTION_GRANTED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.CORRECTION_REFUSED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.CORRECTION_SEND_TO_JUDGE;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CorrectionActions implements CcdCallbackMap {
    GRANT("grant","Grant Correction Application", CORRECTION_GRANTED, "Correction application granted", "Correction application granted", DwpState.CORRECTED_DECISION_NOTICE_ISSUED, InterlocReviewState.NONE),
    REFUSE("refuse","Refuse Correction Application", CORRECTION_REFUSED, "Correction application Refused", "Correction application Refused", null, null),
    SEND_TO_JUDGE("sendToJudge","Send Correction Application to Judge", CORRECTION_SEND_TO_JUDGE, "Correction application send to Judge (Admin)", "Correction application send to Judge (Admin)", null, null);

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
