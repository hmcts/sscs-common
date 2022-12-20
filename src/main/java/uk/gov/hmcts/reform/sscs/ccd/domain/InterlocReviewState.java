package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InterlocReviewState {
    AWAITING_ADMIN_ACTION("awaitingAdminAction"),
    AWAITING_INFORMATION("awaitingInformation"),
    NONE("none"),
    REVIEW_BY_JUDGE("reviewByJudge"),
    REVIEW_BY_TCW("reviewByTcw"),
    WELSH_TRANSLATION("welshTranslation");
    private final String ccdDefinition;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}
