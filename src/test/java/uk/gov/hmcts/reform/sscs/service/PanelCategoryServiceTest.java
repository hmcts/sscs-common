package uk.gov.hmcts.reform.sscs.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsIndustrialInjuriesData;
import uk.gov.hmcts.reform.sscs.ccd.domain.YesNo;
import uk.gov.hmcts.reform.sscs.reference.data.model.PanelCategory;
import uk.gov.hmcts.reform.sscs.reference.data.service.PanelCategoryService;

import java.util.List;

public class PanelCategoryServiceTest {

    private final PanelCategoryService panelCategoryService = new PanelCategoryService();
    private static final String TRIBUNAL_MEMBER_MEDICAL = "58";
    private static final String TRIBUNALS_MEMBER_FINANCIALLY_QUALIFIED = "50";
    private SscsCaseData caseData;


    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(panelCategoryService, "defaultPanelCompEnabled", true);
        caseData = SscsCaseData.builder()
                .sscsIndustrialInjuriesData(SscsIndustrialInjuriesData.builder().build())
                .benefitCode("001")
                .ccdCaseId("1234")
                .issueCode("AD")
                .build();
    }

    @Test
    @DisplayName("Valid call to getPanelCategory should return correct johTier")
    public void getPanelCategory(){
        PanelCategory result = panelCategoryService.getPanelCategory("001AD", null, null, null);
        assertThat(result).isNotNull();
        assertThat(result.getBenefitIssueCode()).isEqualTo("001AD");
        assertThat(result.getJohTiers()).isNotEmpty();
        assertThat(result.getJohTiers().get(0)).isEqualTo("84");
    }

    @Test
    @DisplayName("Invalid call to getPanelCategory should return null")
    public void getPanelCategoryWithInvalidParameters() {
        PanelCategory result = panelCategoryService.getPanelCategory(null, null, null, null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Call to getPanelCategory with FQPM should return correct johTier")
    public void getPanelCategoryWithFQPM() {
        PanelCategory result = panelCategoryService.getPanelCategory("016CC", null, "true", null);
        assertThat(result).isNotNull();
        assertThat(result.getJohTiers().stream().anyMatch(TRIBUNALS_MEMBER_FINANCIALLY_QUALIFIED::equals)).isTrue();
    }

    @Test
    @DisplayName("Call to getPanelCategory with specialism should return correct johTier")
    public void getPanelCategoryWithSpecialism() {
        PanelCategory oneSpecialismResult = panelCategoryService.getPanelCategory("067CB", "1", null, null);
        PanelCategory twoSpecialismResult = panelCategoryService.getPanelCategory("067CB", "2", null, null);
        assertThat(oneSpecialismResult).isNotNull();
        assertThat(twoSpecialismResult).isNotNull();
        assertThat(oneSpecialismResult.getJohTiers().stream().filter(TRIBUNAL_MEMBER_MEDICAL::equals).count()).isEqualTo(1);
        assertThat(twoSpecialismResult.getJohTiers().stream().filter(TRIBUNAL_MEMBER_MEDICAL::equals).count()).isEqualTo(2);
    }

    @DisplayName("getRoleTypes returns an valid list")
    @Test
    public void testGetRoles() {
        List<String> result = panelCategoryService.getRoleTypes(caseData);
        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isEqualTo("84");
    }

    @DisplayName("getRoleTypes should return an empty list when an invalid benefit issue code is given")
    @Test
    public void testGetRolesWithInvalidBenefitIssueCode() {
        caseData.setBenefitCode(null);
        caseData.setIssueCode(null);
        List<String> result = panelCategoryService.getRoleTypes(caseData);
        assertThat(result).isEmpty();
    }

    @DisplayName("getRoleTypes should return an empty list when feature flag is turned off and case is not IIDB or CS")
    @Test
    public void testGetRolesWithFeatureFlagOff() {
        ReflectionTestUtils.setField(panelCategoryService, "defaultPanelCompEnabled", false);
        List<String> result = panelCategoryService.getRoleTypes(caseData);
        assertThat(result).isEmpty();
    }

    @DisplayName("getRoleTypes should return a medical member when specialism is on casedata")
    @Test
    public void testGetRolesWithSpecialism() {
        caseData.getSscsIndustrialInjuriesData().setPanelDoctorSpecialism("dummy");
        caseData.setBenefitCode("067");
        caseData.setIssueCode("CB");
        List<String> result = panelCategoryService.getRoleTypes(caseData);
        assertThat(result).isNotEmpty();
        assertThat(result.stream().anyMatch(TRIBUNAL_MEMBER_MEDICAL::equals)).isTrue();
    }

    @DisplayName("getRoleTypes should return FQPM when FQPM = yes on casedata")
    @Test
    public void testGetRolesWithFQPM() {
        caseData.setIsFqpmRequired(YesNo.YES);
        caseData.setBenefitCode("016");
        caseData.setIssueCode("CC");
        List<String> result = panelCategoryService.getRoleTypes(caseData);
        assertThat(result).isNotEmpty();
        assertThat(result.stream().anyMatch(TRIBUNALS_MEMBER_FINANCIALLY_QUALIFIED::equals)).isTrue();
    }

}
