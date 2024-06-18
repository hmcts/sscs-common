package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminCorrectionType implements CcdCallbackMap {
    BODY("bodyCorrection", "Body correction - Send to judge", EventType.ADMIN_CORRECTION_BODY, "Correction Sent to Judge", "Correction Sent to Judge", null, InterlocReviewState.REVIEW_BY_JUDGE, InterlocReferralReason.REVIEW_CORRECTION_REQUEST, false),
    HEADER("headerCorrection", "Header correction", EventType.ADMIN_CORRECTION_HEADER, "Admin correction header", "Admin correction header", DwpState.CORRECTED_DECISION_NOTICE_ISSUED, InterlocReviewState.NONE, null, false);

    private final String ccdDefinition;
    private final String descriptionEn;
    private final EventType callbackEvent;
    private final String callbackSummary;
    private final String callbackDescription;
    private final DwpState postCallbackDwpState;
    private final InterlocReviewState postCallbackInterlocState;
    private final InterlocReferralReason postCallbackInterlocReason;

    private final Boolean clearPostHearingFields;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}
