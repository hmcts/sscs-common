package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberComposition.FQPM_REF;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class PanelMemberCompositionTest {

    private static final String REGIONAL_MEDICAL_MEMBER_REF = PanelMemberType.REGIONAL_MEDICAL_MEMBER.toRef();

    static Stream<List<String>> listsWithoutFqpm() {
        return Stream.of(
            new ArrayList<>(List.of("58", "44")),
            new ArrayList<>(),
            null
        );
    }

    @ParameterizedTest
    @MethodSource("listsWithoutFqpm")
    void hasFqpm_shouldReturnFalse_whenFqpmNotPresent(List<String> disabilityAndFqpm) {
        PanelMemberComposition panelComposition = PanelMemberComposition.builder()
            .panelCompositionDisabilityAndFqMember(disabilityAndFqpm)
            .build();

        assertThat(panelComposition.hasFqpm()).isFalse();
    }

    @Test
    void hasFqpm_shouldReturnTrue_whenListContainsFqpm() {
        PanelMemberComposition panelComposition = PanelMemberComposition.builder()
            .panelCompositionDisabilityAndFqMember(List.of("58", FQPM_REF, "44"))
            .build();

        assertThat(panelComposition.hasFqpm()).isTrue();
    }

    @ParameterizedTest
    @MethodSource("listsWithoutFqpm")
    void addFqpm_shouldAddFqpm(List<String> disabilityAndFqpm) {
        PanelMemberComposition panelComposition = PanelMemberComposition.builder()
            .panelCompositionDisabilityAndFqMember(disabilityAndFqpm)
            .build();

        panelComposition.addFqpm();

        assertThat(panelComposition.getPanelCompositionDisabilityAndFqMember())
            .contains(FQPM_REF);
    }

    @Test
    void addFqpm_shouldNotAddFqpmAgain_whenAlreadyPresent() {
        PanelMemberComposition panelComposition = PanelMemberComposition.builder()
            .panelCompositionDisabilityAndFqMember(new ArrayList<>(List.of("44", "58", FQPM_REF)))
            .build();

        panelComposition.addFqpm();

        assertThat(panelComposition.getPanelCompositionDisabilityAndFqMember())
            .containsOnlyOnce(FQPM_REF);
    }

    @ParameterizedTest
    @MethodSource("listsWithoutFqpm")
    void removeFqpm_shouldNotChangeList_whenFqpmNotPresent(List<String> disabilityAndFqpm) {
        PanelMemberComposition panelComposition = PanelMemberComposition.builder()
            .panelCompositionDisabilityAndFqMember(disabilityAndFqpm == null ? null : new ArrayList<>(disabilityAndFqpm))
            .build();

        panelComposition.removeFqpm();

        assertThat(panelComposition.getPanelCompositionDisabilityAndFqMember())
            .isEqualTo(disabilityAndFqpm);
    }

    @Test
    void removeFqpm_shouldRemoveFqpm_whenPresent() {
        PanelMemberComposition panelComposition = PanelMemberComposition.builder()
            .panelCompositionDisabilityAndFqMember(new ArrayList<>(List.of("44", FQPM_REF)))
            .build();

        panelComposition.removeFqpm();

        assertThat(panelComposition.getPanelCompositionDisabilityAndFqMember())
            .doesNotContain(FQPM_REF);
    }

    @Test
    void hasMedicalMember_shouldReturnFalse_whenNoMedicalMemberSelected() {
        PanelMemberComposition panelMemberComposition = PanelMemberComposition.builder()
            .panelCompositionMemberMedical1(null)
            .panelCompositionMemberMedical2(null)
            .build();

        assertThat(panelMemberComposition.hasMedicalMember()).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
        "58, null",
        "null, 58",
        "69, 58",
        "69, null"
    })
    void hasMedicalMember_shouldReturnTrue_whenAnyMedicalMemberSelected(String member1, String member2) {
        PanelMemberComposition panelMemberComposition = PanelMemberComposition.builder()
            .panelCompositionMemberMedical1("null".equals(member1) ? null : member1)
            .panelCompositionMemberMedical2("null".equals(member2) ? null : member2)
            .build();

        assertThat(panelMemberComposition.hasMedicalMember()).isTrue();
    }

    @Test
    void clearMedicalMembers_shouldSetBothFieldsToNull() {
        PanelMemberComposition panelMemberComposition = PanelMemberComposition.builder()
            .panelCompositionMemberMedical1(FQPM_REF)
            .panelCompositionMemberMedical2(REGIONAL_MEDICAL_MEMBER_REF)
            .build();

        panelMemberComposition.clearMedicalMembers();

        assertThat(panelMemberComposition.getPanelCompositionMemberMedical1()).isNull();
        assertThat(panelMemberComposition.getPanelCompositionMemberMedical2()).isNull();
    }
}