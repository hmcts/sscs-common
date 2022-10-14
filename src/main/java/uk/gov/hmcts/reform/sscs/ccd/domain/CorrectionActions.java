package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.CORRECTION_GRANTED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.CORRECTION_REFUSED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.CORRECTION_SEND_TO_JUDGE;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CorrectionActions {
    GRANT("grant","Grant Correction Application", CORRECTION_GRANTED),
    REFUSE("refuse","Refuse Correction Application", CORRECTION_REFUSED),
    SEND_TO_JUDGE("sendToJudge","Send Correction Application to Judge", CORRECTION_SEND_TO_JUDGE);

    private final String ccdDefinition;
    private final String descriptionEn;
    private final EventType callbackEvent;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}
