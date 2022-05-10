package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SessionCategoryMapTest {

    @Test
    public void getSessionCategory() {
        SessionCategoryMap sessionCategory = SessionCategoryMap.getSessionCategory("003", "LE", false, false);

        assertEquals(SessionCategoryMap.PIP_REASSESSMENT_CASE_LE_3,sessionCategory);
    }

    @Test
    public void getSessionCategorySecondDoctor() {
        SessionCategoryMap sessionCategory = SessionCategoryMap.getSessionCategory("064", "GC", true, false);

        assertEquals(SessionCategoryMap.INDUSTRIAL_DEATH_BENEFIT_GC_6,sessionCategory);
    }

    @Test
    public void getSessionCategoryNoSecondDoctor() {
        SessionCategoryMap sessionCategory = SessionCategoryMap.getSessionCategory("064", "GC", false, false);

        assertEquals(SessionCategoryMap.INDUSTRIAL_DEATH_BENEFIT_GC_5,sessionCategory);
    }

    @Test
    public void getSessionCategorySecondDoctorInvalid() {
        SessionCategoryMap sessionCategory = SessionCategoryMap.getSessionCategory("003", "LE", true, false);

        assertNull(sessionCategory);
    }

    @Test
    public void getSessionCategoryFqpmRequired() {
        SessionCategoryMap sessionCategory = SessionCategoryMap.getSessionCategory("023", "RB", false, true);

        assertEquals(SessionCategoryMap.CHILD_SUPPORT_REFORMS_RB_2,sessionCategory);
    }

    @Test
    public void getSessionCategoryFqpmNotRequired() {
        SessionCategoryMap sessionCategory = SessionCategoryMap.getSessionCategory("023", "RB", false, false);

        assertEquals(SessionCategoryMap.CHILD_SUPPORT_REFORMS_RB_1,sessionCategory);
    }

    @Test
    public void getSessionCategoryFqpmRequiredInvalid() {
        SessionCategoryMap sessionCategory = SessionCategoryMap.getSessionCategory("003", "LE", false, true);

        assertNull(sessionCategory);
    }
}
