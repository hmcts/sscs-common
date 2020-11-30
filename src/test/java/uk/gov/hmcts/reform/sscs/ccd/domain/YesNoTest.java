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
        assertFalse(isYes(null));
    }
}
