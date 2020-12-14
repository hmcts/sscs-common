package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum YesNo {

    @JsonProperty("Yes")
    YES("Yes"),
    @JsonProperty("No")
    NO("No");

    private final String value;

    YesNo(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public boolean toBoolean() {
        return YES.value.equals(value);
    }

    public static boolean isYes(YesNo yesNo) {
        return YES.equals(yesNo);
    }

    public static boolean isYes(String yesNo) {
        return YES.getValue().equals(yesNo);
    }

    @Override
    public String toString() {
        return value;
    }
}
