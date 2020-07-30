package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum State {

    @JsonProperty("appealCreated")
    APPEAL_CREATED("appealCreated"),

    @JsonProperty("testCreate")
    TEST_CREATE("testCreate"),

    @JsonProperty("incompleteApplication")
    INCOMPLETE_APPLICATION("incompleteApplication"),

    @JsonProperty("interlocutoryReviewState")
    INTERLOCUTORY_REVIEW_STATE("interlocutoryReviewState"),

    @JsonProperty("incompleteApplicationInformationReqsted")
    INCOMPLETE_APPLICATION_INFORMATION_REQUESTED("incompleteApplicationInformationReqsted"),

    @JsonProperty("voidState")
    VOID_STATE("voidState"),

    @JsonProperty("withdrawnRevisedStruckOutLapsedState")
    WITHDRAWN_REVISED_STRUCK_OUT_LAPSED_STATE("withdrawnRevisedStruckOutLapsedState"),

    @JsonProperty("dormantAppealState")
    DORMANT_APPEAL_STATE("dormantAppealState"),

    @JsonProperty("validAppeal")
    VALID_APPEAL("validAppeal"),

    @JsonProperty("responseReceived")
    RESPONSE_RECEIVED("responseReceived"),

    @JsonProperty("readyToList")
    READY_TO_LIST("readyToList"),

    @JsonProperty("withDwp")
    WITH_DWP("withDwp"),

    @JsonProperty("closed")
    CLOSED("closed"),

    @JsonProperty("draft")
    DRAFT("draft"),

    @JsonProperty("draftArchived")
    DRAFT_ARCHIVED("draftArchived"),

    @JsonProperty("hearing")
    HEARING("hearing"),

    @JsonProperty("outcome")
    OUTCOME("outcome"),

    @JsonProperty("pendingAppeal")
    PENDING_APPEAL("pendingAppeal"),

    @JsonProperty("notListable")
    NOT_LISTABLE("notListable"),

    @JsonProperty("unknown")
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String id;

    State(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }

    public static State getById(String id) {
        for (State e : values()) {
            if (e.id.equals(id)) {
                return e;
            }
        }
        return UNKNOWN;
    }
}
