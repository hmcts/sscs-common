package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SET_ASIDE_GRANTED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SET_ASIDE_ISSUE_DIRECTIONS;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SET_ASIDE_REFUSED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SET_ASIDE_REFUSED_SOR;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SetAsideActions implements CcdCallbackMap {
    GRANT("grant","Grant Set Aside Application", SET_ASIDE_GRANTED, "Set aside application granted", "Set aside application granted", null),
    REFUSE("refuse","Refuse Set Aside Application", SET_ASIDE_REFUSED, "Set aside application refused", "Set aside application refused", DwpState.SET_ASIDE_REFUSED),
    ISSUE_DIRECTIONS("issueDirections","IssueDirections", SET_ASIDE_ISSUE_DIRECTIONS, "Set aside application issue directions", "Set aside application issue directions", null),
    REFUSE_SOR("issueDirections","IssueDirections", SET_ASIDE_REFUSED_SOR, "Set aside application refused & request statement of reasons", "Set aside application refused & request statement of reasons", null);

    private final String ccdDefinition;
    private final String descriptionEn;
    private final EventType callbackEvent;
    private final String callbackSummary;
    private final String callbackDescription;
    private final DwpState postCallbackDwpState;
    private final InterlocReviewState postCallbackInterlocState = null;
    private final InterlocReferralReason postCallbackInterlocReason = null;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}