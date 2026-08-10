package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_interlocWorkflow", generate = true)
@Getter
@AllArgsConstructor
public enum InterlocReviewState {
    @CCD(label = "Awaiting Admin Action")
    AWAITING_ADMIN_ACTION("awaitingAdminAction"),
    @CCD(label = "Awaiting Information")
    AWAITING_INFORMATION("awaitingInformation"),
    @CCD(label = "HEF Issued")
    HEF_ISSUED("hefIssued"),
    @CCD(label = "N/A")
    NONE("none"),
    @CCD(label = "Review by Judge")
    REVIEW_BY_JUDGE("reviewByJudge"),
    @CCD(label = "Review by TCW")
    REVIEW_BY_TCW("reviewByTcw"),
    @CCD(label = "Welsh Translation")
    WELSH_TRANSLATION("welshTranslation");
    private final String ccdDefinition;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}
