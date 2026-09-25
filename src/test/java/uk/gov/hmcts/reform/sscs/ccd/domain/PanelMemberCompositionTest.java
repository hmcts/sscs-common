package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberComposition.FQPM_REF;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.DISTRICT_TRIBUNAL_JUDGE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.REGIONAL_MEDICAL_MEMBER;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.TRIBUNAL_JUDGE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.TRIBUNAL_MEMBER_DISABILITY;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.TRIBUNAL_MEMBER_MEDICAL;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class PanelMemberCompositionTest {

    private static final String REGIONAL_MEDICAL_MEMBER_REF = PanelMemberType.REGIONAL_MEDICAL_MEMBER.toRef();

    @DisplayName("Populate Panel composition from role types with No medical member")
    @Test
    void createPanelCompositionFromJohTiersNoMedicalMember() {
        var roleTypes = List.of(TRIBUNAL_JUDGE.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef(),
                TRIBUNAL_MEMBER_DISABILITY.toRef());
        var panelComposition = PanelMemberComposition.builder()
                .panelCompositionJudge(TRIBUNAL_JUDGE.toRef())
                .panelCompositionDisabilityAndFqMember(
                        List.of(TRIBUNAL_MEMBER_DISABILITY.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef())
                ).build();

        var result = new PanelMemberComposition(roleTypes);

        assertEqualsPanelComposition(panelComposition, result);
    }

    @DisplayName("Populate Panel composition from role types with one medical member")
    @Test
    void createPanelCompositionFromJohTiersWithOneMedicalMember() {
        var roleTypes = List.of(TRIBUNAL_JUDGE.toRef(), TRIBUNAL_MEMBER_MEDICAL.toRef(),
                TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef(), TRIBUNAL_MEMBER_DISABILITY.toRef());
        var panelComposition = PanelMemberComposition.builder()
                .panelCompositionJudge(TRIBUNAL_JUDGE.toRef())
                .panelCompositionMemberMedical1(TRIBUNAL_MEMBER_MEDICAL.toRef())
                .panelCompositionDisabilityAndFqMember(
                        List.of(TRIBUNAL_MEMBER_DISABILITY.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef())
                ).build();

        var result = new PanelMemberComposition(roleTypes);

        assertEqualsPanelComposition(panelComposition, result);
    }

    @DisplayName("Get JOH tiers from panelComposition with tribunal judge")
    @Test
    void shouldGetJohTiersFromPanelCompositionWithTribunalJudge() {
        var johTiers = List.of(TRIBUNAL_JUDGE.toRef(), TRIBUNAL_MEMBER_MEDICAL.toRef(),
                TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef(), TRIBUNAL_MEMBER_DISABILITY.toRef());
        var panelComposition = PanelMemberComposition.builder()
                .panelCompositionJudge(TRIBUNAL_JUDGE.toRef())
                .panelCompositionMemberMedical1(TRIBUNAL_MEMBER_MEDICAL.toRef())
                .panelCompositionDisabilityAndFqMember(
                        List.of(TRIBUNAL_MEMBER_DISABILITY.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef())
                ).build();

        assertThat(panelComposition.getJohTiers()).containsAll(johTiers);
    }

    @DisplayName("Get JOH tiers from panelComposition with dtj and no fqpm")
    @Test
    void shouldGetJohTiersFromPanelCompositionWithDtjAndNoFqpm() {
        var johTiers =
                List.of(TRIBUNAL_MEMBER_MEDICAL.toRef(), DISTRICT_TRIBUNAL_JUDGE.toRef(), TRIBUNAL_MEMBER_DISABILITY.toRef());
        var panelComposition = PanelMemberComposition.builder()
                .districtTribunalJudge(DISTRICT_TRIBUNAL_JUDGE.toRef())
                .panelCompositionMemberMedical1(TRIBUNAL_MEMBER_MEDICAL.toRef())
                .panelCompositionDisabilityAndFqMember(
                        List.of(TRIBUNAL_MEMBER_DISABILITY.toRef())
                ).build();

        assertThat(panelComposition.getJohTiers()).containsAll(johTiers);
    }

    @DisplayName("Populate Panel composition from role types with two medical members")
    @Test
    void createPanelCompositionFromJohTiersWithTwoMedicalMembers() {
        var roleTypes = List.of(TRIBUNAL_JUDGE.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef(),
                TRIBUNAL_MEMBER_MEDICAL.toRef(),TRIBUNAL_MEMBER_MEDICAL.toRef(), TRIBUNAL_MEMBER_DISABILITY.toRef());
        var panelComposition = PanelMemberComposition.builder()
                .panelCompositionJudge(TRIBUNAL_JUDGE.toRef())
                .panelCompositionMemberMedical1(TRIBUNAL_MEMBER_MEDICAL.toRef())
                .panelCompositionMemberMedical2(TRIBUNAL_MEMBER_MEDICAL.toRef())
                .panelCompositionDisabilityAndFqMember(
                        List.of(TRIBUNAL_MEMBER_DISABILITY.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef())
                ).build();

        var result = new PanelMemberComposition(roleTypes);

        assertEqualsPanelComposition(panelComposition, result);
    }

    @DisplayName("Populate Panel composition from role types with regional and tribunal medical members")
    @Test
    void createPanelCompositionFromJohTiersWithRegionalAndTribunalMedicalMembers() {
        var roleTypes = List.of(DISTRICT_TRIBUNAL_JUDGE.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef(),
                TRIBUNAL_MEMBER_MEDICAL.toRef(),REGIONAL_MEDICAL_MEMBER.toRef(), TRIBUNAL_MEMBER_DISABILITY.toRef());
        var panelComposition = PanelMemberComposition.builder()
                .districtTribunalJudge(DISTRICT_TRIBUNAL_JUDGE.toRef())
                .panelCompositionMemberMedical1(REGIONAL_MEDICAL_MEMBER.toRef())
                .panelCompositionMemberMedical2(TRIBUNAL_MEMBER_MEDICAL.toRef())
                .panelCompositionDisabilityAndFqMember(
                        List.of(TRIBUNAL_MEMBER_DISABILITY.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef())
                ).build();

        var result = new PanelMemberComposition(roleTypes);

        assertEqualsPanelComposition(panelComposition, result);
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

    @ParameterizedTest
    @MethodSource("judgeOnlyScenarios")
    void isJudgeOnly_shouldReturnExpectedResult(
            final String judge,
            final String districtTribunalJudge,
            final String medical1,
            final String medical2,
            final List<String> disabilityAndFqMember,
            final boolean expected) {
        final PanelMemberComposition panelComposition = PanelMemberComposition.builder()
            .panelCompositionJudge(judge)
            .districtTribunalJudge(districtTribunalJudge)
            .panelCompositionMemberMedical1(medical1)
            .panelCompositionMemberMedical2(medical2)
            .panelCompositionDisabilityAndFqMember(disabilityAndFqMember)
            .build();

        assertThat(panelComposition.isJudgeOnly()).isEqualTo(expected);
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

    @Test
    void equals_shouldReturnTrueForIdenticalObjects() {
        PanelMemberComposition a = PanelMemberComposition.builder()
                .districtTribunalJudge("dtj")
                .panelCompositionJudge("judge")
                .panelCompositionMemberMedical1("med1")
                .panelCompositionMemberMedical2("med2")
                .panelCompositionDisabilityAndFqMember(List.of("dis1", "fq1"))
                .build();

        PanelMemberComposition b = PanelMemberComposition.builder()
                .districtTribunalJudge("dtj")
                .panelCompositionJudge("judge")
                .panelCompositionMemberMedical1("med1")
                .panelCompositionMemberMedical2("med2")
                .panelCompositionDisabilityAndFqMember(List.of("dis1", "fq1"))
                .build();

        assertThat(a.equals(b)).isTrue();
    }

    @Test
    void equals_shouldReturnFalseForDifferentFields() {
        PanelMemberComposition a = PanelMemberComposition.builder()
                .districtTribunalJudge("dtj")
                .panelCompositionJudge("judge")
                .panelCompositionMemberMedical1("med1")
                .panelCompositionMemberMedical2("med2")
                .panelCompositionDisabilityAndFqMember(List.of("dis1", "fq1"))
                .build();

        PanelMemberComposition b = PanelMemberComposition.builder()
                .districtTribunalJudge("dtj2")
                .panelCompositionJudge("judge")
                .panelCompositionMemberMedical1("med1")
                .panelCompositionMemberMedical2("med2")
                .panelCompositionDisabilityAndFqMember(List.of("dis1", "fq1"))
                .build();

        assertThat(a.equals(b)).isFalse();
    }

    @Test
    void equals_shouldReturnTrueForSameReference() {
        PanelMemberComposition a = PanelMemberComposition.builder()
                .districtTribunalJudge("dtj")
                .build();

        assertThat(a.equals(a)).isTrue();
    }

    @Test
    void equals_shouldReturnFalseForNull() {
        PanelMemberComposition a = PanelMemberComposition.builder()
                .districtTribunalJudge("dtj")
                .build();

        assertThat(a.equals(null)).isFalse();
    }

    @Test
    void equals_shouldReturnFalseForDifferentClass() {
        PanelMemberComposition a = PanelMemberComposition.builder()
                .districtTribunalJudge("dtj")
                .build();

        assertThat(a.equals("not a PanelMemberComposition")).isFalse();
    }

    @Test
    void equals_shouldIgnoreOrderOfDisabilityAndFqMember() {
        PanelMemberComposition a = PanelMemberComposition.builder()
                .panelCompositionDisabilityAndFqMember(List.of("a", "b"))
                .build();

        PanelMemberComposition b = PanelMemberComposition.builder()
                .panelCompositionDisabilityAndFqMember(List.of("b", "a"))
                .build();

        assertThat(a.equals(b)).isTrue();
    }

    @Test
    void equals_shouldReturnTrueForBothEmptyDisabilityAndFqMember() {
        PanelMemberComposition a = PanelMemberComposition.builder()
                .panelCompositionDisabilityAndFqMember(new ArrayList<>())
                .build();

        PanelMemberComposition b = PanelMemberComposition.builder()
                .panelCompositionDisabilityAndFqMember(new ArrayList<>())
                .build();

        assertThat(a.equals(b)).isTrue();
    }

    private void assertEqualsPanelComposition(PanelMemberComposition expected, PanelMemberComposition actual) {
        assertSoftly(softly -> {
            softly.assertThat(actual.getPanelCompositionJudge()).isEqualTo(expected.getPanelCompositionJudge());
            softly.assertThat(actual.getDistrictTribunalJudge()).isEqualTo(expected.getDistrictTribunalJudge());
            softly.assertThat(actual.getPanelCompositionMemberMedical1()).isEqualTo(expected.getPanelCompositionMemberMedical1());
            softly.assertThat(actual.getPanelCompositionMemberMedical2()).isEqualTo(expected.getPanelCompositionMemberMedical2());
            softly.assertThat(expected.getPanelCompositionDisabilityAndFqMember())
                    .containsAll(actual.getPanelCompositionDisabilityAndFqMember());
        });
    }

    private static Stream<Arguments> judgeOnlyScenarios() {
        return Stream.of(
                Arguments.of("judge", null, null, null, null, true),
                Arguments.of("judge", null, null, null, new ArrayList<>(), true),
                Arguments.of(null, "dtj", null, null, null, true),
                Arguments.of("judge", "dtj", null, null, new ArrayList<>(), true),
                Arguments.of(null, null, null, null, null, false),
                Arguments.of(null, null, null, null, new ArrayList<>(), false),
                Arguments.of("judge", null, "med1", null, null, false),
                Arguments.of("judge", null, null, "med2", null, false),
                Arguments.of("judge", null, null, null, List.of("58"), false),
                Arguments.of(null, "dtj", "med1", "med2", List.of("58"), false)
        );
    }

    private static Stream<List<String>> listsWithoutFqpm() {
        return Stream.of(
                new ArrayList<>(List.of("58", "44")),
                new ArrayList<>(),
                null
        );
    }
}
