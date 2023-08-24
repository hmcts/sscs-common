package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.BUNDLE_CREATED_FOR_UPPER_TRIBUNAL;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GetFirstTierDocumentsActions implements CcdCallbackMap {

    BUNDLE_CREATED("bundleCreatedForUpperTribunal","Bundle created for Upper Tribunal", BUNDLE_CREATED_FOR_UPPER_TRIBUNAL, "Bundle created for UT", "Bundle created for UT");

    private final String ccdDefinition;
    private final String descriptionEn;
    private final EventType callbackEvent;
    private final String callbackSummary;
    private final String callbackDescription;
    private final DwpState postCallbackDwpState = null;
    private final InterlocReviewState postCallbackInterlocState = null;
    private final InterlocReferralReason postCallbackInterlocReason = null;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}
