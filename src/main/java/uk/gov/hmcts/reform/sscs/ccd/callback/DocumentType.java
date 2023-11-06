package uk.gov.hmcts.reform.sscs.ccd.callback;

import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DocumentType {
    ADJOURNMENT_NOTICE("adjournmentNotice", "Adjournment Notice"),
    APPELLANT_EVIDENCE("appellantEvidence", "Appellant evidence"),
    AT38("at38", "AT38"),
    AUDIO_DOCUMENT("audioDocument", "Audio document"),
    AUDIO_VIDEO_EVIDENCE_DIRECTION_NOTICE("audioVideoEvidenceDirectionNotice", "Audio/Video evidence direction notice"),
    CONFIDENTIALITY_REQUEST("confidentialityRequest", "Confidentiality request"),
    CORRECTED_DECISION_NOTICE("correctedDecisionNotice", "Corrected decision notice"),
    CORRECTION_APPLICATION("correctionApplication", "Correction Application"),
    DRAFT_CORRECTED_NOTICE("draftCorrectedNotice", "Draft Corrected Final Decision Notice"),
    CORRECTION_GRANTED("correctionGranted", "Corrected Final Decision Notice"),
    CORRECTION_REFUSED("correctionRefused", "Correction Refused Decision Notice"),
    DECISION_NOTICE("Decision Notice", "Decision Notice"),
    DIRECTION_NOTICE("Direction Notice", "Directions Notice"),
    DL16("dl16", "DL16"),
    DL6("dl6", "DL6"),
    DRAFT_ADJOURNMENT_NOTICE("draftAdjournmentNotice", "Draft Adjournment Notice"),
    DRAFT_DECISION_NOTICE("draftDecisionNotice", "Draft Decision Notice"),
    DWP_EVIDENCE("dwpEvidence", "FTA evidence"),
    DWP_RESPONSE("DWP response", "FTA response"),
    FINAL_DECISION_NOTICE("finalDecisionNotice", "Final Decision Notice"),
    GENERIC_NOTICE("genericNotice", "Generic Notice"),
    HMCTS_EVIDENCE("hmctsEvidence", "HMCTS evidence"),
    JOINT_PARTY_EVIDENCE("jointPartyEvidence", "Joint party evidence"),
    LIBERTY_TO_APPLY_APPLICATION("libertyToApplyApplication", "Liberty to Apply Application"),
    LIBERTY_TO_APPLY_GRANTED("libertyToApplyGranted", "Liberty to Apply Granted Decision Notice"),
    LIBERTY_TO_APPLY_REFUSED("libertyToApplyRefused", "Liberty to Apply Refused Decision Notice"),
    OTHER_DOCUMENT("Other document"),
    OTHER_EVIDENCE("Other evidence"),
    OTHER_PARTY_EVIDENCE("otherPartyEvidence", "Other party evidence"),
    OTHER_PARTY_HEARING_PREFERENCES("otherPartyHearingPreferences", "Other party hearing preferences"),
    OTHER_PARTY_REPRESENTATIVE_EVIDENCE("otherPartyRepEvidence", "Other party representative evidence"),
    PERMISSION_TO_APPEAL_APPLICATION("permissionToAppealApplication", "Permission to Appeal Application"),
    PERMISSION_TO_APPEAL_GRANTED("permissionToAppealGranted", "Permission to Appeal Granted Decision Notice"),
    PERMISSION_TO_APPEAL_REFUSED("permissionToAppealRefused", "Permission to Appeal Refused Decision Notice"),
    POSTPONEMENT_REQUEST("postponementRequest", "Postponement request"),
    POSTPONEMENT_REQUEST_DIRECTION_NOTICE("postponementRequestDirectionNotice", "Postponement Request direction notice"),
    POST_HEARING_OTHER("postHearingOther", "Post hearing other"),
    REINSTATEMENT_REQUEST("reinstatementRequest", "Reinstatement request"),
    REPRESENTATIVE_EVIDENCE("representativeEvidence", "Representative evidence"),
    RIP1("rip1Document", "RIP 1 document"),
    SET_ASIDE_APPLICATION("setAsideApplication", "Set Aside Application"),
    SET_ASIDE_GRANTED("setAsideGranted", "Set Aside Granted Decision Notice"),
    SET_ASIDE_REFUSED("setAsideRefused", "Set Aside Refused Decision Notice"),
    REVIEW_AND_SET_ASIDE("reviewAndSetAside", "Review granted"),
    SSCS1("sscs1", "SSCS1"),
    STATEMENT_OF_EVIDENCE("statementOfEvidence", "Statement of evidence"),
    STATEMENT_OF_REASONS_GRANTED("statementOfReasonsGranted", "SOR Granted Decision Notice"),
    STATEMENT_OF_REASONS_REFUSED("statementOfReasonsRefused", "SOR Refused Decision Notice"),
    STATEMENT_OF_REASONS("statementOfReasons", "Statement of Reasons"),
    STATEMENT_OF_REASONS_APPLICATION("statementOfReasonsApplication", "Statement of Reasons Application"),
    TL1_FORM("tl1Form", "TL1 Form"),
    UPPER_TRIBUNALS_DECISION_REMADE("upperTribunalsDecisionRemade", "Upper Tribunals Decision Remade Notice"),
    UPPER_TRIBUNALS_DECISION_REFUSED("upperTribunalsDecisionRefused", "Upper Tribunals Decision Refused Notice"),
    UPPER_TRIBUNALS_DECISION_REMITTED("upperTribunalsDecisionRemitted", "Upper Tribunals Decision Remitted Notice"),
    URGENT_HEARING_REQUEST("urgentHearingRequest", "Urgent hearing request"),
    VIDEO_DOCUMENT("videoDocument", "Video document"),
    WITHDRAWAL_REQUEST("withdrawalRequest", "Withdrawal Request");

    private final String value;
    private String label;

    DocumentType(String value) {
        this.value = value;
    }

    public static DocumentType fromValue(String text) {
        return Stream.of(DocumentType.values())
            .filter(type -> type.getValue() != null && type.getValue().equalsIgnoreCase(text))
            .findFirst()
            .orElse(null);
    }

}
