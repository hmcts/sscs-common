package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HearingTypeTest {

    @Test
    public void shouldReturnOralHearingIfTribunalsTypeIs2() {
        HearingType hearingType = HearingType.getHearingTypeByTribunalsTypeId("2");

        assertEquals(hearingType, HearingType.ORAL);
    }
}
