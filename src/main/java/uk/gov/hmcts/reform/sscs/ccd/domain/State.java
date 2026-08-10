package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@Getter
@RequiredArgsConstructor
public enum State {
    @CCD(
            label = "Appeal Created",
            hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}",
            description = "Case created on CCD"
    )
    APPEAL_CREATED("appealCreated"),
    @CCD(
            label = "Await Confidentiality Requirements",
            hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}"
    )
    AWAIT_CONFIDENTIALITY_REQUIREMENTS("awaitConfidentialityRequirements"),
    @CCD(label = "Await Other Party Data", hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}")
    AWAIT_OTHER_PARTY_DATA("awaitOtherPartyData"),
    @CCD(
            label = "Closed",
            hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}",
            description = "6 months after the case has an outcome"
    )
    CLOSED("closed"),
    @CCD(
            label = "Dormant",
            hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}",
            description = "Cases that have had a decision made"
    )
    DORMANT_APPEAL_STATE("dormantAppealState"),
    @CCD(
            label = "Draft",
            hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}",
            description = "Draft cases"
    )
    DRAFT("draft"),
    @CCD(
            label = "Draft Archived",
            hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}",
            description = "Draft cases that are archived"
    )
    DRAFT_ARCHIVED("draftArchived"),
    @CCD(
            label = "Handling Error",
            hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}",
            description = "Issue with List assist Hearing"
    )
    HANDLING_ERROR("handlingError"),
    @CCD(
            label = "Hearing",
            hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}",
            description = "A hearing has been booked and / or is in progress"
    )
    HEARING("hearing"),
    @CCD(
            label = "Incomplete Application",
            hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}",
            description = "An application missing information"
    )
    INCOMPLETE_APPLICATION("incompleteApplication"),
    @CCD(
            label = "Information requested",
            hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}",
            description = "Cases where information has been requested"
    )
    INCOMPLETE_APPLICATION_INFORMATION_REQUESTED("incompleteApplicationInformationReqsted"),
    @CCD(label = "Interlocutory Review - Pre-Valid", hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}")
    INTERLOCUTORY_REVIEW_STATE("interlocutoryReviewState"),
    @CCD(label = "Listing Error", hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}")
    LISTING_ERROR("listingError"),
    @CCD(label = "Not listable", hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}")
    NOT_LISTABLE("notListable"),
    @CCD(
            label = "Post Hearing",
            hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}",
            description = "Issue with List assist Hearing"
    )
    POST_HEARING("postHearing"),
    @CCD(
            label = "Ready to list",
            hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}",
            description = "Appeal is ready for scheduling and listing"
    )
    READY_TO_LIST("readyToList"),
    @CCD(
            label = "Response received",
            hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}",
            description = "Response from FTA received"
    )
    RESPONSE_RECEIVED("responseReceived"),
    @CCD(
            label = "Valid Appeal",
            hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}",
            description = "A fully valid appeal"
    )
    VALID_APPEAL("validAppeal"),
    @CCD(
            label = "Void cases",
            hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}",
            description = "Cases that have been voided"
    )
    VOID_STATE("voidState"),
    WITHDRAWN_REVISED_STRUCK_OUT_LAPSED_STATE("withdrawnRevisedStruckOutLapsedState"),
    @CCD(
            label = "With FTA",
            hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}",
            description = "Cases with FTA"
    )
    WITH_DWP("withDwp"),
    @CCD(
            label = "With Upper Tribunal",
            hint = "# ${[CASE_REFERENCE]}: ${appeal.appellant.name.lastName}",
            description = "Cases with Upper Tribunal decision"
    )
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
