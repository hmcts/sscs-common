package uk.gov.hmcts.reform.sscs.ccd.domain;

public enum ProcessRequestAction {

    SEND_TO_JUDGE("sendToJudge"),
    REFUSE("refuse"),
    GRANT("grant"),
    REFUSE_ON_THE_DAY("refuseOnTheDay");

    private String value;

    ProcessRequestAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
