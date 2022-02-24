package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.isNull;

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

    public static YesNo isYesOrNo(boolean isYes) {
        return isYes ? YES : NO;
    }

    public static YesNo isYesOrNo(String yesNo) {
        return isYesOrNo(isYes(yesNo));
    }

    public static boolean isNoOrNull(YesNo yesNo) {
        return isNull(yesNo) || NO.equals(yesNo);
    }

    public static boolean isNoOrNull(String yesNo) {
        return isNull(yesNo) || NO.getValue().equals(yesNo);
    }

    @Override
    public String toString() {
        return value;
    }
}
