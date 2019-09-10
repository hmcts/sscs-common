package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum State {

    APPEAL_CREATED("appealCreated"),
    TEST_CREATE("testCreate"),
    INCOMPLETE_APPLICATION("incompleteApplication"),
    INTERLOCUTORY_REVIEW_STATE("interlocutoryReviewState"),
    INCOMPLETE_APPLICATION_VOID_STATE("incompleteApplicationVoidState"),
    INCOMPLATE_APPLICATION_INFORMATION_REQUESTED("incompleteApplicationInformationReqsted"),
    VOID_STATE("voidState"),
    WITHDRAWN_REVISED_STRUCK_OUT_LAPSED_STATE("withdrawnRevisedStruckOutLapsedState"),
    DORMANT_APPEAL_STATE("dormantAppealState"),
    VALID_APPEAL("validAppeal"),
    RESPONSE_RECEIVED("responseReceived"),
    READY_TO_LIST("readyToList"),
    WITH_DWP("withDwp"),

    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String id;

    State(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
