package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class YesNoUnknownTest {

    @Test
    void shouldExposeValueForEachEnumConstant() {
        assertThat(YesNoUnknown.YES.getValue()).isEqualTo("Yes");
        assertThat(YesNoUnknown.NO.getValue()).isEqualTo("No");
        assertThat(YesNoUnknown.UNKNOWN.getValue()).isEqualTo("Unknown");
    }

    @Test
    void shouldReturnValueFromToString() {
        assertThat(YesNoUnknown.YES.toString()).hasToString("Yes");
        assertThat(YesNoUnknown.NO.toString()).hasToString("No");
        assertThat(YesNoUnknown.UNKNOWN.toString()).hasToString("Unknown");
    }

    @Test
    void isYesEnumShouldReturnTrueOnlyForYes() {
        assertThat(YesNoUnknown.isYes(YesNoUnknown.YES)).isTrue();
        assertThat(YesNoUnknown.isYes(YesNoUnknown.NO)).isFalse();
        assertThat(YesNoUnknown.isYes(YesNoUnknown.UNKNOWN)).isFalse();
    }

    @Test
    void isYesStringShouldReturnTrueOnlyForYesIgnoringCase() {
        assertThat(YesNoUnknown.isYes("Yes")).isTrue();
        assertThat(YesNoUnknown.isYes("yes")).isTrue();
        assertThat(YesNoUnknown.isYes("No")).isFalse();
        assertThat(YesNoUnknown.isYes("Unknown")).isFalse();
        assertThat(YesNoUnknown.isYes((String) null)).isFalse();
    }

    @Test
    void isNoEnumShouldReturnTrueOnlyForNo() {
        assertThat(YesNoUnknown.isNo(YesNoUnknown.NO)).isTrue();
        assertThat(YesNoUnknown.isNo(YesNoUnknown.YES)).isFalse();
        assertThat(YesNoUnknown.isNo(YesNoUnknown.UNKNOWN)).isFalse();
    }

    @Test
    void isNoStringShouldReturnTrueOnlyForNoIgnoringCase() {
        assertThat(YesNoUnknown.isNo("No")).isTrue();
        assertThat(YesNoUnknown.isNo("no")).isTrue();
        assertThat(YesNoUnknown.isNo("Yes")).isFalse();
        assertThat(YesNoUnknown.isNo("Unknown")).isFalse();
        assertThat(YesNoUnknown.isNo((String) null)).isFalse();
    }

    @Test
    void isNoOrNullEnumShouldReturnTrueForNoOrNull() {
        assertThat(YesNoUnknown.isNoOrNull(YesNoUnknown.NO)).isTrue();
        assertThat(YesNoUnknown.isNoOrNull((YesNoUnknown) null)).isTrue();
        assertThat(YesNoUnknown.isNoOrNull(YesNoUnknown.YES)).isFalse();
        assertThat(YesNoUnknown.isNoOrNull(YesNoUnknown.UNKNOWN)).isFalse();
    }

    @Test
    void isNoOrNullStringShouldReturnTrueForNoOrNullIgnoringCase() {
        assertThat(YesNoUnknown.isNoOrNull("No")).isTrue();
        assertThat(YesNoUnknown.isNoOrNull("no")).isTrue();
        assertThat(YesNoUnknown.isNoOrNull((String) null)).isTrue();
        assertThat(YesNoUnknown.isNoOrNull("Yes")).isFalse();
        assertThat(YesNoUnknown.isNoOrNull("Unknown")).isFalse();
    }

    @Test
    void isNoOrNullOrUnknownEnumShouldReturnTrueForNoOrNullOrUnknown() {
        assertThat(YesNoUnknown.isNoOrNullOrUnknown(YesNoUnknown.NO)).isTrue();
        assertThat(YesNoUnknown.isNoOrNullOrUnknown(YesNoUnknown.UNKNOWN)).isTrue();
        assertThat(YesNoUnknown.isNoOrNullOrUnknown((YesNoUnknown) null)).isTrue();
        assertThat(YesNoUnknown.isNoOrNullOrUnknown(YesNoUnknown.YES)).isFalse();
    }

    @Test
    void isNoOrNullOrUnknownStringShouldReturnTrueForNoOrNullOrUnknownIgnoringCase() {
        assertThat(YesNoUnknown.isNoOrNullOrUnknown("No")).isTrue();
        assertThat(YesNoUnknown.isNoOrNullOrUnknown("unknown")).isTrue();
        assertThat(YesNoUnknown.isNoOrNullOrUnknown((String) null)).isTrue();
        assertThat(YesNoUnknown.isNoOrNullOrUnknown("Yes")).isFalse();
    }

    @Test
    void isNoOrUnknownEnumShouldReturnTrueForNoOrUnknownButNotNull() {
        assertThat(YesNoUnknown.isNoOrUnknown(YesNoUnknown.NO)).isTrue();
        assertThat(YesNoUnknown.isNoOrUnknown(YesNoUnknown.UNKNOWN)).isTrue();
        assertThat(YesNoUnknown.isNoOrUnknown(YesNoUnknown.YES)).isFalse();
        assertThat(YesNoUnknown.isNoOrUnknown((YesNoUnknown) null)).isFalse();
    }

    @Test
    void isNoOrUnknownStringShouldReturnTrueForNoOrUnknownButNotNullIgnoringCase() {
        assertThat(YesNoUnknown.isNoOrUnknown("No")).isTrue();
        assertThat(YesNoUnknown.isNoOrUnknown("UNKNOWN")).isTrue();
        assertThat(YesNoUnknown.isNoOrUnknown("Yes")).isFalse();
        assertThat(YesNoUnknown.isNoOrUnknown((String) null)).isFalse();
    }
}
