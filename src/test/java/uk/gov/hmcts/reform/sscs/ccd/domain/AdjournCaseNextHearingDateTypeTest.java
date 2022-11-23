package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AdjournCaseNextHearingDateTypeTest {

    @ParameterizedTest
    @CsvSource(value = {
        "dateToBeFixed,DATE_TO_BE_FIXED",
        "firstAvailableDateAfter,FIRST_AVAILABLE_DATE_AFTER",
        "firstAvailableDate,FIRST_AVAILABLE_DATE",
    })
    void shouldReturnCorrectDateTypeValue(String expected, AdjournCaseNextHearingDateType adjournCaseNextHearingDateType) {
        assertThat(adjournCaseNextHearingDateType).hasToString(expected);
    }

}