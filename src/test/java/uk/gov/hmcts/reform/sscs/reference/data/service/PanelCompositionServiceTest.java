package uk.gov.hmcts.reform.sscs.reference.data.service;

import static java.time.LocalDateTime.now;
import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.frequency;
import static java.util.List.of;
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
import static uk.gov.hmcts.reform.sscs.ccd.domain.State.READY_TO_LIST;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.NO;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.YES;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.CaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.ElementDisputed;
import uk.gov.hmcts.reform.sscs.ccd.domain.ElementDisputedDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberComposition;
import uk.gov.hmcts.reform.sscs.ccd.domain.ReserveTo;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsIndustrialInjuriesData;

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
    public void getDefaultPanelComposition() {
        var result = panelCompositionService
                .getRoleTypes(SscsCaseData.builder().benefitCode("002").issueCode("EI").build());

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).containsOnly("84");
    }

    @DisplayName("Should return correct panelComposition for valid issue benefit code combination")
    @Test
    public void shouldReturnPanelCompositionForValidIssueBenefitCode() {
        var defaultJohTiers = panelCompositionService
                .getDefaultPanelComposition(SscsCaseData.builder().benefitCode("002").issueCode("EI").build())
                .getJohTiers();
        var result = new PanelMemberComposition(defaultJohTiers);

        assertThat(result).isNotNull();
        assertEquals(TRIBUNAL_JUDGE.toRef(), result.getPanelCompositionJudge());
    }

    @DisplayName("Should return correct panelComposition for single UC issue benefit code combination")
    @Test
    public void shouldReturnPanelCompositionForValidSingleUcIssueBenefitCode() {
        SscsCaseData sscsCaseData = SscsCaseData.builder()
                .elementsDisputedGeneral(of(getElement("CX"))).benefitCode("001").issueCode("US").build();

        var defaultPanelComposition = panelCompositionService.getDefaultPanelComposition(sscsCaseData);
        var result = new PanelMemberComposition(defaultPanelComposition.getJohTiers());

        assertThat(result).isNotNull();
        assertThat(result.getPanelCompositionJudge()).isEqualTo(TRIBUNAL_JUDGE.toRef());
        assertThat(result.getPanelCompositionMemberMedical1()).isNull();
        assertThat(result.getPanelCompositionDisabilityAndFqMember()).isEmpty();
        assertThat(defaultPanelComposition.getCategory()).isEqualTo("1");
    }


    @DisplayName("Should return correct panelComposition for multiple UC issue benefit code combination")
    @Test
    public void shouldReturnPanelCompositionForValidMultipleUcIssueBenefitCode() {
        SscsCaseData sscsCaseData = SscsCaseData.builder()
                .elementsDisputedGeneral(of(getElement("CX"), getElement("SG")))
                .elementsDisputedHousing(of(getElement("HT")))
                .benefitCode("001").issueCode("UM").build();

        var defaultPanelComposition = panelCompositionService.getDefaultPanelComposition(sscsCaseData);
        var result = new PanelMemberComposition(defaultPanelComposition.getJohTiers());

        assertThat(result).isNotNull();
        assertThat(result.getPanelCompositionJudge()).isEqualTo(TRIBUNAL_JUDGE.toRef());
        assertThat(result.getPanelCompositionMemberMedical1()).isEqualTo(TRIBUNAL_MEMBER_MEDICAL.toRef());
        assertThat(result.getPanelCompositionDisabilityAndFqMember()).isEmpty();
        assertThat(defaultPanelComposition.getCategory()).isEqualTo("4");
    }

    @DisplayName("Invalid call to getRoleTypes should return null")
    @Test
    public void getDefaultPanelCompositionWithInvalidParameters() {
        var result = panelCompositionService.getRoleTypes(SscsCaseData.builder().issueCode("DD").build());
        assertThat(result).isEmpty();
    }

    @DisplayName("should return emptyPanelComposition")
    @Test
    public void shouldReturnEmptyPanelComposition() {
        var result = panelCompositionService.getDefaultPanelComposition(SscsCaseData.builder().issueCode("DD").build())
                .getJohTiers();
        assertTrue(result.isEmpty());
    }

    @DisplayName("Call to getRoleTypes with FQPM should return correct johTier")
    @Test
    public void getDefaultPanelCompositionWithFqpm() {
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
        assertThat(result).isEqualTo(of(REGIONAL_MEDICAL_MEMBER.toRef()));
    }

    @DisplayName("createPanelCompositionFromJohTiers should extract the JOHTiers within panelComposition object")
    @Test
    public void getJohTiersFromPanelCompositionShouldExtractPanelCompositionIntoListOfStringsFromPanelComposition() {
        PanelMemberComposition panelMemberComposition = PanelMemberComposition.builder()
                .panelCompositionJudge(TRIBUNAL_JUDGE.toRef())
                .panelCompositionMemberMedical1(REGIONAL_MEDICAL_MEMBER.toRef())
                .panelCompositionMemberMedical2(TRIBUNAL_MEMBER_MEDICAL.toRef())
                .panelCompositionDisabilityAndFqMember(of(TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef())).build();
        caseData.setPanelMemberComposition(panelMemberComposition);
        caseData.getSscsIndustrialInjuriesData().setPanelDoctorSpecialism("Dummy");
        caseData.getSscsIndustrialInjuriesData().setSecondPanelDoctorSpecialism("Dummy");

        List<String> roleTypes = panelCompositionService.getRoleTypes(caseData);

        assertThat(roleTypes).isNotEmpty();
        assertThat(roleTypes)
                .containsAll(of(TRIBUNAL_JUDGE.toRef(), TRIBUNAL_MEMBER_MEDICAL.toRef(),
                        REGIONAL_MEDICAL_MEMBER.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef()));
    }

    @DisplayName("createPanelCompositionFromJohTiers should return an empty list when there is nothing to map")
    @Test
    public void getJohTiersFromPanelCompositionShouldReturnEmptyListWhenFieldsAreEmpty() {
        PanelMemberComposition panelMemberComposition = PanelMemberComposition.builder()
                .panelCompositionDisabilityAndFqMember(new ArrayList<>()).build();
        var caseData = SscsCaseData.builder().issueCode("DD").build();
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
        assertThat(result).isEqualTo(of("84"));
    }

    @DisplayName("createPanelCompositionFromJohTiers should return a district tribunal judge when they are reserved")
    @Test
    public void getJohTiersFromPanelCompositionShouldReturnDtjWhenItIsReserved() {
        PanelMemberComposition panelMemberComposition =
                PanelMemberComposition.builder().districtTribunalJudge(DISTRICT_TRIBUNAL_JUDGE.toRef()).build();
        caseData.setPanelMemberComposition(panelMemberComposition);

        List<String> result = panelCompositionService.getRoleTypes(caseData);

        assertThat(result).isEqualTo(of(DISTRICT_TRIBUNAL_JUDGE.toRef()));
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
                .districtTribunalJudge(DISTRICT_TRIBUNAL_JUDGE.toRef())
                .panelCompositionDisabilityAndFqMember(
                        of(TRIBUNAL_MEMBER_DISABILITY.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef())
                ).build();
        caseData.setPanelMemberComposition(panelMemberComposition);

        List<String> result = panelCompositionService.getRoleTypes(caseData);

        assertThat(result).containsAll(of(DISTRICT_TRIBUNAL_JUDGE.toRef(), TRIBUNAL_MEMBER_DISABILITY.toRef(),
                TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef()));
    }

    @DisplayName("getDefaultPanelComposition should return correct JOH tiers")
    @Test
    public void getDefaultPanelCompositionShouldReturnCorrectJohTiers() {
        caseData.setBenefitCode("002");
        caseData.setIssueCode("LC");
        var johTiers = of("84", "58", "44");

        var result = panelCompositionService.getDefaultPanelComposition(caseData);

        assertEquals(johTiers, result.getJohTiers());
    }

    @DisplayName("getDefaultPanelComposition should return emptyList if no panelComp mapping exists")
    @Test
    public void getDefaultPanelCompositionShouldReturnEmptyListIfNoPanelCompMappingExists() {
        caseData.setBenefitCode("dummy");
        caseData.setIssueCode("dummy");

        var result = panelCompositionService.getDefaultPanelComposition(caseData);

        assertEquals(EMPTY_LIST, result.getJohTiers());
    }

    @DisplayName("resetPanelCompositionIfStale should reset panelComp if benefitCode changes")
    @Test
    public void resetPanelCompositionIfStaleShouldResetPanelCompIfBenefitCodeChanges() {
        var caseData = SscsCaseData.builder().benefitCode("002").build();
        var caseDetailsBefore =
                new CaseDetails<>(1234L, "SSCS", READY_TO_LIST, caseData, now(), "Benefit");
        var sscsCaseData = SscsCaseData.builder().benefitCode("022")
                .panelMemberComposition(PanelMemberComposition.builder().panelCompositionJudge("84").build()).build();

        var result = panelCompositionService.resetPanelCompositionIfStale(sscsCaseData, Optional.of(caseDetailsBefore));

        assertEquals(EMPTY_LIST, result.getJohTiers());
    }

    @DisplayName("resetPanelCompositionIfStale should reset panelComp if issueCode changes")
    @Test
    public void resetPanelCompositionIfStaleShouldResetPanelCompIfIssueCodeChanges() {
        var sscsCaseData = SscsCaseData.builder().issueCode("RA")
                .panelMemberComposition(PanelMemberComposition.builder().panelCompositionJudge("84").build()).build();
        var caseDataBefore = SscsCaseData.builder().issueCode("CC").build();
        var caseDetailsBefore =
                new CaseDetails<>(1234L, "SSCS", READY_TO_LIST, caseDataBefore, now(), "Benefit");

        var result = panelCompositionService.resetPanelCompositionIfStale(sscsCaseData, Optional.of(caseDetailsBefore));

        assertEquals(EMPTY_LIST, result.getJohTiers());
    }

    @DisplayName("resetPanelCompositionIfStale should reset panelComp if specialism changes")
    @Test
    public void resetPanelCompositionIfStaleShouldResetPanelCompIfSpecialismChanges() {
        var sscsCaseData = SscsCaseData.builder().sscsIndustrialInjuriesData(
                SscsIndustrialInjuriesData.builder().panelDoctorSpecialism("doctor").build())
                .panelMemberComposition(PanelMemberComposition.builder().panelCompositionJudge("84").build()).build();
        var caseDetailsBefore =
                new CaseDetails<>(1234L, "SSCS", READY_TO_LIST, SscsCaseData.builder().build(), now(), "Benefit");

        var result = panelCompositionService.resetPanelCompositionIfStale(sscsCaseData, Optional.of(caseDetailsBefore));

        assertEquals(EMPTY_LIST, result.getJohTiers());
    }

    @DisplayName("resetPanelCompositionIfStale should not reset panelComp if nothing changes")
    @Test
    public void resetPanelCompositionIfStaleShouldResetPanelCompIfNothingChanges() {
        var sscsCaseData = SscsCaseData.builder()
                .isFqpmRequired(YES).benefitCode("022").issueCode("RA")
                .sscsIndustrialInjuriesData(SscsIndustrialInjuriesData.builder().panelDoctorSpecialism("doc").build())
                .panelMemberComposition(PanelMemberComposition.builder().panelCompositionJudge("84")
                        .panelCompositionMemberMedical1("58").build()).build();
        var sscsCaseDataBefore = SscsCaseData.builder().isFqpmRequired(NO).benefitCode("022").issueCode("RA")
                .sscsIndustrialInjuriesData(SscsIndustrialInjuriesData.builder().panelDoctorSpecialism("doctor")
                        .build()).build();
        var caseDetailsBefore =
                new CaseDetails<>(1234L, "SSCS", READY_TO_LIST, sscsCaseDataBefore, now(), "Benefit");

        var result = panelCompositionService.resetPanelCompositionIfStale(sscsCaseData, Optional.of(caseDetailsBefore));

        assertThat(result.getJohTiers()).containsAll(of("84", "58"));
    }

    @DisplayName("should reset panelComp if UC elements change")
    @Test
    public void resetPanelCompositionIfUcElementsChange() {
        var sscsCaseData = SscsCaseData.builder().elementsDisputedGeneral(of(getElement("CC"))).build();
        var sscsCaseDataBefore = SscsCaseData.builder().elementsDisputedCare(of(getElement("RA"))).build();
        var caseDetailsBefore =
                new CaseDetails<>(1234L, "SSCS", READY_TO_LIST, sscsCaseDataBefore, now(), "Benefit");

        var result = panelCompositionService.resetPanelCompIfElementsChanged(sscsCaseData, Optional.of(caseDetailsBefore));

        assertEquals(EMPTY_LIST, result.getJohTiers());
    }

    @DisplayName("should not reset panelComp if nothing changes")
    @Test
    public void shouldNotResetPanelCompositionIfUcElementsChangeDontChange() {
        var sscsCaseData = SscsCaseData.builder()
                .elementsDisputedCare(of(getElement("CC"), getElement("UC")))
                .elementsDisputedGeneral(of(getElement("RA"))).build();
        var sscsCaseDataBefore = SscsCaseData.builder()
                .elementsDisputedCare(of(getElement("UC"), getElement("RA")))
                .elementsDisputedGeneral(of(getElement("CC"))).build();
        sscsCaseData.setPanelMemberComposition(PanelMemberComposition.builder()
                .panelCompositionJudge("84").panelCompositionMemberMedical1("58").build());
        var caseDetailsBefore =
                new CaseDetails<>(1234L, "SSCS", READY_TO_LIST, sscsCaseDataBefore, now(), "Benefit");

        var result = panelCompositionService.resetPanelCompIfElementsChanged(sscsCaseData, Optional.of(caseDetailsBefore));

        assertThat(result.getJohTiers()).containsAll(of("84", "58"));
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

    private ElementDisputed getElement(String issueCode) {
        return ElementDisputed.builder().value(ElementDisputedDetails.builder().issueCode(issueCode).build()).build();
    }

}
