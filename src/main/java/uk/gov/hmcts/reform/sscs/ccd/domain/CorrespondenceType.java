package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.Getter;

@Getter
public enum CorrespondenceType {
    EMAIL("Email"),
    LETTER("Letter"),
    SMS("Sms");

    private String value;

    CorrespondenceType(String value) {
        this.value = value;
    }
}
