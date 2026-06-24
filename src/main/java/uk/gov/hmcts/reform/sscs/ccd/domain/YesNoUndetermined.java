package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YesNoUndetermined {
    YES("Yes"), NO("No"), UNDETERMINED("Undetermined");
    private final String value;

    public static boolean isYes(final YesNoUndetermined yesNo) {
        return YES == yesNo;
    }

    public static boolean isYes(final String yesNo) {
        return nonNull(yesNo) && YES.getValue().equalsIgnoreCase(yesNo);
    }

    public static boolean isNo(final YesNoUndetermined yesNo) {
        return NO == yesNo;
    }

    public static boolean isNo(final String yesNo) {
        return nonNull(yesNo) && NO.getValue().equalsIgnoreCase(yesNo);
    }

    public static boolean isNoOrNull(final YesNoUndetermined yesNo) {
        return isNull(yesNo) || NO == yesNo;
    }

    public static boolean isNoOrNull(final String yesNo) {
        return isNull(yesNo) || NO.getValue().equalsIgnoreCase(yesNo);
    }

    public static boolean isNoOrNullOrUndetermined(final YesNoUndetermined yesNo) {
        return isNull(yesNo) || NO == yesNo || UNDETERMINED == yesNo;
    }

    public static boolean isNoOrNullOrUndetermined(final String yesNo) {
        return isNull(yesNo) || NO.getValue().equalsIgnoreCase(yesNo) || UNDETERMINED.getValue().equalsIgnoreCase(yesNo);
    }

    public static boolean isNoOrUndetermined(final YesNoUndetermined yesNo) {
        return nonNull(yesNo) && (NO == yesNo || UNDETERMINED == yesNo);
    }

    public static boolean isNoOrUndetermined(final String yesNo) {
        return nonNull(yesNo) && (NO.getValue().equalsIgnoreCase(yesNo) || UNDETERMINED.getValue().equalsIgnoreCase(yesNo));
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }
}
