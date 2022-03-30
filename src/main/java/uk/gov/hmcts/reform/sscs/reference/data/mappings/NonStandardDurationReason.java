package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NonStandardDurationReason {
    TIME_FOR_INTERPRETATION("timeForInterpretation", "Time for interpretation"),
    COMPLEX_CASE("complexCase", "Complex Case"),
    LITIGANT_IN_PERSON("litigantInPerson", "Litigant in person");

    private final String key;
    private final String value;

    public static NonStandardDurationReason getNonStdDurationReasonByValue(String value) {
        for (NonStandardDurationReason nsdr : NonStandardDurationReason.values()) {
            if (nsdr.getValue().equals(value)) {
                return nsdr;
            }
        }
        return null;
    }
}
