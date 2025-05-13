package uk.gov.hmcts.reform.sscs.reference.data.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.DISTRICT_TRIBUNAL_JUDGE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.REGIONAL_MEDICAL_MEMBER;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.TRIBUNAL_JUDGE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.TRIBUNAL_MEMBER_DISABILITY;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.TRIBUNAL_MEMBER_MEDICAL;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.YES;
import static uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils.buildCaseData;
import static uk.gov.hmcts.reform.sscs.reference.data.service.PanelCompositionService.getRoleTypesFromPanelComposition;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberComposition;
import uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType;
import uk.gov.hmcts.reform.sscs.ccd.domain.ReserveTo;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsIndustrialInjuriesData;
import uk.gov.hmcts.reform.sscs.reference.data.model.DefaultPanelComposition;

public class PanelCompositionServiceTest {

    private final PanelCompositionService panelCompositionService = new PanelCompositionService();
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
    public void getDefaultPanelComposition(){
        DefaultPanelComposition result = panelCompositionService
                .getDefaultPanelComposition(SscsCaseData.builder().benefitCode("001").issueCode("AD").build());

        assertThat(result).isNotNull();
        assertThat(result.getBenefitIssueCode()).isEqualTo("001AD");
        assertThat(result.getJohTiers()).isNotEmpty();
        assertThat(result.getJohTiers().get(0)).isEqualTo("84");
    }

    @DisplayName("Invalid call to getPanelCategory should return null")
    @Test
    public void getDefaultPanelCompositionWithInvalidParameters() {
        DefaultPanelComposition result = panelCompositionService.getDefaultPanelComposition(buildCaseData());
        assertThat(result).isNull();
    }

    @DisplayName("Call to getPanelCategory with FQPM should return correct johTier")
    @Test
    public void getDefaultPanelCompositionWithFQPM() {
        DefaultPanelComposition result = panelCompositionService.getDefaultPanelComposition(
                SscsCaseData.builder().benefitCode("016").issueCode("CC").isFqpmRequired(YES).build()
        );

        assertThat(result).isNotNull();
        assertThat(result.getJohTiers()).contains(TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef());
    }

    @DisplayName("Call to getPanelCategory with specialism should return correct johTier")
    @Test
    public void getDefaultPanelCompositionWithSpecialism() {
        DefaultPanelComposition oneSpecialismResult = panelCompositionService
                .getDefaultPanelComposition(SscsCaseData.builder().benefitCode("031").issueCode("AA")
                        .sscsIndustrialInjuriesData(SscsIndustrialInjuriesData.builder()
                                .panelDoctorSpecialism("one").build()).build());
        DefaultPanelComposition twoSpecialismResult = panelCompositionService
                .getDefaultPanelComposition(SscsCaseData.builder().benefitCode("031").issueCode("AA")
                        .sscsIndustrialInjuriesData(SscsIndustrialInjuriesData.builder()
                                .panelDoctorSpecialism("one").secondPanelDoctorSpecialism("two").build())
                        .build());

        assertNotNull(oneSpecialismResult);
        assertNotNull(twoSpecialismResult);
        assertThat(oneSpecialismResult.getJohTiers()).containsOnlyOnce(TRIBUNAL_MEMBER_MEDICAL.toRef());
        assertEquals(2,
                twoSpecialismResult.getJohTiers().stream().filter(TRIBUNAL_MEMBER_MEDICAL.toRef()::equals).count());
    }

    @DisplayName("getRoleTypes returns an valid list")
    @Test
    public void testGetRoles() {
        List<String> result = panelCompositionService.getRoleTypes(caseData);
        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isEqualTo("84");
    }

    @DisplayName("getRoleTypes should return an empty list when an invalid benefit issue code is given")
    @Test
    public void testGetRolesWithInvalidBenefitIssueCode() {
        caseData.setBenefitCode(null);
        caseData.setIssueCode(null);
        List<String> result = panelCompositionService.getRoleTypes(caseData);
        assertThat(result).isEmpty();
    }


    @DisplayName("getRoleTypes should return a medical member when specialism is on casedata")
    @Test
    public void testGetRolesWithSpecialism() {
        caseData.getSscsIndustrialInjuriesData().setPanelDoctorSpecialism("dummy");
        caseData.setBenefitCode("067");
        caseData.setIssueCode("CB");
        List<String> result = panelCompositionService.getRoleTypes(caseData);
        assertThat(result).isNotEmpty();
        assertThat(result.stream().anyMatch(TRIBUNAL_MEMBER_MEDICAL.toRef()::equals)).isTrue();
    }

    @DisplayName("getRoleTypes should return FQPM when FQPM = yes on casedata")
    @Test
    public void testGetRolesWithFQPM() {
        caseData.setIsFqpmRequired(YES);
        caseData.setBenefitCode("016");
        caseData.setIssueCode("CC");
        List<String> result = panelCompositionService.getRoleTypes(caseData);
        assertThat(result).isNotEmpty();
        assertThat(result.stream().anyMatch(TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef()::equals)).isTrue();
    }


    @DisplayName("getRoleTypes should save to case data when savePanelComposition is true")
    @Test
    public void testGetRolesWithSavePanelComposition() {
        var updatedPanelMemberComposition =
                panelCompositionService.getPanelCompositionFromRoleTypes(List.of(TRIBUNAL_JUDGE.toRef()));

        assertEquals(TRIBUNAL_JUDGE.toRef(), updatedPanelMemberComposition.getPanelCompositionJudge());
    }

    @DisplayName("getRoleTypes should not save to case data when savePanelComposition is false")
    @Test
    public void testGetRolesWithoutSavePanelComposition() {
        List<String> result = panelCompositionService.getRoleTypes(caseData);
        assertThat(result).isNotEmpty();
        assertThat(caseData.getPanelMemberComposition()).isNull();
    }

    @DisplayName("getRoleTypes should use panelMemberComposition when it exists in case data")
    @Test
    public void testGetRolesWithPanelCompositionInCaseData() {
        caseData.setPanelMemberComposition(PanelMemberComposition.builder()
                .panelCompositionMemberMedical1(REGIONAL_MEDICAL_MEMBER.toRef()).build());
        List<String> result = panelCompositionService.getRoleTypes(caseData);
        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo(List.of(REGIONAL_MEDICAL_MEMBER.toRef()));
    }

    @DisplayName("mapPanelMemberCompositionToRoleTypes should extract the JOHTiers within panelComposition object")
    @Test
    public void getRoleTypesFromPanelCompositionShouldExtractPanelCompositionIntoListOfStringsFromPanelComposition() {
        PanelMemberComposition panelMemberComposition = PanelMemberComposition.builder()
                .panelCompositionJudge(TRIBUNAL_JUDGE.toRef())
                .panelCompositionMemberMedical1(TRIBUNAL_MEMBER_MEDICAL.toRef())
                .panelCompositionMemberMedical2(REGIONAL_MEDICAL_MEMBER.toRef())
                .panelCompositionDisabilityAndFqMember(List.of(TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef())).build();
        List<String> roleTypes = getRoleTypesFromPanelComposition(panelMemberComposition, caseData);
        assertThat(roleTypes).isNotEmpty();
        assertEquals(roleTypes.stream().map(PanelMemberType::getPanelMemberType).toList(),
                List.of(TRIBUNAL_JUDGE, TRIBUNAL_MEMBER_MEDICAL, REGIONAL_MEDICAL_MEMBER,
                        TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED));
    }

    @DisplayName("mapPanelMemberCompositionToRoleTypes should return an empty list when there is nothing to map")
    @Test
    public void getRoleTypesFromPanelCompositionShouldReturnEmptyListWhenFieldsAreEmpty() {
        PanelMemberComposition panelMemberComposition = PanelMemberComposition.builder().build();
        List<String> result = getRoleTypesFromPanelComposition(panelMemberComposition,caseData);
        assertThat(result).isEmpty();
    }

    @DisplayName("mapPanelMemberCompositionToRoleTypes should return a district tribunal judge when they are reserved")
    @Test
    public void getRoleTypesFromPanelCompositionShouldReturnDtjWhenItIsReserved() {
        PanelMemberComposition panelMemberComposition = PanelMemberComposition.builder().build();
        caseData.getSchedulingAndListingFields().setReserveTo(ReserveTo.builder().reservedDistrictTribunalJudge(YES).build());
        List<String> result = getRoleTypesFromPanelComposition(panelMemberComposition,caseData);
        assertThat(result).isEqualTo(List.of(DISTRICT_TRIBUNAL_JUDGE.toRef()));
    }

    @DisplayName("Populate Panel composition from role types")
    @Test
    public void getPanelCompositionFromRoleTypes() {
        var roleTypes = List.of(TRIBUNAL_JUDGE.toRef(), TRIBUNAL_MEMBER_MEDICAL.toRef(),
                TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef(), TRIBUNAL_MEMBER_DISABILITY.toRef());
        var panelComposition = PanelMemberComposition.builder()
                .panelCompositionJudge(TRIBUNAL_JUDGE.toRef())
                .panelCompositionMemberMedical1(TRIBUNAL_MEMBER_MEDICAL.toRef())
                .panelCompositionDisabilityAndFqMember(
                        List.of(TRIBUNAL_MEMBER_DISABILITY.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef())
                ).build();

        var result = panelCompositionService.getPanelCompositionFromRoleTypes(roleTypes);

        assertEquals(panelComposition, result);
    }
}
