package uk.gov.hmcts.reform.sscs.ccd.callback;

import lombok.Getter;

@Getter
public enum DocumentType {
    OTHER_DOCUMENT("Other document"), APPELLANT_EVIDENCE("appellantEvidence"),
    REPRESENTATIVE_EVIDENCE("representativeEvidence");
    private String value;

    DocumentType(String value) {
        this.value = value;
    }
}