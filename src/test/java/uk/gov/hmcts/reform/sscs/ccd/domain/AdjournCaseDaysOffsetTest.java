package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AdjournCaseDaysOffsetTest {

    @ParameterizedTest
    @CsvSource(value = {
        "14,FOURTEEN_DAYS",
        "21,TWENTY_ONE_DAYS",
        "28,TWENTY_EIGHT_DAYS",
        "0,OTHER"
    })
    void shouldReturnCorrectDaysOffsetValue(Integer i, String expected, AdjournCaseDaysOffset adjournCaseDaysOffset) {
        assertThat(adjournCaseDaysOffset).hasToString(expected);
    }

}
