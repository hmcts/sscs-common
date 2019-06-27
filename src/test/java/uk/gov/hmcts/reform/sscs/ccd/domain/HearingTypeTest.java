package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class HearingTypeTest {

    @Test
    @Parameters({"2,ORAL", "1,PAPER"})
    public void shouldReturnOralHearingIfTribunalsTypeIs2(String tribunalsTypeId, HearingType expected) {
        HearingType hearingType = HearingType.getHearingTypeByTribunalsTypeId(tribunalsTypeId);
        assertEquals(expected, hearingType);
    }

    @Test
    public void shouldReturnCorrectHearingTypeValue() {
        assertEquals("cor", HearingType.COR.getValue());
    }
}
