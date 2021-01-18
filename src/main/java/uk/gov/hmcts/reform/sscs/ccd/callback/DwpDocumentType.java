package uk.gov.hmcts.reform.sscs.ccd.callback;

import lombok.Getter;

@Getter
public enum DwpDocumentType {
    TL1_FORM("tl1Form", "TL1 Form"),
    UCB("ucb", "Unacceptable customer behaviour document"),
    APPENDIX_12("appendix12", "Appendix 12 document");

    private String value;
    private String label;

    DwpDocumentType(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
