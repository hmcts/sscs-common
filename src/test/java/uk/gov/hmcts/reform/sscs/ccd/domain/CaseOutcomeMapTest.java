package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class CaseOutcomeMapTest {

    @ParameterizedTest
    @EnumSource(CaseOutcomeMap.class)
    void shouldReturnCorrectCodeForEachOutcomeKey(CaseOutcomeMap mapping) {
        String lookedUpOutcome = CaseOutcomeMap.getCaseOutcomeByOutcome(mapping.getOutcomeKey());
        assertThat(lookedUpOutcome).isEqualTo(mapping.getCaseOutcomeCode());
    }

    @Test
    void shouldThrowExceptionForUnknownOutcomeKey() {
        String invalidKey = "invalidKey";

        assertThatThrownBy(() -> CaseOutcomeMap.getCaseOutcomeByOutcome(invalidKey))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid Outcome: " + invalidKey);
    }
}