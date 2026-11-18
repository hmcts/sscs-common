package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class HearingOptionsTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"No", "NO"})
    void givenDoesNotWantToAttendHearing_thenReturnFalse(String wantsToAttend) {
        final HearingOptions hearingOptions = HearingOptions.builder().wantsToAttend(wantsToAttend).build();
        assertThat(hearingOptions.isWantsToAttendHearing()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Yes", "YES"})
    void givenWantsToAttendHearing_thenReturnTrue(String wantsToAttend) {
        final HearingOptions hearingOptions = HearingOptions.builder().wantsToAttend(wantsToAttend).build();
        assertThat(hearingOptions.isWantsToAttendHearing()).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"No", "NO"})
    void givenDoesNotAgreeLessNotice_thenReturnFalse(String agreeLessNotice) {
        final HearingOptions hearingOptions = HearingOptions.builder().agreeLessNotice(agreeLessNotice).build();
        assertThat(hearingOptions.isAgreeLessNotice()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Yes", "YES"})
    void givenAgreesLessNotice_thenReturnTrue(String agreeLessNotice) {
        final HearingOptions hearingOptions = HearingOptions.builder().agreeLessNotice(agreeLessNotice).build();
        assertThat(hearingOptions.isAgreeLessNotice()).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"No", "NO"})
    void givenDoesNotWantInterpreter_thenReturnFalse(String languageInterpreter) {
        final HearingOptions hearingOptions = HearingOptions.builder().languageInterpreter(languageInterpreter).build();
        assertThat(hearingOptions.wantsInterpreter()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Yes", "YES"})
    void givenWantsInterpreter_thenReturnTrue(String languageInterpreter) {
        final HearingOptions hearingOptions = HearingOptions.builder().languageInterpreter(languageInterpreter).build();
        assertThat(hearingOptions.wantsInterpreter()).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"No", "NO"})
    void givenDoesNotWantSupport_thenReturnFalse(String wantsSupport) {
        final HearingOptions hearingOptions = HearingOptions.builder().wantsSupport(wantsSupport).build();
        assertThat(hearingOptions.wantsSupport()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Yes", "YES"})
    void givenWantSupport_thenReturnTrue(String wantsSupport) {
        final HearingOptions hearingOptions = HearingOptions.builder().wantsSupport(wantsSupport).build();
        assertThat(hearingOptions.wantsSupport()).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("arrangementsWithoutSignLanguageInterpreter")
    void givenArrangementsDoesNotIncludeSignLanguageInterpreter_thenReturnFalse(List<String> arrangements) {
        final HearingOptions hearingOptions = HearingOptions.builder().arrangements(arrangements).build();
        assertThat(hearingOptions.wantsSignLanguageInterpreter()).isFalse();
    }

    @ParameterizedTest
    @MethodSource("arrangementsWithSignLanguageInterpreter")
    void givenArrangementsIncludesSignLanguageInterpreter_thenReturnTrue(List<String> arrangements) {
        final HearingOptions hearingOptions = HearingOptions.builder().arrangements(arrangements).build();
        assertThat(hearingOptions.wantsSignLanguageInterpreter()).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("arrangementsWithoutHearingLoop")
    void givenArrangementsDoesNotIncludeHearingLoop_thenReturnFalse(List<String> arrangements) {
        final HearingOptions hearingOptions = HearingOptions.builder().arrangements(arrangements).build();
        assertThat(hearingOptions.wantsHearingLoop()).isFalse();
    }

    @ParameterizedTest
    @MethodSource("arrangementsWithHearingLoop")
    void givenArrangementsIncludesHearingLoop_thenReturnTrue(List<String> arrangements) {
        final HearingOptions hearingOptions = HearingOptions.builder().arrangements(arrangements).build();
        assertThat(hearingOptions.wantsHearingLoop()).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("arrangementsWithoutDisabledAccess")
    void givenArrangementsDoesNotIncludeDisabledAccess_thenReturnFalse(List<String> arrangements) {
        final HearingOptions hearingOptions = HearingOptions.builder().arrangements(arrangements).build();
        assertThat(hearingOptions.wantsAccessibleHearingRoom()).isFalse();
    }

    @ParameterizedTest
    @MethodSource("arrangementsWithDisabledAccess")
    void givenArrangementsIncludesDisabledAccess_thenReturnTrue(List<String> arrangements) {
        final HearingOptions hearingOptions = HearingOptions.builder().arrangements(arrangements).build();
        assertThat(hearingOptions.wantsAccessibleHearingRoom()).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
        "No, Yes, Yes",
        "Yes, No, Yes",
        "Yes, Yes, No",
        ", ,",
    })
    void givenMissingAttendSupportOrInterpreter_thenWantsToAttendWithInterpreterSupportReturnsFalse(
        String wantsToAttend, String wantsSupport, String languageInterpreter) {
        final HearingOptions hearingOptions = HearingOptions.builder()
            .wantsToAttend(wantsToAttend)
            .wantsSupport(wantsSupport)
            .languageInterpreter(languageInterpreter)
            .build();
        assertThat(hearingOptions.wantsToAttendWithInterpreterSupport()).isFalse();
    }

    @Test
    void givenWantsToAttendSupportAndInterpreter_thenWantsToAttendWithInterpreterSupportReturnsTrue() {
        final HearingOptions hearingOptions = HearingOptions.builder()
            .wantsToAttend("Yes")
            .wantsSupport("Yes")
            .languageInterpreter("Yes")
            .build();
        assertThat(hearingOptions.wantsToAttendWithInterpreterSupport()).isTrue();
    }

    private static Stream<List<String>> arrangementsWithoutSignLanguageInterpreter() {
        return Stream.of(List.of("hearingLoop"), List.of("disabledAccess"), List.of("hearingLoop", "disabledAccess"));
    }

    private static Stream<List<String>> arrangementsWithSignLanguageInterpreter() {
        return Stream.of(List.of("signLanguageInterpreter"), List.of("signLanguageInterpreter", "hearingLoop"));
    }

    private static Stream<List<String>> arrangementsWithoutHearingLoop() {
        return Stream.of(List.of("signLanguageInterpreter"), List.of("disabledAccess"),
            List.of("signLanguageInterpreter", "disabledAccess"));
    }

    private static Stream<List<String>> arrangementsWithHearingLoop() {
        return Stream.of(List.of("hearingLoop"), List.of("hearingLoop", "signLanguageInterpreter"));
    }

    private static Stream<List<String>> arrangementsWithoutDisabledAccess() {
        return Stream.of(List.of("signLanguageInterpreter"), List.of("hearingLoop"),
            List.of("signLanguageInterpreter", "hearingLoop"));
    }

    private static Stream<List<String>> arrangementsWithDisabledAccess() {
        return Stream.of(List.of("disabledAccess"), List.of("disabledAccess", "hearingLoop"));
    }
}
