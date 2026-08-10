package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_interlocReferralReason", generate = true)
@Getter
@AllArgsConstructor
public enum InterlocReferralReason {
    @CCD(label = "Advice on how to proceed")
    ADVICE_ON_HOW_TO_PROCEED("adviceOnHowToProceed", "Advice on how to proceed"),
    @CCD(label = "Complex Case")
    COMPLEX_CASE("complexCase", "Complex Case"),
    @CCD(label = "Confirm panel composition and listing directions")
    CONFIRM_PANEL_COMPOSITION_AND_LISTING_DIRECTIONS("confirmPanelCompositionAndListingDirections","Confirm panel composition and listing directions"),
    @CCD(label = "Listing directions")
    LISTING_DIRECTIONS("listingDirections", "Listing directions"),
    @CCD(label = "N/A")
    NONE("none", "N/A"),
    @CCD(label = "No MRN")
    NO_MRN("noMrn", "No MRN"),
    @CCD(label = "No response to a direction")
    NO_RESPONSE_TO_DIRECTION("noResponseToDirection", "No response to a direction"),
    @CCD(label = "Other")
    OTHER("other", "Other"),
    OVER_13_MONTHS("over13months", "Over 13 months"),
    OVER_13_MONTHS_AND_GROUNDS_MISSING("over13MonthsAndGroundsMissing", "Grounds for appeal missing"),
    OVER_300_PAGES("over300Pages", "Over 300 pages"),
    PHE_REQUEST("phmeRequest", "PHE request"),
    @CCD(label = "Reject hearing recording request")
    REJECT_HEARING_RECORDING_REQUEST("rejectHearingRecordingRequest", "Reject hearing recording request"),
    @CCD(label = "Review A/V evidence")
    REVIEW_AUDIO_VIDEO_EVIDENCE("reviewAudioVideoEvidence", "Review Audio Video Evidence"),
    @CCD(label = "Review correction request")
    REVIEW_CORRECTION_REQUEST("reviewCorrectionRequest", "Review correction request"),
    @CCD(label = "Review hearing postponement request")
    REVIEW_POSTPONEMENT_REQUEST("reviewPostponementRequest", "Review hearing postponement request"),
    @CCD(label = "Time extension")
    TIME_EXTENSION("timeExtension","Time extension"),
    @CCD(label = "Review set aside application")
    REVIEW_SET_ASIDE_APPLICATION("reviewSetAsideApplication", "Review set aside application"),
    @CCD(label = "Review correction application")
    REVIEW_CORRECTION_APPLICATION("reviewCorrectionApplication", "Review correction application"),
    @CCD(label = "Statement of reasons application")
    STATEMENT_OF_REASONS_APPLICATION("statementOfReasonsApplication", "Statement of reasons application"),
    @CCD(label = "Late statement of reasons application")
    LATE_STATEMENT_OF_REASONS_APPLICATION("lateStatementOfReasonsApplication", "Late statement of reasons application"),
    @CCD(label = "Review permission to appeal application")
    REVIEW_PERMISSION_TO_APPEAL_APPLICATION("reviewPermissionToAppealApplication", "Review permission to appeal application"),
    @CCD(label = "Review liberty to apply application")
    REVIEW_LIBERTY_TO_APPLY_APPLICATION("reviewLibertyToApplyApplication", "Review liberty to apply application"),
    @CCD(label = "Review upper tribunal decision")
    REVIEW_UPPER_TRIBUNAL_DECISION("reviewUpperTribunalDecision", "Review upper tribunal decision"),
    @CCD(label = "Review reinstatement request")
    REVIEW_REINSTATEMENT_REQUEST("reviewReinstatementRequest", "Review reinstatement request"),
    @CCD(label = "UC JP ONLY - Review confidentiality request")
    REVIEW_CONFIDENTIALITY_REQUEST("reviewConfidentialityRequest", "Review confidentiality request"),
    @CCD(label = "Confidentiality")
    CONFIDENTIALITY("confidentiality", "Confidentiality");

    private final String ccdDefinition;
    private final String description;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}
