package uk.gov.hmcts.reform.sscs.ccd.domain;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;

public class NameTest {

    @Test
    public void givenAName_generateAnAbbreviatedFullName() {
        Name name = Name.builder().title("Mr").firstName("Terry").lastName("Tibbs").build();

        assertEquals("Mr T Tibbs", name.getAbbreviatedFullName());
    }
}
