package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NonStdDurationReason {
    TIME_FOR_INTERPRETATION("timeForInterpretation", "Time for interpretation"),
    COMPLEX_CASE("complexCase", "Complex Case"),
    LITIGANT_IN_PERSON("litigantInPerson", "Litigant in person");

    private final String key;
    private final String value;

    public static NonStdDurationReason getNonStdDurationReasonByValue(String value) {
        for (NonStdDurationReason nsdr : NonStdDurationReason.values()) {
            if (nsdr.getValue().equals(value)) {
                return nsdr;
            }
        }
        return null;
    }
}
