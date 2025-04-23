package uk.gov.hmcts.reform.sscs.reference.data.service;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.NO;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.YES;
import static uk.gov.hmcts.reform.sscs.reference.data.service.PanelCategoryService.DISTRICT_TRIBUNAL_JUDGE;
import static uk.gov.hmcts.reform.sscs.reference.data.service.PanelCategoryService.REGIONAL_MEMBER_MEDICAL;
import static uk.gov.hmcts.reform.sscs.reference.data.service.PanelCategoryService.TRIBUNAL_JUDGE;
import static uk.gov.hmcts.reform.sscs.reference.data.service.PanelCategoryService.TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED;
import static uk.gov.hmcts.reform.sscs.reference.data.service.PanelCategoryService.TRIBUNAL_MEMBER_MEDICAL;
import static uk.gov.hmcts.reform.sscs.reference.data.service.PanelCategoryService.mapPanelMemberCompositionToRoleTypes;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberComposition;
import uk.gov.hmcts.reform.sscs.ccd.domain.ReserveTo;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsIndustrialInjuriesData;
import uk.gov.hmcts.reform.sscs.reference.data.model.PanelCategory;

public class PanelCategoryServiceTest {

    private final PanelCategoryService panelCategoryService = new PanelCategoryService();
    private SscsCaseData caseData;


    @BeforeEach
    public void setUp() throws Exception {
        caseData = SscsCaseData.builder()
                .sscsIndustrialInjuriesData(SscsIndustrialInjuriesData.builder().build())
                .benefitCode("001")
                .ccdCaseId("1234")
                .issueCode("AD")
                .build();
    }

    @DisplayName("Valid call to getPanelCategory should return correct johTier")
    @Test
    public void getPanelCategory(){
        PanelCategory result = panelCategoryService.getPanelCategory("001AD", null, null);
        assertThat(result).isNotNull();
        assertThat(result.getBenefitIssueCode()).isEqualTo("001AD");
        assertThat(result.getJohTiers()).isNotEmpty();
        assertThat(result.getJohTiers().get(0)).isEqualTo("84");
    }

    @DisplayName("Invalid call to getPanelCategory should return null")
    @Test
    public void getPanelCategoryWithInvalidParameters() {
        PanelCategory result = panelCategoryService.getPanelCategory(null, null, null);
        assertThat(result).isNull();
    }

    @DisplayName("Call to getPanelCategory with FQPM should return correct johTier")
    @Test
    public void getPanelCategoryWithFQPM() {
        PanelCategory result = panelCategoryService.getPanelCategory("016CC", null, "true");
        assertThat(result).isNotNull();
        assertThat(result.getJohTiers().stream().anyMatch(TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED::equals)).isTrue();
    }

    @DisplayName("Call to getPanelCategory with specialism should return correct johTier")
    @Test
    public void getPanelCategoryWithSpecialism() {
        PanelCategory oneSpecialismResult = panelCategoryService.getPanelCategory("067CB", "1", null);
        PanelCategory twoSpecialismResult = panelCategoryService.getPanelCategory("067CB", "2", null);
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
        caseData.setIsFqpmRequired(YES);
        caseData.setBenefitCode("016");
        caseData.setIssueCode("CC");
        List<String> result = panelCategoryService.getRoleTypes(caseData);
        assertThat(result).isNotEmpty();
        assertThat(result.stream().anyMatch(TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED::equals)).isTrue();
    }


    @DisplayName("getRoleTypes should save to case data when savePanelComposition is true")
    @Test
    public void testGetRolesWithSavePanelComposition() {
        panelCategoryService.setPanelMemberComposition(caseData, List.of(TRIBUNAL_JUDGE));//true

        assertThat(caseData.getPanelMemberComposition()).isNotNull();
    }

    @DisplayName("getRoleTypes should not save to case data when savePanelComposition is false")
    @Test
    public void testGetRolesWithoutSavePanelComposition() {
        List<String> result = panelCategoryService.getRoleTypes(caseData);
        assertThat(result).isNotEmpty();
        assertThat(caseData.getPanelMemberComposition()).isNull();
    }

    @DisplayName("getRoleTypes should use panelMemberComposition when it exists in case data")
    @Test
    public void testGetRolesWithPanelCompositionInCaseData() {
        caseData.setPanelMemberComposition(PanelMemberComposition.builder().panelCompositionMemberMedical1(REGIONAL_MEMBER_MEDICAL).build());
        List<String> result = panelCategoryService.getRoleTypes(caseData);
        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo(List.of(REGIONAL_MEMBER_MEDICAL));
    }

    @DisplayName("getRoleTypes should return District tribunal judge when it is set to yes in case data")
    @Test
    public void testGetRolesWithDTJ() {
        caseData.setPanelMemberComposition(PanelMemberComposition.builder().build());
        caseData.getSchedulingAndListingFields().setReserveTo(ReserveTo.builder().reservedDistrictTribunalJudge(YES).build());
        List<String> result = panelCategoryService.getRoleTypes(caseData);
        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo(List.of(DISTRICT_TRIBUNAL_JUDGE));
    }

    @DisplayName("mapPanelMemberCompositionToRoleTypes should extract the JOHTiers within panelComposition object")
    @Test
    public void mapPanelMemberCompositionToRoleTypesShouldExtractPanelCompositionIntoListOfStrings() {
        PanelMemberComposition panelMemberComposition = PanelMemberComposition.builder()
                .panelCompositionJudge(TRIBUNAL_JUDGE)
                .panelCompositionMemberMedical1(TRIBUNAL_MEMBER_MEDICAL)
                .panelCompositionMemberMedical2(REGIONAL_MEMBER_MEDICAL)
                .panelCompositionDisabilityAndFqMember(List.of(TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED)).build();
        List<String> result = mapPanelMemberCompositionToRoleTypes(panelMemberComposition, NO);
        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo(List.of(TRIBUNAL_JUDGE, TRIBUNAL_MEMBER_MEDICAL, REGIONAL_MEMBER_MEDICAL, TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED));
    }

    @DisplayName("mapPanelMemberCompositionToRoleTypes should return an empty list when there is nothing to map")
    @Test
    public void mapPanelMemberCompositionToRoleTypesShouldReturnEmptyListWhenFieldsAreEmpty() {
        PanelMemberComposition panelMemberComposition = PanelMemberComposition.builder().build();
        List<String> result = mapPanelMemberCompositionToRoleTypes(panelMemberComposition, NO);
        assertThat(result).isEmpty();
    }

    @DisplayName("mapPanelMemberCompositionToRoleTypes should return DTJ code when reservedToDistrictTribunalJudge is true")
    @Test
    public void mapPanelMemberCompositionShouldIncludeDTJWhenSetToYes() {
        PanelMemberComposition panelMemberComposition = PanelMemberComposition.builder().build();

        List<String> result = mapPanelMemberCompositionToRoleTypes(panelMemberComposition, YES);
        
        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo(List.of(DISTRICT_TRIBUNAL_JUDGE));
    }

}
