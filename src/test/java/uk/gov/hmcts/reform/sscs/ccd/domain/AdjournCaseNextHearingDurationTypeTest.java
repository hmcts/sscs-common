package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AdjournCaseNextHearingDurationTypeTest {

    @ParameterizedTest
    @CsvSource(value = {
        "nonStandardTimeSlot,NON_STANDARD",
        "standardTimeSlot,STANDARD"
    })
    void shouldReturnCorrectDurationTypeValue(String expected, AdjournCaseNextHearingDurationType adjournCaseNextHearingDurationType) {
        assertThat(adjournCaseNextHearingDurationType).hasToString(expected);
    }

}