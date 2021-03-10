package uk.gov.hmcts.reform.sscs.ccd.callback;

import lombok.Getter;

@Getter
public enum DocumentType {

    OTHER_EVIDENCE("Other evidence"),
    OTHER_DOCUMENT("Other document"),
    DWP_RESPONSE("DWP response"),
    DECISION_NOTICE("Decision Notice", "Decision Notice"),
    DIRECTION_NOTICE("Direction Notice", "Directions Notice"),
    DL6("dl6", "DL6"),
    DL16("dl16", "DL16"),
    APPELLANT_EVIDENCE("appellantEvidence", "Appellant evidence"),
    REPRESENTATIVE_EVIDENCE("representativeEvidence", "Representative evidence"),
    DWP_EVIDENCE("dwpEvidence", "DWP evidence"),
    AT38("at38", "AT38"),
    SSCS1("sscs1", "SSCS1"),
    DRAFT_DECISION_NOTICE("draftDecisionNotice", "Draft Decision Notice"),
    FINAL_DECISION_NOTICE("finalDecisionNotice", "Final Decision Notice"),
    DRAFT_ADJOURNMENT_NOTICE("draftAdjournmentNotice", "Draft Adjournment Notice"),
    ADJOURNMENT_NOTICE("adjournmentNotice", "Adjournment Notice"),
    REINSTATEMENT_REQUEST("reinstatementRequest", "Reinstatement request"),
    CONFIDENTIALITY_REQUEST("confidentialityRequest", "Confidentiality request"),
    JOINT_PARTY_EVIDENCE("jointPartyEvidence", "Joint party evidence"),
    URGENT_HEARING_REQUEST("urgentHearingRequest", "Urgent hearing request"),
    TL1_FORM("tl1Form", "TL1 Form"),
    AUDIO_DOCUMENT("audioDocument", "Audio document"),
    VIDEO_DOCUMENT("videoDocument", "Video document"),
    AUDIO_VIDEO_EVIDENCE_DIRECTION_NOTICE("audioVideoEvidenceDirectionNotice", "Audio/Video evidence direction notice");

    private final String value;
    private String label;

    public static DocumentType fromValue(String text) {

        for (DocumentType documentType : DocumentType.values()) {
            if (documentType.getValue() != null && documentType.getValue().equalsIgnoreCase(text)) {
                return documentType;
            }
        }
        return null;
    }

    DocumentType(String value) {
        this.value = value;
    }

    DocumentType(String value, String label) {
        this.value = value;
        this.label = label;
    }

}
