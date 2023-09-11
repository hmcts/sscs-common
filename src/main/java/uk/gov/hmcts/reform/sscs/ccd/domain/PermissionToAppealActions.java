package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.*;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PermissionToAppealActions implements CcdCallbackMap {
    GRANT("grant","Grant Permission to Appeal Application", PERMISSION_TO_APPEAL_GRANTED, "Permission to Appeal Granted", "Permission to Appeal Granted", DwpState.PERMISSION_TO_APPEAL_GRANTED),
    REFUSE("refuse","Refuse Permission to Appeal Application", PERMISSION_TO_APPEAL_REFUSED, "Permission to Appeal Refused", "Permission to Appeal Refused", DwpState.PERMISSION_TO_APPEAL_REFUSED),
    REVIEW("review", "Review and Set Aside Application", REVIEW_AND_SET_ASIDE, "PTA Decision Remade", "Permission to Appeal Decision Remade", DwpState.DECISION_REMADE);

    private final String ccdDefinition;
    private final String descriptionEn;
    private final EventType callbackEvent;
    private final String callbackSummary;
    private final String callbackDescription;
    private final DwpState postCallbackDwpState;
    private final InterlocReviewState postCallbackInterlocState = InterlocReviewState.NONE;
    private final InterlocReferralReason postCallbackInterlocReason = null;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}
