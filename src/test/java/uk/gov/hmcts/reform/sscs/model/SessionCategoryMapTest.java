package uk.gov.hmcts.reform.sscs.model;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;
import uk.gov.hmcts.reform.sscs.ccd.domain.SessionCategory;

public class SessionCategoryMapTest {

    @Test
    public void getSessionCategory() {
        SessionCategoryMap sessionCategory = SessionCategoryMap.getSessionCategory("003", "LE", false, false);

        assertNotNull(sessionCategory);
        Assert.assertEquals(BenefitCode.PIP_REASSESSMENT_CASE,sessionCategory.getBenefitCode());
        Assert.assertEquals(Issue.LE,sessionCategory.getIssue());
        Assert.assertEquals(SessionCategory.CATEGORY_03,sessionCategory.getCategory());
    }

    @Test
    public void getSessionCategorySecondDoctor() {
        SessionCategoryMap sessionCategory = SessionCategoryMap.getSessionCategory("064", "GC", true, false);

        assertNotNull(sessionCategory);
        assertEquals(BenefitCode.INDUSTRIAL_DEATH_BENEFIT,sessionCategory.getBenefitCode());
        assertEquals(Issue.GC,sessionCategory.getIssue());
        assertEquals(SessionCategory.CATEGORY_06,sessionCategory.getCategory());
    }

    @Test
    public void getSessionCategoryNoSecondDoctor() {
        SessionCategoryMap sessionCategory = SessionCategoryMap.getSessionCategory("064", "GC", false, false);

        assertNotNull(sessionCategory);
        assertEquals(BenefitCode.INDUSTRIAL_DEATH_BENEFIT,sessionCategory.getBenefitCode());
        assertEquals(Issue.GC,sessionCategory.getIssue());
        assertEquals(SessionCategory.CATEGORY_05,sessionCategory.getCategory());
    }

    @Test
    public void getSessionCategorySecondDoctorInvalid() {
        SessionCategoryMap sessionCategory = SessionCategoryMap.getSessionCategory("003", "LE", true, false);

        assertNull(sessionCategory);
    }

    @Test
    public void getSessionCategoryFqpmRequired() {
        SessionCategoryMap sessionCategory = SessionCategoryMap.getSessionCategory("023", "RB", false, true);

        assertNotNull(sessionCategory);
        assertEquals(BenefitCode.CHILD_SUPPORT_REFORMS,sessionCategory.getBenefitCode());
        assertEquals(Issue.RB,sessionCategory.getIssue());
        assertEquals(SessionCategory.CATEGORY_02,sessionCategory.getCategory());
    }

    @Test
    public void getSessionCategoryFqpmNotRequired() {
        SessionCategoryMap sessionCategory = SessionCategoryMap.getSessionCategory("023", "RB", false, false);

        assertNotNull(sessionCategory);
        assertEquals(BenefitCode.CHILD_SUPPORT_REFORMS,sessionCategory.getBenefitCode());
        assertEquals(Issue.RB,sessionCategory.getIssue());
        assertEquals(SessionCategory.CATEGORY_01,sessionCategory.getCategory());
    }

    @Test
    public void getSessionCategoryFqpmRequiredInvalid() {
        SessionCategoryMap sessionCategoryMap = SessionCategoryMap.getSessionCategory("003", "LE", false, true);

        assertNull(sessionCategoryMap);
    }
}
