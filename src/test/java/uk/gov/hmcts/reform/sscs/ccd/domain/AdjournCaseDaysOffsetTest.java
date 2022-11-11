package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AdjournCaseDaysOffsetTest {

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectDaysOffsetValue(Integer i, String expected, AdjournCaseDaysOffset adjournCaseDaysOffset) {
        assertThat(adjournCaseDaysOffset).hasToString(expected);
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectDaysOffsetFromCcdDefinition(Integer i, String ccdDefinition, AdjournCaseDaysOffset expected) {
        assertThat(AdjournCaseDaysOffset.getDaysOffsetByCcdDefinition(ccdDefinition)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectDaysOffsetFromNumber(Integer number, String s, AdjournCaseDaysOffset expected) {
        assertThat(AdjournCaseDaysOffset.getDaysOffsetByNumber(number)).isEqualTo(expected);
    }

    private static Stream<Arguments> argumentsProvider() {
        return Stream.of(
            Arguments.of(14, "14", AdjournCaseDaysOffset.FOURTEEN_DAYS),
            Arguments.of(21, "21", AdjournCaseDaysOffset.TWENTY_ONE_DAYS),
            Arguments.of(28, "28", AdjournCaseDaysOffset.TWENTY_EIGHT_DAYS),
            Arguments.of(0, "0", AdjournCaseDaysOffset.OTHER)
        );
    }
}
