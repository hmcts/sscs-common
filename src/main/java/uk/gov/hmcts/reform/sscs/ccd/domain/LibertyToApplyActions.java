package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.LIBERTY_TO_APPLY_GRANTED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.LIBERTY_TO_APPLY_REFUSED;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_actionLibertyToApply", generate = true)
@Getter
@AllArgsConstructor
public enum LibertyToApplyActions implements CcdCallbackMap {
    @CCD(label = "Grant Liberty to Apply Application")
    GRANT("grant","Grant Liberty to Apply Application", LIBERTY_TO_APPLY_GRANTED, "Liberty to apply application granted", "Liberty to apply application granted", DwpState.LIBERTY_TO_APPLY_GRANTED, InterlocReviewState.AWAITING_ADMIN_ACTION),
    @CCD(label = "Refuse Liberty to Apply Application")
    REFUSE("refuse","Refuse Liberty to Apply Application", LIBERTY_TO_APPLY_REFUSED, "Liberty to apply application Refused", "Liberty to apply application Refused", DwpState.LIBERTY_TO_APPLY_REFUSED, InterlocReviewState.NONE);

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
