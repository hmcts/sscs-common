package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.ccd.domain.AdjournCaseTypeOfHearing.FACE_TO_FACE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.AdjournCaseTypeOfHearing.PAPER;
import static uk.gov.hmcts.reform.sscs.ccd.domain.AdjournCaseTypeOfHearing.TELEPHONE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.AdjournCaseTypeOfHearing.VIDEO;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.hmcts.reform.sscs.reference.data.model.HearingChannel;

class AdjournCaseTypeOfHearingTest {

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectTypeOfHearingValue(String expected, AdjournCaseTypeOfHearing adjournCaseTypeOfHearing) {
        assertThat(adjournCaseTypeOfHearing).hasToString(expected);
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectTypeOfHearingFromCcdDefinition(String ccdDefinition, AdjournCaseTypeOfHearing expected) {
        assertThat(AdjournCaseTypeOfHearing.getTypeOfHearingByCcdDefinition(ccdDefinition)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void shouldReturnCorrectTypeOfHearingFromHearingChannel(String ccdDefinition, AdjournCaseTypeOfHearing expected, HearingChannel hearingChannel) {
        assertThat(AdjournCaseTypeOfHearing.getTypeOfHearingByHearingChannel(hearingChannel)).isEqualTo(expected);
    }

    private static Stream<Arguments> argumentsProvider() {
        return Stream.of(
          Arguments.of("paper", PAPER, HearingChannel.PAPER),
          Arguments.of("video", VIDEO, HearingChannel.VIDEO),
          Arguments.of("telephone", TELEPHONE, HearingChannel.TELEPHONE),
          Arguments.of("faceToFace", FACE_TO_FACE, HearingChannel.FACE_TO_FACE)
        );
    }
}
