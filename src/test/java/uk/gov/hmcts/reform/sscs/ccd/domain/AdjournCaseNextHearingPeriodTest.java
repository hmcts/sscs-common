package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AdjournCaseNextHearingPeriodTest {

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectPeriodValue(Integer i, String expected, AdjournCaseNextHearingPeriod adjournCaseNextHearingPeriod) {
        assertThat(adjournCaseNextHearingPeriod).hasToString(expected);
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectPeriodFromCcdDefinition(Integer i, String ccdDefinition, AdjournCaseNextHearingPeriod expected) {
        assertThat(AdjournCaseNextHearingPeriod.getPeriodByCcdDefinition(ccdDefinition)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectPeriodFromNumber(Integer number, String s, AdjournCaseNextHearingPeriod expected) {
        assertThat(AdjournCaseNextHearingPeriod.getPeriodByNumber(number)).isEqualTo(expected);
    }

    private static Stream<Arguments> argumentsProvider() {
        return Stream.of(
            Arguments.of(90, "90", AdjournCaseNextHearingPeriod.NINETY_DAYS),
            Arguments.of(42, "42", AdjournCaseNextHearingPeriod.FORTY_TWO_DAYS),
            Arguments.of(28, "28", AdjournCaseNextHearingPeriod.TWENTY_EIGHT_DAYS)
        );
    }
}