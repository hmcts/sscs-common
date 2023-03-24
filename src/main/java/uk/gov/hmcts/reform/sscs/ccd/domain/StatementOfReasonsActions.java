package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SOR_EXTEND_TIME;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SOR_ISSUE_DIRECTIONS;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SOR_REFUSED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SOR_WRITE;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatementOfReasonsActions implements CcdCallbackMap {
    GRANT("extendTime","Grant Statement of Reasons Application", SOR_EXTEND_TIME, "Statement of reasons - Extend time and send to hearing Judge", "Statement of reasons - Extend time and send to hearing Judge"),
    REFUSE("refuse","Refuse Statement of Reasons Application", SOR_REFUSED, "Statement of reasons Refuse to extend time", "Statement of reasons Refuse to extend time"),
    ISSUE_DIRECTIONS("issueDirections","Issue directions", SOR_ISSUE_DIRECTIONS, "Statement of reasons Issue directions", "Statement of reasons Issue directions"),
    WRITE("write","Write Statement of Reasons", SOR_WRITE, "Statement of reasons Write Statement of Reasons", "Statement of reasons Write Statement of Reasons");

    private final String ccdDefinition;
    private final String descriptionEn;
    private final EventType callbackEvent;
    private final String callbackSummary;
    private final String callbackDescription;
    private final State postCallbackState = null;
    private final DwpState postCallbackDwpState = null;
    private final InterlocReviewState postCallbackInterlocState = null;
    private final InterlocReferralReason postCallbackInterlocReason = null;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}
