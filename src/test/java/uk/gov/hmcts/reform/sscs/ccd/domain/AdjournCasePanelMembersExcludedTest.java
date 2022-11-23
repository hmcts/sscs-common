package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AdjournCasePanelMembersExcludedTest {
    
    @ParameterizedTest
    @CsvSource(value = {
        "Reserved,RESERVED",
        "No,NO",
        "Yes,YES"
    })
    void shouldReturnCorrectPanelMembersExcludedValue(String expected, AdjournCasePanelMembersExcluded adjournCasePanelMembersExcluded) {
        assertThat(adjournCasePanelMembersExcluded).hasToString(expected);
    }

}