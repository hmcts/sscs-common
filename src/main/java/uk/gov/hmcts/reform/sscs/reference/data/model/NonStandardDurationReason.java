package uk.gov.hmcts.reform.sscs.reference.data.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NonStandardDurationReason {

    TIME_FOR_INTERPRETATION("timeForInterpretation", "Time for interpretation", "", ""),
    COMPLEX_CASE("complexCase", "Complex Case", "", ""),
    LITIGANT_IN_PERSON("litigantInPerson", "Litigant in person", "", "");

    private final String hmcReference;
    private final String valueEn;
    private final String valueCy;
    private final String serviceCode;

}
