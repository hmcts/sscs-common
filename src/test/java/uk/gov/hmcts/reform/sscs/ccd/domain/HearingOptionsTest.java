package uk.gov.hmcts.reform.sscs.ccd.domain;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.NO;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.YES;

import org.junit.Test;

public class HearingOptionsTest {

    @Test
    public void givenAUserWantsToAttendHearing_thenReturnTrue() {
        HearingOptions hearingOptions = HearingOptions.builder().wantsToAttend(YES).build();

        assertTrue(hearingOptions.isWantsToAttendHearing());
    }

    @Test
    public void givenAUserDoesNotWantToAttendHearing_thenReturnFalse() {
        HearingOptions hearingOptions = HearingOptions.builder().wantsToAttend(NO).build();

        assertFalse(hearingOptions.isWantsToAttendHearing());
    }

    @Test
    public void givenWantsToAttendHearingFieldIsNull_thenReturnFalse() {
        HearingOptions hearingOptions = HearingOptions.builder().wantsToAttend(null).build();

        assertFalse(hearingOptions.isWantsToAttendHearing());
    }
}
