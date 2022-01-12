package uk.gov.hmcts.reform.sscs.ccd.callback;

import lombok.Getter;

@Getter
public enum DocumentSubtype {

    DWP_EVIDENCE("FTA evidence");

    private String value;

    DocumentSubtype(String value) {
        this.value = value;
    }
}
