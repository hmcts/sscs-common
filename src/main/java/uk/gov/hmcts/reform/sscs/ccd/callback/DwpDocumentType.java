package uk.gov.hmcts.reform.sscs.ccd.callback;

import lombok.Getter;

@Getter
public enum DwpDocumentType {
    TL1_FORM("tl1Form", "TL1 Form"),
    UCB("ucb", "Unacceptable customer behaviour document");

    private String value;
    private String label;

    DwpDocumentType(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
