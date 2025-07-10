package uk.gov.hmcts.reform.sscs.reference.data.service;

import static java.util.Collections.frequency;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.DISTRICT_TRIBUNAL_JUDGE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.REGIONAL_MEDICAL_MEMBER;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.TRIBUNAL_JUDGE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.TRIBUNAL_MEMBER_DISABILITY;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.TRIBUNAL_MEMBER_MEDICAL;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.NO;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.YES;
import static uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils.buildCaseData;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.ElementDisputed;
import uk.gov.hmcts.reform.sscs.ccd.domain.ElementDisputedDetails;
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
                .benefitCode("002")
                .ccdCaseId("1234")
                .issueCode("EI")
                .build();
    }

    @DisplayName("Valid call to getRoleTypes should return correct johTier")
    @Test
    public void getDefaultPanelComposition(){
        var result = panelCompositionService
                .getRoleTypes(SscsCaseData.builder().benefitCode("002").issueCode("EI").build());

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).containsOnly("84");
    }

    @DisplayName("Should return correct panelComposition for valid issue benefit code combination")
    @Test
    public void shouldReturnPanelCompositionForValidIssueBenefitCode(){
        var result = panelCompositionService
                .createPanelComposition(SscsCaseData.builder().benefitCode("002").issueCode("EI").build());

        assertThat(result).isNotNull();
        assertEquals(TRIBUNAL_JUDGE.toRef(), result.getPanelCompositionJudge());
    }

    @DisplayName("Should return correct panelComposition for single UC issue benefit code combination")
    @Test
    public void shouldReturnPanelCompositionForValidSingleUcIssueBenefitCode(){

        ElementDisputed disputedElement1 = ElementDisputed.builder().value(ElementDisputedDetails.builder().issueCode("CX").build())
                .build();

        var result = panelCompositionService
                .createPanelComposition(SscsCaseData.builder()
                        .elementsDisputedGeneral(List.of(disputedElement1))
                        .benefitCode("001").issueCode("US").build());

        assertThat(result).isNotNull();
        assertThat(result.getPanelCompositionJudge()).isEqualTo(TRIBUNAL_JUDGE.toRef());
        assertThat(result.getPanelCompositionMemberMedical1()).isNull();
        assertThat(result.getPanelCompositionDisabilityAndFqMember()).isEmpty();
    }


    @DisplayName("Should return correct panelComposition for multiple UC issue benefit code combination")
    @Test
    public void shouldReturnPanelCompositionForValidMultipleUcIssueBenefitCode(){

        ElementDisputed disputedElement1 = ElementDisputed.builder().value(ElementDisputedDetails.builder().issueCode("CX").build())
                .build();
        ElementDisputed disputedElement2 = ElementDisputed.builder().value(ElementDisputedDetails.builder().issueCode("SG").build())
                .build();
        ElementDisputed disputedElement3 = ElementDisputed.builder().value(ElementDisputedDetails.builder().issueCode("HT").build())
                .build();

        var result = panelCompositionService
                .createPanelComposition(SscsCaseData.builder()
                        .elementsDisputedGeneral(List.of(disputedElement1, disputedElement2))
                        .elementsDisputedHousing(List.of(disputedElement3))
                        .benefitCode("001").issueCode("UM").build());

        assertThat(result).isNotNull();
        assertThat(result.getPanelCompositionJudge()).isEqualTo(TRIBUNAL_JUDGE.toRef());
        assertThat(result.getPanelCompositionMemberMedical1()).isEqualTo(TRIBUNAL_MEMBER_MEDICAL.toRef());
        assertThat(result.getPanelCompositionDisabilityAndFqMember()).isEmpty();
    }

    @DisplayName("Invalid call to getRoleTypes should return null")
    @Test
    public void getDefaultPanelCompositionWithInvalidParameters() {
        var result = panelCompositionService.getRoleTypes(buildCaseData());
        assertThat(result).isEmpty();
    }

    @DisplayName("should return emptyPanelComposition")
    @Test
    public void shouldReturnEmptyPanelComposition() {
        var result = panelCompositionService.createPanelComposition(buildCaseData());
        assertTrue(result.isEmpty());
    }

    @DisplayName("Call to getRoleTypes with FQPM should return correct johTier")
    @Test
    public void getDefaultPanelCompositionWithFQPM() {
        var result = panelCompositionService.getRoleTypes(
                SscsCaseData.builder().benefitCode("016").issueCode("CC").isFqpmRequired(YES).build()
        );

        assertThat(result).isNotNull();
        assertThat(result).contains(TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef());
    }

    @Test
    @DisplayName("Call to getRoleTypes with medical member should return correct johTier")
    public void getPanelCategoryWithMedicalMember() {
        var result = panelCompositionService.getRoleTypes(
                SscsCaseData.builder().benefitCode("093").issueCode("CE").isMedicalMemberRequired(YES).build());
        assertThat(result).isNotNull();
        assertThat(result).contains(TRIBUNAL_MEMBER_MEDICAL.toRef());
    }

    @Test
    @DisplayName("Call to getRoleTypes with FQPM and medical member should return correct johTier")
    public void getPanelCategoryWithFqpmAndMedicalMember() {
        var result = panelCompositionService.getRoleTypes(
                SscsCaseData.builder().benefitCode("093").issueCode("CE")
                        .isFqpmRequired(YES).isMedicalMemberRequired(YES).build());
        assertThat(result).isNotNull();
        assertThat(result).contains(TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef());
        assertThat(result).contains(TRIBUNAL_MEMBER_MEDICAL.toRef());
    }

    @Test
    @DisplayName("Call to getRoleTypes with no FQPM and medical member should return correct johTier")
    public void getPanelCategoryWithNoFqpmAndMedicalMember() {
        var result = panelCompositionService.getRoleTypes(
                SscsCaseData.builder().benefitCode("093").issueCode("CE")
                        .isFqpmRequired(NO).isMedicalMemberRequired(YES).build());
        assertThat(result).isNotNull();
        assertThat(result).doesNotContain(TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef());
        assertThat(result).contains(TRIBUNAL_MEMBER_MEDICAL.toRef());
    }

    @DisplayName("Call to getRoleTypes with specialism should return correct johTier")
    @Test
    public void getDefaultPanelCompositionWithSpecialism() {
        var oneSpecialismResult = panelCompositionService
                .getRoleTypes(SscsCaseData.builder().benefitCode("031").issueCode("AA")
                        .sscsIndustrialInjuriesData(SscsIndustrialInjuriesData.builder()
                                .panelDoctorSpecialism("one").build()).build());
        var twoSpecialismResult = panelCompositionService
                .getRoleTypes(SscsCaseData.builder().benefitCode("031").issueCode("AA")
                        .sscsIndustrialInjuriesData(SscsIndustrialInjuriesData.builder()
                                .panelDoctorSpecialism("one").secondPanelDoctorSpecialism("two").build())
                        .build());

        assertNotNull(oneSpecialismResult);
        assertNotNull(twoSpecialismResult);
        assertThat(oneSpecialismResult).containsOnlyOnce(TRIBUNAL_MEMBER_MEDICAL.toRef());
        assertEquals(2, frequency(twoSpecialismResult, TRIBUNAL_MEMBER_MEDICAL.toRef()));
    }

    @DisplayName("getRoleTypes should return a medical member when specialism is on casedata")
    @Test
    public void testGetRolesWithSpecialism() {
        caseData.getSscsIndustrialInjuriesData().setPanelDoctorSpecialism("dummy");
        caseData.setBenefitCode("067");
        caseData.setIssueCode("CB");
        List<String> result = panelCompositionService.getRoleTypes(caseData);
        assertThat(result).isNotEmpty();
        assertThat(result).contains(TRIBUNAL_MEMBER_MEDICAL.toRef());
    }

    @DisplayName("getRoleTypes should save to case data when savePanelComposition is true")
    @Test
    public void testGetRolesWithSavePanelComposition() {
        var updatedPanelMemberComposition =
                panelCompositionService.createPanelCompositionFromJohTiers(List.of(TRIBUNAL_JUDGE.toRef()));

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

    @DisplayName("createPanelCompositionFromJohTiers should extract the JOHTiers within panelComposition object")
    @Test
    public void getJohTiersFromPanelCompositionShouldExtractPanelCompositionIntoListOfStringsFromPanelComposition() {
        PanelMemberComposition panelMemberComposition = PanelMemberComposition.builder()
                .panelCompositionJudge(TRIBUNAL_JUDGE.toRef())
                .panelCompositionMemberMedical1(REGIONAL_MEDICAL_MEMBER.toRef())
                .panelCompositionMemberMedical2(TRIBUNAL_MEMBER_MEDICAL.toRef())
                .panelCompositionDisabilityAndFqMember(List.of(TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef())).build();
        caseData.setPanelMemberComposition(panelMemberComposition);
        caseData.getSscsIndustrialInjuriesData().setPanelDoctorSpecialism("Dummy");
        caseData.getSscsIndustrialInjuriesData().setSecondPanelDoctorSpecialism("Dummy");

        List<String> roleTypes = panelCompositionService.getRoleTypes(caseData);

        assertThat(roleTypes).isNotEmpty();
        assertThat(roleTypes)
                .containsAll(List.of(TRIBUNAL_JUDGE.toRef(), TRIBUNAL_MEMBER_MEDICAL.toRef(),
                        REGIONAL_MEDICAL_MEMBER.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef()));
    }

    @DisplayName("createPanelCompositionFromJohTiers should return an empty list when there is nothing to map")
    @Test
    public void getJohTiersFromPanelCompositionShouldReturnEmptyListWhenFieldsAreEmpty() {
        PanelMemberComposition panelMemberComposition = PanelMemberComposition.builder()
                .panelCompositionDisabilityAndFqMember(new ArrayList<>()).build();
        var caseData = buildCaseData();
        caseData.setPanelMemberComposition(panelMemberComposition);
        List<String> result = panelCompositionService.getRoleTypes(caseData);
        assertThat(result).isEmpty();
    }

    @DisplayName("getRoleType should return default values when there is nothing to map")
    @Test
    public void getRoleTypeShouldNotReturnEmptyListWhenPanelCompositionFieldsAreEmpty() {
        PanelMemberComposition panelMemberComposition = PanelMemberComposition.builder()
                .panelCompositionDisabilityAndFqMember(new ArrayList<>()).build();
        caseData.setPanelMemberComposition(panelMemberComposition);
        List<String> result = panelCompositionService.getRoleTypes(caseData);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(List.of("84"));
    }

    @DisplayName("getRoleTypes should return an empty list when there is nothing to map")
    @Test
    public void getRoleTypesShouldReturnDtjWhenPanelCompositionFieldsAreEmptyButReserveTpDistrictJudgeSelected() {
        PanelMemberComposition panelMemberComposition = PanelMemberComposition.builder()
                .panelCompositionDisabilityAndFqMember(new ArrayList<>()).build();
        caseData.getSchedulingAndListingFields()
                .setReserveTo(ReserveTo.builder().reservedDistrictTribunalJudge(YES).build());
        caseData.setPanelMemberComposition(panelMemberComposition);

        List<String> result = panelCompositionService.getRoleTypes(caseData);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(List.of("90000"));
    }

    @DisplayName("createPanelCompositionFromJohTiers should return a district tribunal judge when they are reserved")
    @Test
    public void getJohTiersFromPanelCompositionShouldReturnDtjWhenItIsReserved() {
        PanelMemberComposition panelMemberComposition =
                PanelMemberComposition.builder().panelCompositionJudge(TRIBUNAL_JUDGE.toRef()).build();
        caseData.getSchedulingAndListingFields().setReserveTo(ReserveTo.builder()
                .reservedDistrictTribunalJudge(YES).build());
        caseData.setPanelMemberComposition(panelMemberComposition);

        List<String> result = panelCompositionService.getRoleTypes(caseData);

        assertThat(result).isEqualTo(List.of(DISTRICT_TRIBUNAL_JUDGE.toRef()));
    }

    @DisplayName("should not throw exception if reservedDistrictTribunalJudge is null")
    @Test
    public void getJohTiersFromPanelCompositionShouldNotThrowExceptionIfReservedDistrictTribunalJudgeIsNull() {
        PanelMemberComposition panelMemberComposition =
                PanelMemberComposition.builder().panelCompositionJudge(TRIBUNAL_JUDGE.toRef()).build();
        caseData.getSchedulingAndListingFields().setReserveTo(ReserveTo.builder().build());
        caseData.setPanelMemberComposition(panelMemberComposition);

        assertDoesNotThrow(() -> panelCompositionService.getRoleTypes(caseData));
    }

    @DisplayName("getJohTiersFromPanelComposition should return fqpm and disability expert when present")
    @Test
    public void getJohTiersFromPanelCompositionShouldReturnFqpmAndDisabilityList() {
        PanelMemberComposition panelMemberComposition = PanelMemberComposition.builder()
                .panelCompositionDisabilityAndFqMember(
                        List.of(TRIBUNAL_MEMBER_DISABILITY.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef())
                ).build();
        caseData.getSchedulingAndListingFields().setReserveTo(ReserveTo.builder()
                .reservedDistrictTribunalJudge(YES).build());
        caseData.setPanelMemberComposition(panelMemberComposition);

        List<String> result = panelCompositionService.getRoleTypes(caseData);

        assertThat(result).containsAll(List.of(DISTRICT_TRIBUNAL_JUDGE.toRef(), TRIBUNAL_MEMBER_DISABILITY.toRef(),
                TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef()));
    }

    @DisplayName("Populate Panel composition from role types with No medical member")
    @Test
    public void createPanelCompositionFromJohTiersNoMedicalMember() {
        var roleTypes = List.of(TRIBUNAL_JUDGE.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef(),
                TRIBUNAL_MEMBER_DISABILITY.toRef());
        var panelComposition = PanelMemberComposition.builder()
                .panelCompositionJudge(TRIBUNAL_JUDGE.toRef())
                .panelCompositionDisabilityAndFqMember(
                        List.of(TRIBUNAL_MEMBER_DISABILITY.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef())
                ).build();

        var result = panelCompositionService.createPanelCompositionFromJohTiers(roleTypes);

        assertEqualsPanelComposition(panelComposition, result);
    }

    @DisplayName("Populate Panel composition from role types with one medical member")
    @Test
    public void createPanelCompositionFromJohTiersWithOneMedicalMember() {
        var roleTypes = List.of(TRIBUNAL_JUDGE.toRef(), TRIBUNAL_MEMBER_MEDICAL.toRef(),
                TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef(), TRIBUNAL_MEMBER_DISABILITY.toRef());
        var panelComposition = PanelMemberComposition.builder()
                .panelCompositionJudge(TRIBUNAL_JUDGE.toRef())
                .panelCompositionMemberMedical1(TRIBUNAL_MEMBER_MEDICAL.toRef())
                .panelCompositionDisabilityAndFqMember(
                        List.of(TRIBUNAL_MEMBER_DISABILITY.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef())
                ).build();

        var result = panelCompositionService.createPanelCompositionFromJohTiers(roleTypes);

        assertEqualsPanelComposition(panelComposition, result);
    }

    @DisplayName("Populate Panel composition from role types with two medical members")
    @Test
    public void createPanelCompositionFromJohTiersWithTwoMedicalMembers() {
        var roleTypes = List.of(TRIBUNAL_JUDGE.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef(),
                TRIBUNAL_MEMBER_MEDICAL.toRef(),TRIBUNAL_MEMBER_MEDICAL.toRef(), TRIBUNAL_MEMBER_DISABILITY.toRef());
        var panelComposition = PanelMemberComposition.builder()
                .panelCompositionJudge(TRIBUNAL_JUDGE.toRef())
                .panelCompositionMemberMedical1(TRIBUNAL_MEMBER_MEDICAL.toRef())
                .panelCompositionMemberMedical2(TRIBUNAL_MEMBER_MEDICAL.toRef())
                .panelCompositionDisabilityAndFqMember(
                        List.of(TRIBUNAL_MEMBER_DISABILITY.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef())
                ).build();

        var result = panelCompositionService.createPanelCompositionFromJohTiers(roleTypes);

        assertEqualsPanelComposition(panelComposition, result);
    }

    @DisplayName("Populate Panel composition from role types with regional and tribunal medical members")
    @Test
    public void createPanelCompositionFromJohTiersWithRegionalAndTribunalMedicalMembers() {
        var roleTypes = List.of(TRIBUNAL_JUDGE.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef(),
                TRIBUNAL_MEMBER_MEDICAL.toRef(),REGIONAL_MEDICAL_MEMBER.toRef(), TRIBUNAL_MEMBER_DISABILITY.toRef());
        var panelComposition = PanelMemberComposition.builder()
                .panelCompositionJudge(TRIBUNAL_JUDGE.toRef())
                .panelCompositionMemberMedical1(REGIONAL_MEDICAL_MEMBER.toRef())
                .panelCompositionMemberMedical2(TRIBUNAL_MEMBER_MEDICAL.toRef())
                .panelCompositionDisabilityAndFqMember(
                        List.of(TRIBUNAL_MEMBER_DISABILITY.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef())
                ).build();

        var result = panelCompositionService.createPanelCompositionFromJohTiers(roleTypes);

        assertEqualsPanelComposition(panelComposition, result);
    }

    @Test
    public void resetMedicalMembersWhenSpecialismCountIsChangedFromTwoToOne() {
        PanelMemberComposition panelMemberComposition = PanelMemberComposition.builder().panelCompositionJudge("84")
                .panelCompositionMemberMedical1("58")
                .panelCompositionMemberMedical2("58")
                .panelCompositionDisabilityAndFqMember(List.of("44")).build();
        caseData.setPanelMemberComposition(panelMemberComposition);
        caseData.getSscsIndustrialInjuriesData().setPanelDoctorSpecialism("dummy");
        List<String> result = panelCompositionService.getRoleTypes(caseData);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(List.of("84","58","44"));
    }

    @Test
    public void resetMedicalMembersWhenSpecialismCountIsChangedFromOneToTwo() {
        PanelMemberComposition panelMemberComposition = PanelMemberComposition.builder().panelCompositionJudge("84")
                .panelCompositionMemberMedical1("58")
                .panelCompositionDisabilityAndFqMember(List.of("44")).build();
        caseData.setPanelMemberComposition(panelMemberComposition);
        caseData.getSscsIndustrialInjuriesData().setPanelDoctorSpecialism("dummy");
        caseData.getSscsIndustrialInjuriesData().setSecondPanelDoctorSpecialism("dummy");
        List<String> result = panelCompositionService.getRoleTypes(caseData);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(List.of("84","58","58","44"));
    }

    @Test
    public void resetMedicalMembersWhenSpecialismCountIsChangedFromOneToTwoWithNoMedicalMember() {
        PanelMemberComposition panelMemberComposition = PanelMemberComposition.builder().panelCompositionJudge("84")
                .panelCompositionDisabilityAndFqMember(List.of("44")).build();
        caseData.setPanelMemberComposition(panelMemberComposition);
        caseData.getSscsIndustrialInjuriesData().setPanelDoctorSpecialism("dummy");
        caseData.getSscsIndustrialInjuriesData().setSecondPanelDoctorSpecialism("dummy");
        List<String> result = panelCompositionService.getRoleTypes(caseData);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(List.of("84","58","58","44"));
    }

    private void assertEqualsPanelComposition(PanelMemberComposition expected, PanelMemberComposition actual) {
        assertEquals(expected.getPanelCompositionJudge(), actual.getPanelCompositionJudge());
        assertEquals(expected.getPanelCompositionMemberMedical1(), actual.getPanelCompositionMemberMedical1());
        assertEquals(expected.getPanelCompositionMemberMedical2(), actual.getPanelCompositionMemberMedical2());
        assertTrue(expected.getPanelCompositionDisabilityAndFqMember().containsAll(actual.getPanelCompositionDisabilityAndFqMember()));
    }

    @DisplayName("IsValidBenefitIssueCode should return true when benefit issue code is in panel map")
    @Test
    public void testIsValidBenefitIssueCode() {
        boolean result = panelCompositionService.isBenefitIssueCodeValid("001", "AD");
        assertThat(result).isTrue();
    }

    @DisplayName("IsValidBenefitIssueCode should return false when benefit issue code is not in panel map")
    @Test
    public void testIsValidBenefitIssueCodeForInvalidBenefitIssueCode() {
        boolean result = panelCompositionService.isBenefitIssueCodeValid("000", "00");
        assertThat(result).isFalse();
    }

}
