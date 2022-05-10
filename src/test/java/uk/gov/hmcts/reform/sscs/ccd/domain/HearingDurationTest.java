package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;

public class HearingDurationTest {

    @Test
    public void getHearingDuration() {
        HearingDuration result = HearingDuration.getHearingDuration("003", "LE");

        assertEquals(HearingDuration.PIP_REASSESSMENT_CASE_LE,result);
    }

    @Test
    public void getDurationFaceToFaceNoElement() {
        int result = HearingDuration.UC_US.getDurationFaceToFace(List.of());

        assertEquals(30,result);
    }

    @Test
    public void getDurationFaceToFaceNoCorrectElement() {
        int result = HearingDuration.UC_US.getDurationFaceToFace(List.of("PH"));

        assertEquals(30,result);
    }

    @Test
    public void getDurationFaceToFaceWc() {
        int result = HearingDuration.UC_US.getDurationFaceToFace(List.of("WC"));

        assertEquals(45,result);
    }

    @Test
    public void getDurationFaceToFaceSg() {
        int result = HearingDuration.UC_US.getDurationFaceToFace(List.of("SG"));

        assertEquals(45,result);
    }

    @Test
    public void getDurationInterpreterNoElement() {
        int result = HearingDuration.UC_US.getDurationInterpreter(List.of());

        assertEquals(60,result);
    }

    @Test
    public void getDurationInterpreterNoCorrectElement() {
        int result = HearingDuration.UC_US.getDurationInterpreter(List.of("PH"));

        assertEquals(60,result);
    }

    @Test
    public void getDurationInterpreterWc() {
        int result = HearingDuration.UC_US.getDurationInterpreter(List.of("WC"));

        assertEquals(75,result);
    }

    @Test
    public void getDurationInterpreterSg() {
        int result = HearingDuration.UC_US.getDurationInterpreter(List.of("SG"));

        assertEquals(75,result);
    }
}
