package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.ccd.domain.AdjournCaseNextHearingDateOrPeriod.PROVIDE_DATE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.AdjournCaseNextHearingDateOrPeriod.PROVIDE_PERIOD;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AdjournCaseNextHearingDateOrPeriodTest {

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectDateOrPeriodValue(String expected, AdjournCaseNextHearingDateOrPeriod AdjournCaseNextHearingDateOrPeriod) {
        assertThat(AdjournCaseNextHearingDateOrPeriod).hasToString(expected);
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectDateOrPeriodFromCcdDefinition(String ccdDefinition, AdjournCaseNextHearingDateOrPeriod expected) {
        assertThat(AdjournCaseNextHearingDateOrPeriod.getDateOrPeriodByCcdDefinition(ccdDefinition)).isEqualTo(expected);
    }

    private static Stream<Arguments> argumentsProvider() {
        return Stream.of(
            Arguments.of("providePeriod", PROVIDE_PERIOD),
            Arguments.of("provideDate", PROVIDE_DATE)
        );
    }
}