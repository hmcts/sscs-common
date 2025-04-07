package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class HearingTypeTest {

    @ParameterizedTest
    @CsvSource({"2,ORAL", "1,PAPER"})
    public void shouldReturnOralHearingIfTribunalsTypeIs2(String tribunalsTypeId, HearingType expected) {
        HearingType hearingType = HearingType.getHearingTypeByTribunalsTypeId(tribunalsTypeId);
        assertEquals(expected, hearingType);
    }

    @Test
    public void shouldReturnCorrectHearingTypeValue() {
        assertEquals("cor", HearingType.COR.getValue());
    }
}
