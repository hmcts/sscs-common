package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class UploadPartyTest {

    @Test
    @Parameters({
            "CTSC, ctsc",
            "APPELLANT, appellant",
            "APPOINTEE, appointee",
            "REP, rep",
            "JOINT_PARTY, jointParty",
            "DWP, dwp"
    })
    public void shouldGetAudioVideoPartyFromText(UploadParty party, String text) {
        assertEquals(party, UploadParty.fromValue(text));
    }

}
