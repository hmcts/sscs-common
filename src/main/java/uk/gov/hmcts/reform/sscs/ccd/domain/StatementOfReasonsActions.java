package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SOR_EXTEND_TIME;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SOR_REFUSED;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatementOfReasonsActions implements CcdCallbackMap {
    GRANT("extendTime","Grant Statement of Reasons Application", SOR_EXTEND_TIME, "Statement of reasons - Extend time and send to hearing Judge", "Statement of reasons - Extend time and send to hearing Judge", null, null),
    REFUSE("refuse","Refuse Statement of Reasons Application", SOR_REFUSED, "Statement of reasons Refuse to extend time", "Statement of reasons Refuse to extend time", DwpState.STATEMENT_OF_REASONS_REFUSED, InterlocReviewState.NONE);

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
