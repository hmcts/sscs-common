package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.DwpState.SET_ASIDE_REQUESTED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.CORRECTION_REQUEST;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.LIBERTY_TO_APPLY_REQUEST;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.PERMISSION_TO_APPEAL_REQUEST;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SET_ASIDE_REQUEST;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SOR_REQUEST;
import static uk.gov.hmcts.reform.sscs.ccd.domain.InterlocReferralReason.POST_HEARING_REQUEST;
import static uk.gov.hmcts.reform.sscs.ccd.domain.InterlocReviewState.REVIEW_BY_JUDGE;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostHearingRequestType implements CcdCallbackMap {
    SET_ASIDE("setAside","Set Aside", SET_ASIDE_REQUEST, "Set Aside Request Made", "Set Aside Request Made", null, SET_ASIDE_REQUESTED, REVIEW_BY_JUDGE, POST_HEARING_REQUEST),
    CORRECTION("correction","Correction", CORRECTION_REQUEST, "Correction Request Made", "Correction Request Made", null, null, REVIEW_BY_JUDGE, POST_HEARING_REQUEST),
    STATEMENT_OF_REASONS("statementOfReasons","Statement of Reasons", SOR_REQUEST, "Statement of Reasons Request Made", "Statement of Reasons Request Made", null, null, REVIEW_BY_JUDGE, POST_HEARING_REQUEST),
    PERMISSION_TO_APPEAL("permissionToAppeal","Permission to Appeal", PERMISSION_TO_APPEAL_REQUEST, "Permission to Appeal Request Made", "Permission to Appeal Request Made", null,null, REVIEW_BY_JUDGE, POST_HEARING_REQUEST),
    LIBERTY_TO_APPLY("libertyToApply","Liberty to Apply", LIBERTY_TO_APPLY_REQUEST, "Liberty to Apply Request Made", "Liberty to Apply Request Made", null, null, REVIEW_BY_JUDGE, POST_HEARING_REQUEST);

    private final String ccdDefinition;
    private final String descriptionEn;
    private final EventType callbackEvent;
    private final String callbackSummary;
    private final String callbackDescription;
    private final State postCallbackState;
    private final DwpState postCallbackDwpState;
    private final InterlocReviewState postCallbackInterlocState;
    private final InterlocReferralReason postCallbackInterlocReason;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}
