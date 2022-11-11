package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AdjournCaseNextHearingDurationUnitsTest {

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectDurationUnitsValue(String expected, AdjournCaseNextHearingDurationUnits adjournCaseNextHearingDurationUnits) {
        assertThat(adjournCaseNextHearingDurationUnits).hasToString(expected);
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectDurationUnitsFromCcdDefinition(String ccdDefinition, AdjournCaseNextHearingDurationUnits expected) {
        assertThat(AdjournCaseNextHearingDurationUnits.getDurationUnitsByCcdDefinition(ccdDefinition)).isEqualTo(expected);
    }

    private static Stream<Arguments> argumentsProvider() {
        return Stream.of(
            Arguments.of("minutes", AdjournCaseNextHearingDurationUnits.MINUTES),
            Arguments.of("sessions", AdjournCaseNextHearingDurationUnits.SESSIONS)
        );
    }

}