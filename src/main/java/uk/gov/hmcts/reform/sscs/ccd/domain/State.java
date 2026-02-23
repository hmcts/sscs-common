package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum State {
    APPEAL_CREATED("appealCreated"),
    AWAIT_OTHER_PARTY_DATA("awaitOtherPartyData"),
    CLOSED("closed"),
    DORMANT_APPEAL_STATE("dormantAppealState"),
    DRAFT("draft"),
    DRAFT_ARCHIVED("draftArchived"),
    HANDLING_ERROR("handlingError"),
    HEARING("hearing"),
    INCOMPLETE_APPLICATION("incompleteApplication"),
    INCOMPLETE_APPLICATION_INFORMATION_REQUESTED("incompleteApplicationInformationReqsted"),
    INTERLOCUTORY_REVIEW_STATE("interlocutoryReviewState"),
    LISTING_ERROR("listingError"),
    NOT_LISTABLE("notListable"),
    POST_HEARING("postHearing"),
    READY_TO_LIST("readyToList"),
    RESPONSE_RECEIVED("responseReceived"),
    VALID_APPEAL("validAppeal"),
    VOID_STATE("voidState"),
    WITHDRAWN_REVISED_STRUCK_OUT_LAPSED_STATE("withdrawnRevisedStruckOutLapsedState"),
    WITH_DWP("withDwp"),
    WITH_UT("withUT"),
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    private final String id;

    @JsonValue
    @Override
    public String toString() {
        return id;
    }

    public static State getById(String id) {
        if (isNull(id)) {
            return UNKNOWN;
        }

        return Arrays.stream(values())
            .filter(state -> state.getId().equalsIgnoreCase(id))
            .findFirst()
            .orElse(UNKNOWN);
    }
}
