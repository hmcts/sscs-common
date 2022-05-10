package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;

public class HearingDurationTest {

    @Test
    public void getHearingDuration() {
        HearingDuration result = HearingDuration.getHearingDuration("003", "LE", null);

        assertEquals(HearingDuration.PIP_REASSESSMENT_CASE_LE,result);
    }

    @Test
    public void getHearingDurationNoElement() {
        HearingDuration result = HearingDuration.getHearingDuration("001", "US", List.of());

        assertEquals(HearingDuration.UC_US,result);
    }

    @Test
    public void getHearingDurationNoCorrectElement() {
        HearingDuration result = HearingDuration.getHearingDuration("001", "US", List.of("PH"));

        assertEquals(HearingDuration.UC_US,result);
    }

    @Test
    public void getHearingDurationWC() {
        HearingDuration result = HearingDuration.getHearingDuration("001", "US", List.of("WC"));

        assertEquals(HearingDuration.UC_US_WC,result);
    }

    @Test
    public void getHearingDurationSG() {
        HearingDuration result = HearingDuration.getHearingDuration("001", "US", List.of("SG"));

        assertEquals(HearingDuration.UC_US_SG,result);
    }
}
