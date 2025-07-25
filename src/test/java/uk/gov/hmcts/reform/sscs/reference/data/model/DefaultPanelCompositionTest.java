package uk.gov.hmcts.reform.sscs.reference.data.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType;

import java.util.List;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsIndustrialInjuriesData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.REGIONAL_MEDICAL_MEMBER;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.TRIBUNAL_MEMBER_MEDICAL;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.NO;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.YES;

public class DefaultPanelCompositionTest {

    @DisplayName("ContainsAnyPanelMembers should return true when default panel comp contains one of the panel members supplied")
    @Test
    public void testContainsAnyPanelMembersTrue() {
        DefaultPanelComposition defaultPanelComposition = new DefaultPanelComposition();
        defaultPanelComposition.setJohTiers(List.of(TRIBUNAL_MEMBER_MEDICAL.toRef()));
        List<PanelMemberType> panelMembers = List.of(TRIBUNAL_MEMBER_MEDICAL);
        boolean result = defaultPanelComposition.containsAnyPanelMembers(panelMembers);
        assertThat(result).isTrue();
    }

    @DisplayName("ContainsAnyPanelMembers should return false when default panel comp doesn't contain panel members")
    @Test
    public void testContainsAnyPanelMembersFalse() {
        DefaultPanelComposition defaultPanelComposition = new DefaultPanelComposition();
        defaultPanelComposition.setJohTiers(List.of(TRIBUNAL_MEMBER_MEDICAL.toRef()));
        List<PanelMemberType> panelMembers = List.of(REGIONAL_MEDICAL_MEMBER);
        boolean result = defaultPanelComposition.containsAnyPanelMembers(panelMembers);
        assertThat(result).isFalse();
    }

    @DisplayName("Create DefaultPanelComposition with FQPM and no medical member")
    @Test
    public void shouldReturnDefaultPanelCompositionWithFqpmAndNoMedicalMember() {
        var caseData = SscsCaseData.builder()
                .benefitCode("022").issueCode("RA")
                .sscsIndustrialInjuriesData(SscsIndustrialInjuriesData.builder()
                        .panelDoctorSpecialism("cardiologist").build())
                .isFqpmRequired(YES)
                .isMedicalMemberRequired(NO)
                .build();

        var actual  = new DefaultPanelComposition(caseData.getIssueCode(), caseData);

        assertEquals("022RA", actual.getBenefitIssueCode());
        assertEquals("1", actual.getSpecialismCount());
        assertEquals("yes", actual.getFqpm());
        assertNull(actual.getMedicalMember());
    }

    @DisplayName("Create DefaultPanelComposition with  Medical member and no FQPM")
    @Test
    public void shouldReturnDefaultPanelCompositionWithMedicalMemberAndNoFqpm() {
        var caseData = SscsCaseData.builder()
                .benefitCode("022").issueCode("RA")
                .sscsIndustrialInjuriesData(SscsIndustrialInjuriesData.builder()
                        .panelDoctorSpecialism("cardiologist").secondPanelDoctorSpecialism("radiologist").build())
                .isFqpmRequired(NO)
                .isMedicalMemberRequired(YES)
                .build();

        var actual  = new DefaultPanelComposition(caseData.getIssueCode(), caseData);

        assertEquals("022RA", actual.getBenefitIssueCode());
        assertEquals("2", actual.getSpecialismCount());
        assertEquals("yes", actual.getMedicalMember());
        assertNull(actual.getFqpm());
    }
}
