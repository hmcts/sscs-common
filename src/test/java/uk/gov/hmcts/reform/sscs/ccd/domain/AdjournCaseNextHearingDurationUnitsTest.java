package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AdjournCaseNextHearingDurationUnitsTest {

    @ParameterizedTest
    @CsvSource(value = {
        "minutes,MINUTES",
        "sessions,SESSIONS",
    })
    void shouldReturnCorrectDurationUnitsValue(String expected, AdjournCaseNextHearingDurationUnits adjournCaseNextHearingDurationUnits) {
        assertThat(adjournCaseNextHearingDurationUnits).hasToString(expected);
    }

}