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
    CORRECTION_REFUSED("correctionRefused", "Correction refused decision notice"),
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
    LIBERTY_TO_APPLY_GRANTED("libertyToApplyGranted", "Liberty to Apply granted decision notice"),
    LIBERTY_TO_APPLY_REFUSED("libertyToApplyRefused", "Liberty to Apply refused decision notice"),
    OTHER_DOCUMENT("Other document"),
    OTHER_EVIDENCE("Other evidence"),
    OTHER_PARTY_EVIDENCE("otherPartyEvidence", "Other party evidence"),
    OTHER_PARTY_HEARING_PREFERENCES("otherPartyHearingPreferences", "Other party hearing preferences"),
    OTHER_PARTY_REPRESENTATIVE_EVIDENCE("otherPartyRepEvidence", "Other party representative evidence"),
    POSTPONEMENT_REQUEST("postponementRequest", "Postponement request"),
    POSTPONEMENT_REQUEST_DIRECTION_NOTICE("postponementRequestDirectionNotice", "Postponement Request direction notice"),
    REINSTATEMENT_REQUEST("reinstatementRequest", "Reinstatement request"),
    REPRESENTATIVE_EVIDENCE("representativeEvidence", "Representative evidence"),
    RIP1("rip1Document", "RIP 1 document"),
    SET_ASIDE_APPLICATION("setAsideApplication", "Set Aside Application"),
    SET_ASIDE_REFUSED("setAsideRefused", "Set aside refused decision notice"),
    SSCS1("sscs1", "SSCS1"),
    STATEMENT_OF_EVIDENCE("statementOfEvidence", "Statement of evidence"),
    STATEMENT_OF_REASONS_GRANTED("statementOfReasonsGranted", "SOR Granted decision notice"),
    STATEMENT_OF_REASONS_REFUSED("statementOfReasonsRefused", "SOR refused decision notice"),
    STATEMENT_OF_REASONS_APPLICATION("statementOfReasonsApplication", "Statement of Reasons Application"),
    TL1_FORM("tl1Form", "TL1 Form"),
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
