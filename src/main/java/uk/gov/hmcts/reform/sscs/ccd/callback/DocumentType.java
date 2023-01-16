package uk.gov.hmcts.reform.sscs.ccd.callback;

import lombok.Getter;

@Getter
public enum DocumentType {

    OTHER_EVIDENCE("Other evidence"),
    OTHER_DOCUMENT("Other document"),
    DWP_RESPONSE("DWP response", "FTA response"),
    DECISION_NOTICE("Decision Notice", "Decision Notice"),
    DIRECTION_NOTICE("Direction Notice", "Directions Notice"),
    DL6("dl6", "DL6"),
    DL16("dl16", "DL16"),
    HMCTS_EVIDENCE("hmctsEvidence", "HMCTS evidence"),
    APPELLANT_EVIDENCE("appellantEvidence", "Appellant evidence"),
    REPRESENTATIVE_EVIDENCE("representativeEvidence", "Representative evidence"),
    DWP_EVIDENCE("dwpEvidence", "FTA evidence"),
    AT38("at38", "AT38"),
    SSCS1("sscs1", "SSCS1"),
    DRAFT_DECISION_NOTICE("draftDecisionNotice", "Draft Decision Notice"),
    FINAL_DECISION_NOTICE("finalDecisionNotice", "Final Decision Notice"),
    DRAFT_ADJOURNMENT_NOTICE("draftAdjournmentNotice", "Draft Adjournment Notice"),
    ADJOURNMENT_NOTICE("adjournmentNotice", "Adjournment Notice"),
    GENERIC_NOTICE("genericNotice", "Generic Notice"),
    REINSTATEMENT_REQUEST("reinstatementRequest", "Reinstatement request"),
    CONFIDENTIALITY_REQUEST("confidentialityRequest", "Confidentiality request"),
    JOINT_PARTY_EVIDENCE("jointPartyEvidence", "Joint party evidence"),
    URGENT_HEARING_REQUEST("urgentHearingRequest", "Urgent hearing request"),
    TL1_FORM("tl1Form", "TL1 Form"),
    AUDIO_DOCUMENT("audioDocument", "Audio document"),
    VIDEO_DOCUMENT("videoDocument", "Video document"),
    AUDIO_VIDEO_EVIDENCE_DIRECTION_NOTICE("audioVideoEvidenceDirectionNotice", "Audio/Video evidence direction notice"),
    STATEMENT_OF_EVIDENCE("statementOfEvidence", "Statement of evidence"),
    POSTPONEMENT_REQUEST("postponementRequest", "Postponement request"),
    POSTPONEMENT_REQUEST_DIRECTION_NOTICE("postponementRequestDirectionNotice", "Postponement Request direction notice"),
    OTHER_PARTY_HEARING_PREFERENCES("otherPartyHearingPreferences", "Other party hearing preferences"),
    RIP1("rip1Document", "RIP 1 document"),
    WITHDRAWAL_REQUEST("withdrawalRequest", "Withdrawal Request"),
    OTHER_PARTY_EVIDENCE("otherPartyEvidence", "Other party evidence"),
    OTHER_PARTY_REPRESENTATIVE_EVIDENCE("otherPartyRepEvidence", "Other party representative evidence");

    private final String value;
    private String label;

    DocumentType(String value) {
        this.value = value;
    }

    DocumentType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static DocumentType fromValue(String text) {

        for (DocumentType documentType : DocumentType.values()) {
            if (documentType.getValue() != null && documentType.getValue().equalsIgnoreCase(text)) {
                return documentType;
            }
        }
        return null;
    }

}
