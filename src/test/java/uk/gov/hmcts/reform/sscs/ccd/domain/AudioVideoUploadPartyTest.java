package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class AudioVideoUploadPartyTest {

    @Test
    @Parameters({
            "CTSC, caseworker-sscs-clerk",
            "APPELLANT, appellant",
            "REP, rep",
            "JOINT_PARTY, jointParty",
            "DWP, caseworker-sscs-dwpresponsewriter"
    })
    public void shouldGetAudioVideoPartyFromText(AudioVideoUploadParty party, String text) {
        assertEquals(party, AudioVideoUploadParty.fromValue(text));
    }

}
