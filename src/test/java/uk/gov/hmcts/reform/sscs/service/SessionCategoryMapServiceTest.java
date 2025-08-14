package uk.gov.hmcts.reform.sscs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.getDuplicates;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;
import uk.gov.hmcts.reform.sscs.ccd.domain.SessionCategory;
import uk.gov.hmcts.reform.sscs.reference.data.model.SessionCategoryMap;
import uk.gov.hmcts.reform.sscs.reference.data.service.SessionCategoryMapService;

public class SessionCategoryMapServiceTest {

    SessionCategoryMapService sessionCategoryMaps = new SessionCategoryMapService();

    @DisplayName("There should be no duplicate Session Category Maps")
    @Test
    public void testHearingDurationsDuplicates() {
        List<SessionCategoryMap> sessionCategoryMaps = this.sessionCategoryMaps.getSessionCategoryMaps();
        Set<SessionCategoryMap> result = new HashSet<>(sessionCategoryMaps);

        assertThat(result)
                .withFailMessage("There are the following duplicates:\n%s",
                        getDuplicates(sessionCategoryMaps))
                .hasSameSizeAs(sessionCategoryMaps);
    }


    @DisplayName("When valid Benefit Code and Issue Code is given to getHearingDuration the valid mapping is returned")
    @Test
    public void testGetSessionCategory() {
        SessionCategoryMap sessionCategory = sessionCategoryMaps
                .getSessionCategory("003", "LE", false, false);

        assertThat(sessionCategory).isNotNull();
        assertThat(sessionCategory.getBenefitCode()).isEqualTo(BenefitCode.PIP_REASSESSMENT_CASE);
        assertThat(sessionCategory.getIssue()).isEqualTo(Issue.LE);
        assertThat(sessionCategory.getCategory()).isEqualTo(SessionCategory.CATEGORY_03);
    }

    @DisplayName("When valid Benefit Code, Issue Code and a secondDoctor is given to getHearingDuration"
            + "the valid mapping is returned")
    @Test
    public void testGSessionCategorySecondDoctor() {
        SessionCategoryMap sessionCategory = sessionCategoryMaps
                .getSessionCategory("064", "GC", true, false);

        assertThat(sessionCategory).isNotNull();
        assertThat(sessionCategory.getBenefitCode()).isEqualTo(BenefitCode.INDUSTRIAL_DEATH_BENEFIT);
        assertThat(sessionCategory.getIssue()).isEqualTo(Issue.GC);
        assertThat(sessionCategory.getCategory()).isEqualTo(SessionCategory.CATEGORY_06);
    }

    @DisplayName("When valid Benefit Code, Issue Code and no secondDoctor is given to getHearingDuration"
            + "the valid mapping is returned")
    @Test
    public void testGSessionCategoryNoSecondDoctor() {
        SessionCategoryMap sessionCategory = sessionCategoryMaps
                .getSessionCategory("064", "GC", false, false);

        assertThat(sessionCategory).isNotNull();
        assertThat(sessionCategory.getBenefitCode()).isEqualTo(BenefitCode.INDUSTRIAL_DEATH_BENEFIT);
        assertThat(sessionCategory.getIssue()).isEqualTo(Issue.GC);
        assertThat(sessionCategory.getCategory()).isEqualTo(SessionCategory.CATEGORY_05);
    }

    @DisplayName("When valid Benefit Code, Issue Code but secondDoctor shouldn't be given to getHearingDuration"
            + "null is returned")
    @Test
    public void testGSessionCategorySecondDoctorInvalid() {
        SessionCategoryMap sessionCategory = sessionCategoryMaps
                .getSessionCategory("003", "LE", true, false);

        assertThat(sessionCategory).isNull();
    }

    @DisplayName("When valid Benefit Code, Issue Code and a secondDoctor is given to getHearingDuration"
            + "the valid mapping is returned")
    @Test
    public void testGSessionCategoryFqpmRequired() {
        SessionCategoryMap sessionCategory = sessionCategoryMaps
                .getSessionCategory("023", "RB", false, true);

        assertThat(sessionCategory).isNotNull();
        assertThat(sessionCategory.getBenefitCode()).isEqualTo(BenefitCode.CHILD_SUPPORT_REFORMS);
        assertThat(sessionCategory.getIssue()).isEqualTo(Issue.RB);
        assertThat(sessionCategory.getCategory()).isEqualTo(SessionCategory.CATEGORY_02);
    }

    @DisplayName("When valid Benefit Code, Issue Code and no secondDoctor is given to getHearingDuration"
            + "the valid mapping is returned")
    @Test
    public void testGSessionCategoryFqpmNotRequired() {
        SessionCategoryMap sessionCategory = sessionCategoryMaps
                .getSessionCategory("023", "RB", false, false);

        assertThat(sessionCategory).isNotNull();
        assertThat(sessionCategory.getBenefitCode()).isEqualTo(BenefitCode.CHILD_SUPPORT_REFORMS);
        assertThat(sessionCategory.getIssue()).isEqualTo(Issue.RB);
        assertThat(sessionCategory.getCategory()).isEqualTo(SessionCategory.CATEGORY_01);
    }

    @DisplayName("When valid Benefit Code, Issue Code but fqpmRequired shouldn't be given to getHearingDuration"
            + "null is returned")
    @Test
    public void testGSessionCategoryFqpmRequiredInvalid() {
        SessionCategoryMap sessionCategoryMap = sessionCategoryMaps
                .getSessionCategory("003", "LE", false, true);

        assertThat(sessionCategoryMap).isNull();
    }
}
