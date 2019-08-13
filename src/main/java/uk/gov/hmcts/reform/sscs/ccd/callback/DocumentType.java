package uk.gov.hmcts.reform.sscs.ccd.callback;

import lombok.Getter;

@Getter
public enum DocumentType {

    OTHER_EVIDENCE("Other evidence"),
    OTHER_DOCUMENT("Other document"),
    DWP_RESPONSE("DWP response"),
    DECISION_NOTICE("Decision Notice"),
    DIRECTION_NOTICE("Direction Notice"),
    DL6("dl6"),
    DL16("dl16"),
    APPELLANT_EVIDENCE("appellantEvidence"),
    REPRESENTATIVE_EVIDENCE("representativeEvidence"),
    DWP_EVIDENCE("dwpEvidence"),
    AT38("at38"),
    SSCS1("sscs1");

    private String value;

    DocumentType(String value) {
        this.value = value;
    }
}