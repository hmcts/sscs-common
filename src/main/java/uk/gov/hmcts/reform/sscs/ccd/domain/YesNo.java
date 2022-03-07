package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum YesNo {

    @JsonProperty("Yes")
    YES("Yes", true),
    @JsonEnumDefaultValue
    @JsonProperty("No")
    NO("No", false);

    private final String value;
    private final boolean bool;

    YesNo(String value, boolean bool) {
        this.value = value;
        this.bool = bool;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public boolean toBoolean() {
        return this.bool;
    }

    public static YesNo isYesOrNo(boolean isYes) {
        return isYes ? YES : NO;
    }

    public static YesNo isYesOrNo(String yesNo) {
        return isYesOrNo(isYes(yesNo));
    }

    public static YesNo isYesOrNo(YesNo yesNo) {
        return isYesOrNo(isYes(yesNo));
    }

    public static YesNo isYesOrNoOrNull(String yesNo) {
        return nonNull(yesNo) && !yesNo.trim().isEmpty() ? isYesOrNo(yesNo) : null;
    }

    public static boolean isYes(YesNo yesNo) {
        return YES.equals(yesNo);
    }

    public static boolean isYes(String yesNo) {
        return YES.getValue().equalsIgnoreCase(yesNo);
    }

    public static boolean isNo(YesNo yesNo) {
        return NO.equals(yesNo);
    }

    public static boolean isNo(String yesNo) {
        return NO.getValue().equalsIgnoreCase(yesNo);
    }

    public static boolean isNoOrNull(YesNo yesNo) {
        return isNull(yesNo) || NO.equals(yesNo);
    }

    public static boolean isNoOrNull(String yesNo) {
        return isNull(yesNo) || yesNo.trim().isEmpty() || NO.getValue().equalsIgnoreCase(yesNo);
    }

    public static String yesOrNoToString(String yesNo) {
        return isYesOrNo(yesNo).getValue();
    }

    public static String yesOrNoToString(YesNo yesNo) {
        return isYesOrNo(yesNo).getValue();
    }

    public static String yesOrNoOrNullToString(String yesNo) {
        return nonNull(yesNo) && !yesNo.trim().isEmpty() ? isYesOrNo(yesNo).getValue() : null;
    }

    public static String yesOrNoOrNullToString(YesNo yesNo) {
        return nonNull(yesNo) ? isYesOrNo(yesNo).getValue() : null;
    }

    @Override
    public String toString() {
        return value;
    }
}
