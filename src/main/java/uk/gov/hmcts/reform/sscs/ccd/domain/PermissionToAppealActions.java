package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.*;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_actionPermissionToAppeal", generate = true)
@Getter
@AllArgsConstructor
public enum PermissionToAppealActions implements CcdCallbackMap {
    @CCD(label = "Grant Permission to Appeal Application")
    GRANT("grant","Grant Permission to Appeal Application", PERMISSION_TO_APPEAL_GRANTED, "Permission to Appeal Granted", "Permission to Appeal Granted", DwpState.PERMISSION_TO_APPEAL_GRANTED, InterlocReviewState.NONE),
    @CCD(label = "Refuse Permission to Appeal Application")
    REFUSE("refuse","Refuse Permission to Appeal Application", PERMISSION_TO_APPEAL_REFUSED, "Permission to Appeal Refused", "Permission to Appeal Refused", DwpState.PERMISSION_TO_APPEAL_REFUSED, InterlocReviewState.NONE),
    @CCD(label = "Review decision")
    REVIEW("review", "Review Granted", REVIEW_AND_SET_ASIDE, "Permission to Appeal Review Granted", "Permission to Appeal Review Granted", DwpState.DECISION_REMADE, InterlocReviewState.REVIEW_BY_JUDGE);

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
