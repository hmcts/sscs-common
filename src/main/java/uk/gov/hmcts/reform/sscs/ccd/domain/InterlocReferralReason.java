package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InterlocReferralReason {

    ADVICE_ON_HOW_TO_PROCEED("adviceOnHowToProceed", "Advice on how to proceed"),
    COMPLEX_CASE("complexCase", "Complex Case"),
    CONFIRM_PANEL_COMPOSITION_AND_LISTING_DIRECTIONS("confirmPanelCompositionAndListingDirections","Confirm panel composition and listing directions"),
    LISTING_DIRECTIONS("listingDirections", "Listing directions"),
    NONE("none", "N/A"),
    NO_MRN("noMrn", "No MRN"),
    NO_RESPONSE_TO_DIRECTION("noResponseToDirection", "No response to a direction"),
    OTHER("other", "Other"),
    OVER_13_MONTHS("over13months", "Over 13 months"),
    OVER_13_MONTHS_AND_GROUNDS_MISSING("over13MonthsAndGroundsMissing", "Grounds for appeal missing"),
    OVER_300_PAGES("over300Pages", "Over 300 pages"),
    PHE_REQUEST("phmeRequest", "PHE request"),
    POST_HEARING_REQUEST("postHearingRequest", "Post Hearing Request"),
    REJECT_HEARING_RECORDING_REQUEST("rejectHearingRecordingRequest", "Reject hearing recording request"),
    REVIEW_AUDIO_VIDEO_EVIDENCE("reviewAudioVideoEvidence", "Review Audio Video Evidence"),
    REVIEW_POSTPONEMENT_REQUEST("reviewPostponementRequest", "Review hearing postponement request"),
    TIME_EXTENSION("timeExtension","Time extension");

    private final String ccdDefinition;
    private final String description;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}
