package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class TypeOfHearingTest {

    @Test
    @Parameters({
        "SUBSTANTIVE,substantive,BBA3-SUB,Substantive",
        "DIRECTION_HEARINGS,direction,BBA3-DIR,Direction Hearings",
        "CHAMBERS_OUTCOME,chamber,BBA3-CHA,Chambers Outcome"
    })
    public void shouldReturnCorrectValues(TypeOfHearing typeOfHearing, String ccdDefinition, String hmcReference, String valueEn) {
        assertEquals(ccdDefinition, typeOfHearing.getCcdDefinition());
        assertEquals(ccdDefinition, typeOfHearing.toString());
        assertEquals(hmcReference, typeOfHearing.getHmcReference());
        assertEquals(valueEn, typeOfHearing.getValueEn());
        assertNull(typeOfHearing.getValueCy());
    }
}
