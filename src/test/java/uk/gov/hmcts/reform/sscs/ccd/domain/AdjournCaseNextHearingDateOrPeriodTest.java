package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AdjournCaseNextHearingDateOrPeriodTest {

    @ParameterizedTest
    @CsvSource(value = {
        "providePeriod,PROVIDE_PERIOD",
        "provideDate,PROVIDE_DATE"
    })
    void shouldReturnCorrectDateOrPeriodValue(String expected, AdjournCaseNextHearingDateOrPeriod AdjournCaseNextHearingDateOrPeriod) {
        assertThat(AdjournCaseNextHearingDateOrPeriod).hasToString(expected);
    }

}