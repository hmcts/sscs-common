package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.DwpState.STATEMENT_OF_REASONS_ISSUED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SOR_WRITE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.InterlocReviewState.NONE;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WriteStatementOfReasons implements CcdCallbackMap {
    IN_TIME("inTime","In time", SOR_WRITE, "Post hearing application - SOR written", "Post hearing application - SOR written", STATEMENT_OF_REASONS_ISSUED, NONE);

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
