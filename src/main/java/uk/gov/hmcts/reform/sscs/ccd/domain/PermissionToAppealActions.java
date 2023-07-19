package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.*;

@Getter
@AllArgsConstructor
public enum PermissionToAppealActions implements CcdCallbackMap {
    GRANT("grant","Grant Permission to Appeal Application", PERMISSION_TO_APPEAL_GRANTED, "Permission to Appeal Granted", "Permission to Appeal Granted", DwpState.PERMISSION_TO_APPEAL_GRANTED, InterlocReviewState.NONE),
    REFUSE("refuse","Refuse Permission to Appeal Application", PERMISSION_TO_APPEAL_REFUSED, "Permission to Appeal Refused", "Permission to Appeal Refused", DwpState.PERMISSION_TO_APPEAL_REFUSED, InterlocReviewState.NONE),
    REVIEW("review", "Review and Set Aside Application", SET_ASIDE_REVIEW, "Permission to Appeal Decision Remade", "Permission to Appeal Decision Remade", DwpState.DECISION_REMADE, InterlocReviewState.NONE);

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
