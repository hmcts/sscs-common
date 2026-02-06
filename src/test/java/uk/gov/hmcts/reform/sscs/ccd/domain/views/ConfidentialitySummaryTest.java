package uk.gov.hmcts.reform.sscs.ccd.domain.views;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appeal;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appellant;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appointee;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitType;
import uk.gov.hmcts.reform.sscs.ccd.domain.CcdValue;
import uk.gov.hmcts.reform.sscs.ccd.domain.ConfidentialitySummaryEntry;
import uk.gov.hmcts.reform.sscs.ccd.domain.Name;
import uk.gov.hmcts.reform.sscs.ccd.domain.OtherParty;
import uk.gov.hmcts.reform.sscs.ccd.domain.YesNo;

class ConfidentialitySummaryTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d MMM yyyy, h:mm:ss a", Locale.UK);
    private static final LocalDateTime APPELLANT_CHANGED_DATE = LocalDateTime.now();
    private static final LocalDateTime OTHER_PARTY_ONE_CHANGED_DATE = LocalDateTime.now().minusDays(1);
    private static final LocalDateTime OTHER_PARTY_TWO_CHANGED_DATE = LocalDateTime.now().minusDays(2);
    private static final String CHILD_SUPPORT_CODE = "childSupport";

    @ParameterizedTest
    @MethodSource("emptyResultScenarios")
    void shouldReturnEmptyListForInvalidScenarios(List<CcdValue<OtherParty>> otherParties, Appeal appeal, String scenario) {
        List<CcdValue<ConfidentialitySummaryEntry>> results = ConfidentialitySummary.getConfidentialitySummaryEntries(
            otherParties, appeal);

        assertThat(results).as("Should return empty list when " + scenario).isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenAppealIsNull() {
        List<CcdValue<ConfidentialitySummaryEntry>> results = ConfidentialitySummary.getConfidentialitySummaryEntries(List.of(),
            null);

        assertThat(results).isEmpty();
    }

    @Test
    void shouldBuildEntriesForAppellantAppointeeAndOtherPartiesWhenChildSupportBenefitProvided() {

        final LocalDateTime now = LocalDateTime.now();

        Appellant appellant = Appellant.builder().name(Name.builder().title("Ms").firstName("App").lastName("Ellant").build())
            .confidentialityRequired(YesNo.YES).confidentialityRequiredChangedDate(APPELLANT_CHANGED_DATE).isAppointee("Yes")
            .appointee(Appointee.builder().name(Name.builder().title("Mr").firstName("Apo").lastName("Intee").build()).build())
            .build();

        OtherParty otherParty1 = OtherParty.builder().name(Name.builder().firstName("Other").lastName("PartyOne").build())
            .confidentialityRequired(YesNo.NO).confidentialityRequiredChangedDate(OTHER_PARTY_ONE_CHANGED_DATE).build();

        OtherParty otherParty2 = OtherParty.builder().name(Name.builder().firstName("Other").lastName("PartyTwo").build())
            .confidentialityRequired(YesNo.NO).confidentialityRequiredChangedDate(OTHER_PARTY_TWO_CHANGED_DATE).build();

        Appeal appeal = childSupportAppeal(appellant);

        List<CcdValue<ConfidentialitySummaryEntry>> results = ConfidentialitySummary.getConfidentialitySummaryEntries(
            List.of(ccdValueOf(otherParty1), ccdValueOf(otherParty2)), appeal);

        assertThat(results).hasSize(4);
        assertEntry(results.get(0).getValue(), "App Ellant", "Appellant", "Yes", APPELLANT_CHANGED_DATE.format(FORMATTER));
        assertEntry(results.get(1).getValue(), "Apo Intee", "Appointee", "Yes", APPELLANT_CHANGED_DATE.format(FORMATTER));
        assertEntry(results.get(2).getValue(), "Other PartyOne", "Other Party #1", "No",
            OTHER_PARTY_ONE_CHANGED_DATE.format(FORMATTER));
        assertEntry(results.get(3).getValue(), "Other PartyTwo", "Other Party #2", "No",
            OTHER_PARTY_TWO_CHANGED_DATE.format(FORMATTER));
    }

    @ParameterizedTest
    @MethodSource("undeterminedConfidentialityScenarios")
    void shouldSetConfidentialityRequiredToUndeterminedWhenItIsNull(Appellant appellant, List<CcdValue<OtherParty>> otherParties,
        int expectedSize, String scenario) {

        List<CcdValue<ConfidentialitySummaryEntry>> results = ConfidentialitySummary.getConfidentialitySummaryEntries(
            otherParties, childSupportAppeal(appellant));

        assertThat(results).as("Should have correct number of entries for " + scenario).hasSize(expectedSize);

        results.forEach(result -> assertThat(result.getValue().getConfidentialityRequired()).as(
            "Confidentiality should be Undetermined for " + scenario).isEqualTo("Undetermined"));
    }

    @Test
    void shouldBuildOnlyOtherPartyEntryWhenAppellantIsMissing() {
        OtherParty otherParty = OtherParty.builder().confidentialityRequiredChangedDate(OTHER_PARTY_ONE_CHANGED_DATE)
            .name(Name.builder().firstName("Bob").lastName("Holmes").build()).build();
        Appeal appeal = Appeal.builder().benefitType(BenefitType.builder().code(CHILD_SUPPORT_CODE).build()).build();

        List<CcdValue<ConfidentialitySummaryEntry>> results = ConfidentialitySummary.getConfidentialitySummaryEntries(
            List.of(ccdValueOf(otherParty)), appeal);

        assertThat(results).hasSize(1);
        assertEntry(results.getFirst().getValue(), "Bob Holmes", "Other Party #1", "Undetermined",
            OTHER_PARTY_ONE_CHANGED_DATE.format(FORMATTER));
    }

    @Test
    void shouldReturnEmptyListWhenChildSupportAndAppellantMissingWithNoOtherParties() {
        Appeal appeal = Appeal.builder().benefitType(BenefitType.builder().code(CHILD_SUPPORT_CODE).build()).build();

        List<CcdValue<ConfidentialitySummaryEntry>> results = ConfidentialitySummary.getConfidentialitySummaryEntries(List.of(),
            appeal);

        assertThat(results).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"No", "no", "N", ""})
    @NullAndEmptySource
    void shouldNotAddAppointeeEntryWhenIsAppointeeIsNotYes(String isAppointeeValue) {
        Appellant appellant = Appellant.builder().name(Name.builder().firstName("App").lastName("Ellant").build())
            .isAppointee(isAppointeeValue)
            .appointee(Appointee.builder().name(Name.builder().firstName("Appointee").build()).build()).build();

        final Appeal appeal = childSupportAppeal(appellant);

        List<CcdValue<ConfidentialitySummaryEntry>> results = ConfidentialitySummary.getConfidentialitySummaryEntries(List.of(),
            appeal);

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getValue().getRole()).isEqualTo("Appellant");
    }

    @Test
    void shouldReturnOnlyAppellantEntryWhenAppointeeIsMissing() {
        List<CcdValue<ConfidentialitySummaryEntry>> results = ConfidentialitySummary.getConfidentialitySummaryEntries(List.of(),
            childSupportAppeal(appellantWithName()));

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getValue().getRole()).isEqualTo("Appellant");
    }

    private static Appeal childSupportAppeal(Appellant appellant) {
        return Appeal.builder().appellant(appellant).benefitType(BenefitType.builder().code(CHILD_SUPPORT_CODE).build()).build();
    }

    private static Appellant appellantWithName() {
        return Appellant.builder().name(Name.builder().firstName("App").lastName("Ellant").build()).build();
    }

    private static OtherParty otherPartyWithName() {
        return OtherParty.builder().name(Name.builder().firstName("Other").lastName("Party").build()).build();
    }

    private static CcdValue<OtherParty> ccdValueOf(OtherParty otherParty) {
        return CcdValue.<OtherParty>builder().value(otherParty).build();
    }

    private static Stream<Arguments> emptyResultScenarios() {
        Appeal noBenefitType = Appeal.builder().build();
        Appeal noBenefitCode = Appeal.builder().benefitType(BenefitType.builder().build()).appellant(appellantWithName()).build();
        Appeal nonChildSupportBenefit = Appeal.builder().benefitType(BenefitType.builder().code("pip").build())
            .appellant(appellantWithName()).build();
        OtherParty otherParty = otherPartyWithName();

        return Stream.of(Arguments.of(null, null, "appeal is null"), Arguments.of(null, noBenefitType, "benefit type is missing"),
            Arguments.of(null, noBenefitCode, "benefit code is null"),
            Arguments.of(List.of(ccdValueOf(otherParty)), nonChildSupportBenefit, "benefit type is not child support"));
    }

    private static Stream<Arguments> undeterminedConfidentialityScenarios() {
        Appellant appellantOnly = appellantWithName();
        OtherParty otherParty = otherPartyWithName();

        Appellant appellantWithAppointee = Appellant.builder().isAppointee("Yes").appointee(Appointee.builder().build()).build();

        return Stream.of(Arguments.of(appellantOnly, List.of(ccdValueOf(otherParty)), 2,
                "appellant and other party with null confidentiality"),
            Arguments.of(appellantWithAppointee, List.of(), 2, "appellant and appointee with null names and confidentiality"));
    }

    private void assertEntry(ConfidentialitySummaryEntry entry, String expectedName, String expectedRole,
        String expectedConfidentiality, String expectedDate) {
        assertThat(entry.getName()).isEqualTo(expectedName);
        assertThat(entry.getRole()).isEqualTo(expectedRole);
        assertThat(entry.getConfidentialityRequired()).isEqualTo(expectedConfidentiality);
        assertThat(entry.getConfidentialityRequiredChangedDate()).isEqualTo(expectedDate);
    }
}
