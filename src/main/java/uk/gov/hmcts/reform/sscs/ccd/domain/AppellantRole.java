package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.Getter;

@Getter
public enum AppellantRole {
    PAYING_PARENT("Paying parent"),
    RECEIVING_PARENT("Receiving parent"),
    OTHER("Other");

    AppellantRole(String name) {
        this.name = name;
    }

    private String name;
}
