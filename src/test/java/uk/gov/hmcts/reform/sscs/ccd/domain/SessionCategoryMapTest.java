package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

public class SessionCategoryMapTest {

    @Ignore
    @Test
    public void getSessionCategory() {
        SessionCategoryMap sessionCategory = SessionCategoryMap.getSessionCategory("003", "LE", false, false);

        assertEquals(SessionCategoryMap.PIP_REASSESSMENT_CASE_LE_3,sessionCategory);
    }
}
