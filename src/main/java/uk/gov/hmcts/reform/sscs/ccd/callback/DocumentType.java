package uk.gov.hmcts.reform.sscs.ccd.callback;

import lombok.Getter;

@Getter
public enum DocumentType {

    OTHER_EVIDENCE("Other evidence"),
    OTHER_DOCUMENT("Other document"),
    DWP_RESPONSE("DWP response"),
    DECISION_NOTICE("Decision Notice"),
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
    REINSTATEMENT_REQUEST("reinstatementRequest", "Reinstatement request");

    private String value;
    private String label;

    DocumentType(String value) {
        this.value = value;
    }

    DocumentType(String value, String label) {
        this.value = value;
        this.label = label;
    }
}