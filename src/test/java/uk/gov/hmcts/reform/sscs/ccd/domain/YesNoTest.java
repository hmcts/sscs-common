package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.NO;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.YES;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isNoOrNull;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;

import org.junit.jupiter.api.Test;

class YesNoTest {

    @Test
    void isYes_shouldBeTrueForYes() {
        assertThat(isYes(YES)).isTrue();
    }

    @Test
    void isYes_shouldBeFalseForNo() {
        assertThat(isYes(NO)).isFalse();
    }

    @Test
    void isYes_shouldBeFalseForNull() {
        YesNo yesNo = null;
        assertThat(isYes(yesNo)).isFalse();
    }

    @Test
    void isYes_shouldBeTrueForYesString() {
        assertThat(isYes(YES.getValue())).isTrue();
    }

    @Test
    void isYes_shouldBeFalseForNoString() {
        assertThat(isYes(NO.getValue())).isFalse();
    }

    @Test
    void isYes_shouldBeFalseForNullString() {
        String yesNo = null;
        assertThat(isYes(yesNo)).isFalse();
    }

    @Test
    void isNo_shouldBeTrueForNo() {
        assertThat(isNoOrNull(NO)).isTrue();
    }

    @Test
    void isNoOrNull_shouldBeFalseForYes() {
        assertThat(isNoOrNull(YES)).isFalse();
    }

    @Test
    void isNoOrNull_shouldBeTrueForNull() {
        YesNo yesNo = null;
        assertThat(isNoOrNull(yesNo)).isTrue();
    }

    @Test
    void isNo_shouldBeTrueForNoString() {
        assertThat(isNoOrNull(NO.getValue())).isTrue();
    }

    @Test
    void isNo_shouldBeFalseForYesString() {
        assertThat(isNoOrNull(YES.getValue())).isFalse();
    }

    @Test
    void isNo_shouldBeTrueForNullString() {
        String yesNo = null;
        assertThat(isNoOrNull(yesNo)).isTrue();
    }

}
