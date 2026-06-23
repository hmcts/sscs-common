package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class YesNoUndeterminedTest {

    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String UNDETERMINED = "Undetermined";
    public static final String YES_LC = "yes";
    public static final String NO_LC = "no";
    public static final String UNDETERMINED_LC = "undetermined";
    public static final String UNDETERMINED_UC = "UNDETERMINED";

    @Test
    void shouldExposeValueForEachEnumConstant() {
        assertThat(YesNoUndetermined.YES.getValue()).isEqualTo(YES);
        assertThat(YesNoUndetermined.NO.getValue()).isEqualTo(NO);
        assertThat(YesNoUndetermined.UNDETERMINED.getValue()).isEqualTo(UNDETERMINED);
    }

    @Test
    void shouldReturnValueFromToString() {
        assertThat(YesNoUndetermined.YES.toString()).hasToString(YES);
        assertThat(YesNoUndetermined.NO.toString()).hasToString(NO);
        assertThat(YesNoUndetermined.UNDETERMINED.toString()).hasToString(UNDETERMINED);
    }

    @Test
    void isYesEnumShouldReturnTrueOnlyForYes() {
        assertThat(YesNoUndetermined.isYes(YesNoUndetermined.YES)).isTrue();
        assertThat(YesNoUndetermined.isYes(YesNoUndetermined.NO)).isFalse();
        assertThat(YesNoUndetermined.isYes(YesNoUndetermined.UNDETERMINED)).isFalse();
    }

    @Test
    void isYesStringShouldReturnTrueOnlyForYesIgnoringCase() {
        assertThat(YesNoUndetermined.isYes(YES)).isTrue();
        assertThat(YesNoUndetermined.isYes(YES_LC)).isTrue();
        assertThat(YesNoUndetermined.isYes(NO)).isFalse();
        assertThat(YesNoUndetermined.isYes(UNDETERMINED)).isFalse();
        assertThat(YesNoUndetermined.isYes((String) null)).isFalse();
    }

    @Test
    void isNoEnumShouldReturnTrueOnlyForNo() {
        assertThat(YesNoUndetermined.isNo(YesNoUndetermined.NO)).isTrue();
        assertThat(YesNoUndetermined.isNo(YesNoUndetermined.YES)).isFalse();
        assertThat(YesNoUndetermined.isNo(YesNoUndetermined.UNDETERMINED)).isFalse();
    }

    @Test
    void isNoStringShouldReturnTrueOnlyForNoIgnoringCase() {
        assertThat(YesNoUndetermined.isNo(NO)).isTrue();
        assertThat(YesNoUndetermined.isNo(NO_LC)).isTrue();
        assertThat(YesNoUndetermined.isNo(YES)).isFalse();
        assertThat(YesNoUndetermined.isNo(UNDETERMINED)).isFalse();
        assertThat(YesNoUndetermined.isNo((String) null)).isFalse();
    }

    @Test
    void isNoOrNullEnumShouldReturnTrueForNoOrNull() {
        assertThat(YesNoUndetermined.isNoOrNull(YesNoUndetermined.NO)).isTrue();
        assertThat(YesNoUndetermined.isNoOrNull((YesNoUndetermined) null)).isTrue();
        assertThat(YesNoUndetermined.isNoOrNull(YesNoUndetermined.YES)).isFalse();
        assertThat(YesNoUndetermined.isNoOrNull(YesNoUndetermined.UNDETERMINED)).isFalse();
    }

    @Test
    void isNoOrNullStringShouldReturnTrueForNoOrNullIgnoringCase() {
        assertThat(YesNoUndetermined.isNoOrNull(NO)).isTrue();
        assertThat(YesNoUndetermined.isNoOrNull(NO_LC)).isTrue();
        assertThat(YesNoUndetermined.isNoOrNull((String) null)).isTrue();
        assertThat(YesNoUndetermined.isNoOrNull(YES)).isFalse();
        assertThat(YesNoUndetermined.isNoOrNull(UNDETERMINED)).isFalse();
    }

    @Test
    void isNoOrNullOrUndeterminedEnumShouldReturnTrueForNoOrNullOrUndetermined() {
        assertThat(YesNoUndetermined.isNoOrNullOrUndetermined(YesNoUndetermined.NO)).isTrue();
        assertThat(YesNoUndetermined.isNoOrNullOrUndetermined(YesNoUndetermined.UNDETERMINED)).isTrue();
        assertThat(YesNoUndetermined.isNoOrNullOrUndetermined((YesNoUndetermined) null)).isTrue();
        assertThat(YesNoUndetermined.isNoOrNullOrUndetermined(YesNoUndetermined.YES)).isFalse();
    }

    @Test
    void isNoOrNullOrUndeterminedStringShouldReturnTrueForNoOrNullOrUndeterminedIgnoringCase() {
        assertThat(YesNoUndetermined.isNoOrNullOrUndetermined(NO)).isTrue();
        assertThat(YesNoUndetermined.isNoOrNullOrUndetermined(UNDETERMINED_LC)).isTrue();
        assertThat(YesNoUndetermined.isNoOrNullOrUndetermined((String) null)).isTrue();
        assertThat(YesNoUndetermined.isNoOrNullOrUndetermined(YES)).isFalse();
    }

    @Test
    void isNoOrUndeterminedEnumShouldReturnTrueForNoOrUndeterminedButNotNull() {
        assertThat(YesNoUndetermined.isNoOrUndetermined(YesNoUndetermined.NO)).isTrue();
        assertThat(YesNoUndetermined.isNoOrUndetermined(YesNoUndetermined.UNDETERMINED)).isTrue();
        assertThat(YesNoUndetermined.isNoOrUndetermined(YesNoUndetermined.YES)).isFalse();
        assertThat(YesNoUndetermined.isNoOrUndetermined((YesNoUndetermined) null)).isFalse();
    }

    @Test
    void isNoOrUndeterminedStringShouldReturnTrueForNoOrUndeterminedButNotNullIgnoringCase() {
        assertThat(YesNoUndetermined.isNoOrUndetermined(NO)).isTrue();
        assertThat(YesNoUndetermined.isNoOrUndetermined(UNDETERMINED_UC)).isTrue();
        assertThat(YesNoUndetermined.isNoOrUndetermined(YES)).isFalse();
        assertThat(YesNoUndetermined.isNoOrUndetermined((String) null)).isFalse();
    }
}
