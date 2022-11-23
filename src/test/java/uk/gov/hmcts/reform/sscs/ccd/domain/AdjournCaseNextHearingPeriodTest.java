package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AdjournCaseNextHearingPeriodTest {

    @ParameterizedTest
    @CsvSource(value = {
        "90,NINETY_DAYS",
        "42,FORTY_TWO_DAYS",
        "28,TWENTY_EIGHT_DAYS",
    })
    void shouldReturnCorrectPeriodValue(String expected, AdjournCaseNextHearingPeriod adjournCaseNextHearingPeriod) {
        assertThat(adjournCaseNextHearingPeriod).hasToString(expected);
    }

}