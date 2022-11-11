package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AdjournCasePanelMembersExcludedTest {
    
    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectPanelMembersExcludedValue(String expected, AdjournCasePanelMembersExcluded adjournCasePanelMembersExcluded) {
        assertThat(adjournCasePanelMembersExcluded).hasToString(expected);
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectPanelMembersExcludedFromCcdDefinition(String ccdDefinition, AdjournCasePanelMembersExcluded expected) {
        assertThat(AdjournCasePanelMembersExcluded.getPanelMembersExcludedByCcdDefinition(ccdDefinition)).isEqualTo(expected);
    }

    private static Stream<Arguments> argumentsProvider() {
        return Stream.of(
            Arguments.of("Reserved", AdjournCasePanelMembersExcluded.RESERVED),
            Arguments.of("No", AdjournCasePanelMembersExcluded.NO),
            Arguments.of("Yes", AdjournCasePanelMembersExcluded.YES)
        );
    }
}