package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class PartyTest {

    private static final LocalDateTime DATE = LocalDateTime.of(2024, 1, 20, 9, 5, 3);

    @Test
    void shouldReturnEmptyLabelWhenDateIsNull() {
        final Appellant appellant = Appellant.builder().build();

        assertThat(appellant.getConfidentialityRequiredChangedDateLabel()).isEmpty();
    }

    @Test
    void shouldReturnFormattedLabelWhenDateIsSet() {
        final Appellant appellant = Appellant.builder().confidentialityRequiredChangedDate(DATE).build();

        assertThat(appellant.getConfidentialityRequiredChangedDateLabel()).isEqualTo("20 Jan 2024, 9:05:03 am");
    }
}
