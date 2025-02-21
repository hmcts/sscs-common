package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.Getter;

@Getter
public enum DocumentTabChoice {
    REGULAR("document"),
    INTERNAL("internalDocument");

    private final String value;

    DocumentTabChoice(String value) {
        this.value = value;
    }
}
