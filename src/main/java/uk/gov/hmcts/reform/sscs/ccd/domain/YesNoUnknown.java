package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YesNoUnknown {
    YES("Yes"), NO("No"), UNKNOWN("Unknown");
    private final String value;

    public static boolean isYes(YesNoUnknown yesNo) {
        return YES.equals(yesNo);
    }

    public static boolean isYes(String yesNo) {
        return nonNull(yesNo) && YES.getValue().equalsIgnoreCase(yesNo);
    }

    public static boolean isNo(YesNoUnknown yesNo) {
        return NO.equals(yesNo);
    }

    public static boolean isNo(String yesNo) {
        return nonNull(yesNo) && NO.getValue().equalsIgnoreCase(yesNo);
    }

    public static boolean isNoOrNull(YesNoUnknown yesNo) {
        return isNull(yesNo) || NO.equals(yesNo);
    }

    public static boolean isNoOrNull(String yesNo) {
        return isNull(yesNo) || NO.getValue().equalsIgnoreCase(yesNo);
    }

    public static boolean isNoOrNullOrUnknown(YesNoUnknown yesNo) {
        return isNull(yesNo) || NO.equals(yesNo) || UNKNOWN.equals(yesNo);
    }

    public static boolean isNoOrNullOrUnknown(String yesNo) {
        return isNull(yesNo) || NO.getValue().equalsIgnoreCase(yesNo) || UNKNOWN.getValue().equalsIgnoreCase(yesNo);
    }

    public static boolean isNoOrUnknown(YesNoUnknown yesNo) {
        return nonNull(yesNo) && (NO.equals(yesNo) || UNKNOWN.equals(yesNo));
    }

    public static boolean isNoOrUnknown(String yesNo) {
        return nonNull(yesNo) && (NO.getValue().equalsIgnoreCase(yesNo) || UNKNOWN.getValue().equalsIgnoreCase(yesNo));
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }
}
