package uk.gov.hmcts.reform.sscs.reference.data.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.REGIONAL_MEDICAL_MEMBER;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.TRIBUNAL_MEMBER_MEDICAL;

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
}
