package uk.gov.hmcts.reform.sscs.ccd.domain;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;

public class NameNoTitleTest {

    @Test
    public void givenAName_generateAnAbbreviatedFullName() {
        NameNoTitle name = NameNoTitle.builder().firstName("Terry").lastName("Tibbs").build();

        assertEquals("Mr T Tibbs", name.getAbbreviatedFullName("Mr"));
    }

    @Test
    public void givenAName_generateFullNameNoTitle() {
        NameNoTitle name = NameNoTitle.builder().firstName("Terry").lastName("Tibbs").build();
        assertEquals("Terry Tibbs", name.getFullNameNoTitle());
    }

    @Test
    public void givenAName_generateFullName() {
        NameNoTitle name = NameNoTitle.builder().firstName("Terry").lastName("Tibbs").build();
        assertEquals("Mr Terry Tibbs", name.getFullName("Mr"));
    }
}
