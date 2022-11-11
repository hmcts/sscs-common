package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.ccd.domain.AdjournCaseNextHearingDurationType.NON_STANDARD;
import static uk.gov.hmcts.reform.sscs.ccd.domain.AdjournCaseNextHearingDurationType.STANDARD;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AdjournCaseNextHearingDurationTypeTest {

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectDurationTypeValue(String expected, AdjournCaseNextHearingDurationType adjournCaseNextHearingDurationType) {
        assertThat(adjournCaseNextHearingDurationType).hasToString(expected);
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectDurationTypeFromCcdDefinition(String ccdDefinition, AdjournCaseNextHearingDurationType expected) {
        assertThat(AdjournCaseNextHearingDurationType.getDurationTypeByCcdDefinition(ccdDefinition)).isEqualTo(expected);
    }

    private static Stream<Arguments> argumentsProvider() {
        return Stream.of(
            Arguments.of("nonStandardTimeSlot", NON_STANDARD),
            Arguments.of("standardTimeSlot", STANDARD)
        );
    }
}