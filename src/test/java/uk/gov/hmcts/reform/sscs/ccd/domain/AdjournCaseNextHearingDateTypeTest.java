package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.ccd.domain.AdjournCaseNextHearingDateType.DATE_TO_BE_FIXED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.AdjournCaseNextHearingDateType.FIRST_AVAILABLE_DATE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.AdjournCaseNextHearingDateType.FIRST_AVAILABLE_DATE_AFTER;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AdjournCaseNextHearingDateTypeTest {

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectDateTypeValue(String expected, AdjournCaseNextHearingDateType adjournCaseNextHearingDateType) {
        assertThat(adjournCaseNextHearingDateType).hasToString(expected);
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectDateTypeFromCcdDefinition(String ccdDefinition, AdjournCaseNextHearingDateType expected) {
        assertThat(AdjournCaseNextHearingDateType.getDateTypeByCcdDefinition(ccdDefinition)).isEqualTo(expected);
    }

    private static Stream<Arguments> argumentsProvider() {
        return Stream.of(
            Arguments.of("dateToBeFixed", DATE_TO_BE_FIXED),
            Arguments.of("firstAvailableDateAfter", FIRST_AVAILABLE_DATE_AFTER),
            Arguments.of("firstAvailableDate", FIRST_AVAILABLE_DATE)
        );
    }
}