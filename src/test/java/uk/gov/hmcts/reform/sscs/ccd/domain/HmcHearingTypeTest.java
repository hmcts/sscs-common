package uk.gov.hmcts.reform.sscs.ccd.domain;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class HmcHearingTypeTest {

    @ParameterizedTest
    @CsvSource({
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
