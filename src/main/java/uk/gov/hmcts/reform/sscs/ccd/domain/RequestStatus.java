package uk.gov.hmcts.reform.sscs.ccd.domain;

public enum RequestStatus {

    GRANTED("Granted"),
    REFUSED("Refused"),
    REQUESTED("Requested");


    private final String value;

    RequestStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
