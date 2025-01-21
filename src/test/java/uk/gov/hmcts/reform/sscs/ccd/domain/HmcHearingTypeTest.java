package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class HmcHearingTypeTest {

    @Test
    @Parameters({
        "SUBSTANTIVE,BBA3-SUB,Substantive",
        "DIRECTION_HEARINGS,BBA3-DIR,Direction Hearings",
        "CHAMBERS_OUTCOME,BBA3-CHA,Chambers Outcome"
    })
    public void shouldReturnCorrectValues(HmcHearingType hmcHearingType, String hmcReference, String valueEn) {
        assertEquals(hmcReference, hmcHearingType.getHmcReference());
        assertEquals(valueEn, hmcHearingType.getValueEn());
        assertNull(hmcHearingType.getValueCy());
    }
}
