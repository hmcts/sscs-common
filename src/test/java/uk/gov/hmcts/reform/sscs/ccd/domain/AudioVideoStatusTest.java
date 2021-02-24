package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class AudioVideoStatusTest {

    @Test
    @Parameters({
            "AWAITING_ACTION, awaitingAction",
            "INCLUDED, included",
            "EXCLUDED, excluded"
    })
    public void shouldGetAudioVideoStatusFromText(AudioVideoStatus status, String text) {
        assertEquals(status, AudioVideoStatus.fromValue(text));
    }

}
