package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AdjournCaseTypeOfHearingTest {

    @ParameterizedTest
    @CsvSource(value = {
        "paper,PAPER",
        "video,VIDEO",
        "telephone,TELEPHONE",
        "faceToFace,FACE_TO_FACE",
    })
    void shouldReturnCorrectTypeOfHearingValue(String expected, AdjournCaseTypeOfHearing adjournCaseTypeOfHearing) {
        assertThat(adjournCaseTypeOfHearing).hasToString(expected);
    }

}
