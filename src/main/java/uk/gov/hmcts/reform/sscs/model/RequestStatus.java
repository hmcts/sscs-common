package uk.gov.hmcts.reform.sscs.model;

import lombok.Getter;

@Getter
public enum RequestStatus {
    GRANTED("Granted"),
    REFUSED("Refused"),
    REQUESTED("Requested");

    private final String label;

    RequestStatus(String label) {
        this.label = label;
    }
}
