package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.DwpState.CORRECTION_REQUESTED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.DwpState.LIBERTY_TO_APPLY_REQUESTED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.DwpState.PERMISSION_TO_APPEAL_REQUESTED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.DwpState.SET_ASIDE_REQUESTED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.DwpState.STATEMENT_OF_REASONS_REQUESTED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.CORRECTION_REQUEST;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.LIBERTY_TO_APPLY_REQUEST;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.PERMISSION_TO_APPEAL_REQUEST;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SET_ASIDE_REQUEST;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SOR_REQUEST;
import static uk.gov.hmcts.reform.sscs.ccd.domain.InterlocReferralReason.REVIEW_CORRECTION_APPLICATION;
import static uk.gov.hmcts.reform.sscs.ccd.domain.InterlocReferralReason.REVIEW_LIBERTY_TO_APPLY_APPLICATION;
import static uk.gov.hmcts.reform.sscs.ccd.domain.InterlocReferralReason.REVIEW_PERMISSION_TO_APPEAL_APPLICATION;
import static uk.gov.hmcts.reform.sscs.ccd.domain.InterlocReferralReason.REVIEW_SET_ASIDE_APPLICATION;
import static uk.gov.hmcts.reform.sscs.ccd.domain.InterlocReferralReason.STATEMENT_OF_REASONS_APPLICATION;
import static uk.gov.hmcts.reform.sscs.ccd.domain.InterlocReviewState.AWAITING_ADMIN_ACTION;
import static uk.gov.hmcts.reform.sscs.ccd.domain.InterlocReviewState.REVIEW_BY_JUDGE;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostHearingRequestType implements CcdCallbackMap {
    CORRECTION("correction","Correction", CORRECTION_REQUEST, "Correction Request Made", "Correction Request Made", CORRECTION_REQUESTED, AWAITING_ADMIN_ACTION, REVIEW_CORRECTION_APPLICATION),
    LIBERTY_TO_APPLY("libertyToApply","Liberty to Apply", LIBERTY_TO_APPLY_REQUEST, "Liberty to Apply Request Made", "Liberty to Apply Request Made", LIBERTY_TO_APPLY_REQUESTED, AWAITING_ADMIN_ACTION, REVIEW_LIBERTY_TO_APPLY_APPLICATION),
    PERMISSION_TO_APPEAL("permissionToAppeal","Permission to Appeal", PERMISSION_TO_APPEAL_REQUEST, "Permission to Appeal Request Made", "Permission to Appeal Request Made", PERMISSION_TO_APPEAL_REQUESTED, REVIEW_BY_JUDGE, REVIEW_PERMISSION_TO_APPEAL_APPLICATION),
    SET_ASIDE("setAside","Set Aside", SET_ASIDE_REQUEST, "Set Aside Request Made", "Set Aside Request Made", SET_ASIDE_REQUESTED, AWAITING_ADMIN_ACTION, REVIEW_SET_ASIDE_APPLICATION),
    STATEMENT_OF_REASONS("statementOfReasons","Statement of Reasons", SOR_REQUEST, "Statement of Reasons Request Made", "Statement of Reasons Request Made", STATEMENT_OF_REASONS_REQUESTED, AWAITING_ADMIN_ACTION, STATEMENT_OF_REASONS_APPLICATION);

    private final String ccdDefinition;
    private final String descriptionEn;
    private final EventType callbackEvent;
    private final String callbackSummary;
    private final String callbackDescription;
    private final DwpState postCallbackDwpState;
    private final InterlocReviewState postCallbackInterlocState;
    private final InterlocReferralReason postCallbackInterlocReason;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}
