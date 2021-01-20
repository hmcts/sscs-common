package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.*;

import org.junit.Test;

public class YesNoTest {

    @Test
    public void isYes_shouldBeTrueForYes() {
        assertTrue(isYes(YES));
    }

    @Test
    public void isYes_shouldBeFalseForNo() {
        assertFalse(isYes(NO));
    }

    @Test
    public void isYes_shouldBeFalseForNull() {
        YesNo yesNo = null;
        assertFalse(isYes(yesNo));
    }

    @Test
    public void isYes_shouldBeTrueForYesString() {
        assertTrue(isYes(YES.getValue()));
    }

    @Test
    public void isYes_shouldBeFalseForNoString() {
        assertFalse(isYes(NO.getValue()));
    }

    @Test
    public void isYes_shouldBeFalseForNullString() {
        String yesNo = null;
        assertFalse(isYes(yesNo));
    }

    @Test
    public void isNo_shouldBeTrueForNo() {
        assertTrue(isNoOrNull(NO));
    }

    @Test
    public void isNoOrNull_shouldBeFalseForYes() {
        assertFalse(isNoOrNull(YES));
    }

    @Test
    public void isNoOrNull_shouldBeTrueForNull() {
        YesNo yesNo = null;
        assertTrue(isNoOrNull(yesNo));
    }

    @Test
    public void isNo_shouldBeTrueForNoString() {
        assertTrue(isNoOrNull(NO.getValue()));
    }

    @Test
    public void isNo_shouldBeFalseForYesString() {
        assertFalse(isNoOrNull(YES.getValue()));
    }

    @Test
    public void isNo_shouldBeTrueForNullString() {
        String yesNo = null;
        assertTrue(isNoOrNull(yesNo));
    }
}
