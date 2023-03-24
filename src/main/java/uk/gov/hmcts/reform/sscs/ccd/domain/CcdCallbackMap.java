package uk.gov.hmcts.reform.sscs.ccd.domain;

public interface CcdCallbackMap {
    String getCcdDefinition();

    EventType getCallbackEvent();

    String getCallbackSummary();

    String getCallbackDescription();

    State getPostCallbackState();

    DwpState getPostCallbackDwpState();

    InterlocReviewState getPostCallbackInterlocState();

    InterlocReferralReason getPostCallbackInterlocReason();
}
