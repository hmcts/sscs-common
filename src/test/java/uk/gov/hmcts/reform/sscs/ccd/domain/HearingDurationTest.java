package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

public class HearingDurationTest {

    @Ignore
    @Test
    public void getHearingDuration() {
        HearingDuration result = HearingDuration.getHearingDuration("003", "LE", null);

        assertEquals(HearingDuration.PIP_REASSESSMENT_CASE_LE,result);
    }
}
