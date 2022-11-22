package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AdjournCaseNextHearingVenueTest {

    @ParameterizedTest
    @CsvSource(value = {
        "sameVenue,SAME_VENUE",
        "somewhereElse,SOMEWHERE_ELSE"
    })
    void shouldReturnCorrectVenueValue(String expected, AdjournCaseNextHearingVenue adjournCaseNextHearingVenue) {
        assertThat(adjournCaseNextHearingVenue).hasToString(expected);
    }

}
