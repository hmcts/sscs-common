package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AdjournCaseNextHearingVenueTest {

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectVenueValue(String expected, AdjournCaseNextHearingVenue adjournCaseNextHearingVenue) {
        assertThat(adjournCaseNextHearingVenue).hasToString(expected);
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectTypeOfVenueFromCcdDefinition(String ccdDefinition, AdjournCaseNextHearingVenue expected) {
        assertThat(AdjournCaseNextHearingVenue.getNextHearingVenueByCcdDefinition(ccdDefinition)).isEqualTo(expected);
    }

    private static Stream<Arguments> argumentsProvider() {
        return Stream.of(
            Arguments.of("sameVenue", AdjournCaseNextHearingVenue.SAME_VENUE),
            Arguments.of("somewhereElse", AdjournCaseNextHearingVenue.SOMEWHERE_ELSE)
        );
    }
}
