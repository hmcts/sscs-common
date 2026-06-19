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

    public static boolean isYes(final YesNoUnknown yesNo) {
        return YES.equals(yesNo);
    }

    public static boolean isYes(final String yesNo) {
        return nonNull(yesNo) && YES.getValue().equalsIgnoreCase(yesNo);
    }

    public static boolean isNo(final YesNoUnknown yesNo) {
        return NO.equals(yesNo);
    }

    public static boolean isNo(final String yesNo) {
        return nonNull(yesNo) && NO.getValue().equalsIgnoreCase(yesNo);
    }

    public static boolean isNoOrNull(final YesNoUnknown yesNo) {
        return isNull(yesNo) || NO.equals(yesNo);
    }

    public static boolean isNoOrNull(final String yesNo) {
        return isNull(yesNo) || NO.getValue().equalsIgnoreCase(yesNo);
    }

    public static boolean isNoOrNullOrUnknown(final YesNoUnknown yesNo) {
        return isNull(yesNo) || NO.equals(yesNo) || UNKNOWN.equals(yesNo);
    }

    public static boolean isNoOrNullOrUnknown(final String yesNo) {
        return isNull(yesNo) || NO.getValue().equalsIgnoreCase(yesNo) || UNKNOWN.getValue().equalsIgnoreCase(yesNo);
    }

    public static boolean isNoOrUnknown(final YesNoUnknown yesNo) {
        return nonNull(yesNo) && (NO.equals(yesNo) || UNKNOWN.equals(yesNo));
    }

    public static boolean isNoOrUnknown(final String yesNo) {
        return nonNull(yesNo) && (NO.getValue().equalsIgnoreCase(yesNo) || UNKNOWN.getValue().equalsIgnoreCase(yesNo));
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }
}
