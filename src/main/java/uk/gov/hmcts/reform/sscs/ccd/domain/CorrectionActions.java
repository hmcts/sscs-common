package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.CORRECTION_GRANTED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.CORRECTION_REFUSED;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CorrectionActions implements CcdCallbackMap {
    GRANT("grant","Grant Correction Application", CORRECTION_GRANTED, "Correction application granted", "Correction application granted", DwpStates.CORRECTION_GRANTED, InterlocReviewState.NONE),
    REFUSE("refuse","Refuse Correction Application", CORRECTION_REFUSED, "Correction application Refused", "Correction application Refused", DwpStates.CORRECTION_REFUSED, InterlocReviewState.NONE);

    private final String ccdDefinition;
    private final String descriptionEn;
    private final EventType callbackEvent;
    private final String callbackSummary;
    private final String callbackDescription;
    private final DwpStates postCallbackDwpState;
    private final InterlocReviewState postCallbackInterlocState;
    private final InterlocReferralReason postCallbackInterlocReason = null;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}
