package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class HearingOptionsTest {

    @Test
    public void givenAUserWantsToAttendHearing_thenReturnTrue() {
        HearingOptions hearingOptions = HearingOptions.builder().wantsToAttend("Yes").build();

        assertTrue(hearingOptions.isWantsToAttendHearing());
    }

    @Test
    public void givenAUserDoesNotWantToAttendHearing_thenReturnFalse() {
        HearingOptions hearingOptions = HearingOptions.builder().wantsToAttend("No").build();

        assertFalse(hearingOptions.isWantsToAttendHearing());
    }

    @Test
    public void givenWantsToAttendHearingFieldIsNull_thenReturnFalse() {
        HearingOptions hearingOptions = HearingOptions.builder().wantsToAttend(null).build();

        assertFalse(hearingOptions.isWantsToAttendHearing());
    }
}
