package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class UploadPartyTest {

    @ParameterizedTest
    @CsvSource({
        "CTSC, ctsc",
        "APPELLANT, appellant",
        "APPOINTEE, appointee",
        "REP, rep",
        "JOINT_PARTY, jointParty",
        "DWP, dwp",
        "OTHER_PARTY, otherParty",
        "OTHER_PARTY_REP, otherPartyRep",
        "OTHER_PARTY_APPOINTEE, otherPartyAppointee",
    })
    public void shouldGetAudioVideoPartyFromText(UploadParty party, String text) {
        assertEquals(party, UploadParty.fromValue(text));
    }

}
