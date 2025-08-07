package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class CaseOutcomeMapTest {

    @ParameterizedTest
    @EnumSource(CaseOutcomeMap.class)
    void shouldReturnCorrectCodeForEachOutcomeKey(CaseOutcomeMap mapping) {
        String result = CaseOutcomeMap.getCaseOutcomeByOutcome(mapping.getOutcomeKey());
        assertThat(result).isEqualTo(mapping.getCaseOutcomeCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalidKey"})
    @NullAndEmptySource
    void shouldReturnNullForInvalidOrNullOutcomeKeys(String invalidKey) {
        String result = CaseOutcomeMap.getCaseOutcomeByOutcome(invalidKey);
        assertThat(result).isNull();
    }
}