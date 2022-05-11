package uk.gov.hmcts.reform.sscs.model;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;
import uk.gov.hmcts.reform.sscs.ccd.domain.SessionCategory;

public class SessionCategoryMapTest {

    @DisplayName("When valid Benefit Code and Issue Code is given to getHearingDuration the valid mapping is returned")
    @Test
    public void getSessionCategory() {
        SessionCategoryMap sessionCategory = SessionCategoryMap
                .getSessionCategory("003", "LE", false, false);

        assertNotNull(sessionCategory);
        Assert.assertEquals(BenefitCode.PIP_REASSESSMENT_CASE,sessionCategory.getBenefitCode());
        Assert.assertEquals(Issue.LE,sessionCategory.getIssue());
        Assert.assertEquals(SessionCategory.CATEGORY_03,sessionCategory.getCategory());
    }

    @DisplayName("When valid Benefit Code, Issue Code and a secondDoctor is given to getHearingDuration"
            + "the valid mapping is returned")
    @Test
    public void getSessionCategorySecondDoctor() {
        SessionCategoryMap sessionCategory = SessionCategoryMap
                .getSessionCategory("064", "GC", true, false);

        assertNotNull(sessionCategory);
        assertEquals(BenefitCode.INDUSTRIAL_DEATH_BENEFIT,sessionCategory.getBenefitCode());
        assertEquals(Issue.GC,sessionCategory.getIssue());
        assertEquals(SessionCategory.CATEGORY_06,sessionCategory.getCategory());
    }

    @DisplayName("When valid Benefit Code, Issue Code and no secondDoctor is given to getHearingDuration"
            + "the valid mapping is returned")
    @Test
    public void getSessionCategoryNoSecondDoctor() {
        SessionCategoryMap sessionCategory = SessionCategoryMap
                .getSessionCategory("064", "GC", false, false);

        assertNotNull(sessionCategory);
        assertEquals(BenefitCode.INDUSTRIAL_DEATH_BENEFIT,sessionCategory.getBenefitCode());
        assertEquals(Issue.GC,sessionCategory.getIssue());
        assertEquals(SessionCategory.CATEGORY_05,sessionCategory.getCategory());
    }

    @DisplayName("When valid Benefit Code, Issue Code but secondDoctor shouldn't be given to getHearingDuration"
            + "null is returned")
    @Test
    public void getSessionCategorySecondDoctorInvalid() {
        SessionCategoryMap sessionCategory = SessionCategoryMap
                .getSessionCategory("003", "LE", true, false);

        assertNull(sessionCategory);
    }

    @DisplayName("When valid Benefit Code, Issue Code and a secondDoctor is given to getHearingDuration"
            + "the valid mapping is returned")
    @Test
    public void getSessionCategoryFqpmRequired() {
        SessionCategoryMap sessionCategory = SessionCategoryMap
                .getSessionCategory("023", "RB", false, true);

        assertNotNull(sessionCategory);
        assertEquals(BenefitCode.CHILD_SUPPORT_REFORMS,sessionCategory.getBenefitCode());
        assertEquals(Issue.RB,sessionCategory.getIssue());
        assertEquals(SessionCategory.CATEGORY_02,sessionCategory.getCategory());
    }

    @DisplayName("When valid Benefit Code, Issue Code and no secondDoctor is given to getHearingDuration"
            + "the valid mapping is returned")
    @Test
    public void getSessionCategoryFqpmNotRequired() {
        SessionCategoryMap sessionCategory = SessionCategoryMap
                .getSessionCategory("023", "RB", false, false);

        assertNotNull(sessionCategory);
        assertEquals(BenefitCode.CHILD_SUPPORT_REFORMS,sessionCategory.getBenefitCode());
        assertEquals(Issue.RB,sessionCategory.getIssue());
        assertEquals(SessionCategory.CATEGORY_01,sessionCategory.getCategory());
    }

    @DisplayName("When valid Benefit Code, Issue Code but fqpmRequired shouldn't be given to getHearingDuration"
            + "null is returned")
    @Test
    public void getSessionCategoryFqpmRequiredInvalid() {
        SessionCategoryMap sessionCategoryMap = SessionCategoryMap
                .getSessionCategory("003", "LE", false, true);

        assertNull(sessionCategoryMap);
    }
}
