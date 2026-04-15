package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.CHILD_SUPPORT;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.INFECTED_BLOOD_COMPENSATION;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.UC;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.NO;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.YES;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class ConfidentialityTabBuilderTest {

    public static final LocalDateTime DATE = LocalDateTime.of(2024, 1, 20, 9, 5, 3);

    @Test
    void shouldNotIncludePartyRowsWhenAppellantIsNull() {
        final Appeal appeal = Appeal.builder().appellant(null).build();

        final String result = ConfidentialityTabBuilder.buildConfidentialityTab(CHILD_SUPPORT, appeal, null);

        assertThat(result).isEqualTo("""
            Party | Name | Confidentiality Status | Confidentiality Status Confirmed
            -|-|-|-
            
            """);
    }

    @Test
    void shouldShowUndeterminedStatusWhenAppellantHasNullData() {
        final Appellant appellant = Appellant.builder().build();
        final Appeal appeal = Appeal.builder().appellant(appellant).build();

        final String result = ConfidentialityTabBuilder.buildConfidentialityTab(CHILD_SUPPORT, appeal, null);

        assertThat(result).isEqualToIgnoringWhitespace("""
            Party | Name | Confidentiality Status | Confidentiality Status Confirmed
            -|-|-|-
            Appellant |  | Undetermined |
            
            """);
    }

    @Test
    void shouldShowYesWhenAppellantHasYesConfidentiality() {
        final Appellant appellant = Appellant.builder()
                                             .confidentialityRequired(YES)
                                             .name(Name.builder().firstName("John").lastName("Doe").build())
                                             .build();
        final Appeal appeal = Appeal.builder().appellant(appellant).build();

        final String result = ConfidentialityTabBuilder.buildConfidentialityTab(CHILD_SUPPORT, appeal, null);

        assertThat(result).isEqualToIgnoringWhitespace("""
            Party | Name | Confidentiality Status | Confidentiality Status Confirmed
            -|-|-|-
            Appellant | John Doe | Yes |
            
            """);
    }

    @Test
    void shouldShowNoWhenAppellantHasNoConfidentiality() {
        final Appellant appellant = Appellant.builder().confidentialityRequired(NO).build();
        final Appeal appeal = Appeal.builder().appellant(appellant).build();

        final String result = ConfidentialityTabBuilder.buildConfidentialityTab(CHILD_SUPPORT, appeal, null);

        assertThat(result).isEqualToIgnoringWhitespace("""
            Party | Name | Confidentiality Status | Confidentiality Status Confirmed
            -|-|-|-
            Appellant |  | No |
            
            """);
    }

    @Test
    void shouldShowFormattedDateWhenAppellantHasDate() {
        final Appellant appellant = Appellant.builder().confidentialityRequiredChangedDate(DATE).build();
        final Appeal appeal = Appeal.builder().appellant(appellant).build();

        final String result = ConfidentialityTabBuilder.buildConfidentialityTab(CHILD_SUPPORT, appeal, null);

        assertThat(result).isEqualToIgnoringWhitespace("""
            Party | Name | Confidentiality Status | Confidentiality Status Confirmed
            -|-|-|-
            Appellant |  | Undetermined | 20 Jan 2024, 9:05:03 am
            
            """);
    }

    @Test
    void shouldShowAppointeeRowWhenIsAppointeeYes() {
        final Appointee appointee = Appointee.builder().name(Name.builder().firstName("App").lastName("Ointee").build()).build();
        final Appellant appellant = Appellant.builder()
                                             .confidentialityRequired(YES)
                                             .isAppointee("Yes")
                                             .appointee(appointee)
                                             .build();
        final Appeal appeal = Appeal.builder().appellant(appellant).build();

        final String result = ConfidentialityTabBuilder.buildConfidentialityTab(CHILD_SUPPORT, appeal, null);

        assertThat(result).isEqualToIgnoringWhitespace("""
            Party | Name | Confidentiality Status | Confidentiality Status Confirmed
            -|-|-|-
            Appellant |  | Yes |
            Appointee | App Ointee | Yes |
            
            """);
    }

    @Test
    void shouldNotIncludeAppointeeRowWhenIsAppointeeNo() {
        final Appointee appointee = Appointee.builder().build();
        final Appellant appellant = Appellant.builder().isAppointee("No").appointee(appointee).build();
        final Appeal appeal = Appeal.builder().appellant(appellant).build();

        final String result = ConfidentialityTabBuilder.buildConfidentialityTab(CHILD_SUPPORT, appeal, null);

        assertThat(result).isEqualToIgnoringWhitespace("""
            Party | Name | Confidentiality Status | Confidentiality Status Confirmed
            -|-|-|-
            Appellant |  | Undetermined |
            
            """);
    }

    @Test
    void shouldNotIncludeAppointeeRowWhenAppointeeIsNull() {
        final Appellant appellant = Appellant.builder().isAppointee("Yes").appointee(null).build();
        final Appeal appeal = Appeal.builder().appellant(appellant).build();

        final String result = ConfidentialityTabBuilder.buildConfidentialityTab(CHILD_SUPPORT, appeal, null);

        assertThat(result).isEqualToIgnoringWhitespace("""
            Party | Name | Confidentiality Status | Confidentiality Status Confirmed
            -|-|-|-
            Appellant |  | Undetermined |
            
            """);
    }

    @Test
    void shouldShowOtherParty1WhenSingleOtherPartyExists() {
        final OtherParty otherParty = OtherParty.builder()
                                                .name(Name.builder().firstName("Other").lastName("Person").build())
                                                .confidentialityRequired(YES)
                                                .build();
        final Appeal appeal = Appeal.builder().appellant(null).build();

        final String result = ConfidentialityTabBuilder.buildConfidentialityTab(CHILD_SUPPORT, appeal,
            List.of(CcdValue.<OtherParty>builder().value(otherParty).build()));

        assertThat(result).isEqualToIgnoringWhitespace("""
            Party | Name | Confidentiality Status | Confidentiality Status Confirmed
            -|-|-|-
            Other Party 1 | Other Person | Yes |
            
            """);
    }

    @Test
    void shouldUseCorrectIndexingWhenMultipleOtherPartiesExist() {
        final OtherParty otherParty1 = OtherParty.builder()
                                                 .name(Name.builder().firstName("First").lastName("Party").build())
                                                 .build();
        final OtherParty otherParty2 = OtherParty.builder()
                                                 .name(Name.builder().firstName("Second").lastName("Party").build())
                                                 .build();
        final Appeal appeal = Appeal.builder().appellant(null).build();

        final String result = ConfidentialityTabBuilder.buildConfidentialityTab(CHILD_SUPPORT, appeal,
            List.of(CcdValue.<OtherParty>builder().value(otherParty1).build(),
                CcdValue.<OtherParty>builder().value(otherParty2).build()));

        assertThat(result).isEqualToIgnoringWhitespace("""
            Party | Name | Confidentiality Status | Confidentiality Status Confirmed
            -|-|-|-
            Other Party 1 | First Party | Undetermined |
            Other Party 2 | Second Party | Undetermined |
            
            """);
    }

    @Test
    void shouldFilterNullsWhenOtherPartiesContainNullCcdValues() {
        final OtherParty otherParty = OtherParty.builder()
                                                .name(Name.builder().firstName("Valid").lastName("Party").build())
                                                .build();
        final Appeal appeal = Appeal.builder().appellant(null).build();

        final String result = ConfidentialityTabBuilder.buildConfidentialityTab(CHILD_SUPPORT, appeal,
            Arrays.asList(null, CcdValue.<OtherParty>builder().value(otherParty).build()));

        assertThat(result).isEqualToIgnoringWhitespace("""
            Party | Name | Confidentiality Status | Confidentiality Status Confirmed
            -|-|-|-
            Other Party 1 | Valid Party | Undetermined |
            
            """);
    }

    @Test
    void shouldShowEmptyNameInRowWhenOtherPartyHasNullName() {
        final OtherParty otherParty = OtherParty.builder().confidentialityRequired(NO).build();
        final Appeal appeal = Appeal.builder().appellant(null).build();

        final String result = ConfidentialityTabBuilder.buildConfidentialityTab(CHILD_SUPPORT, appeal,
            List.of(CcdValue.<OtherParty>builder().value(otherParty).build()));

        assertThat(result).isEqualToIgnoringWhitespace("""
            Party | Name | Confidentiality Status | Confidentiality Status Confirmed
            -|-|-|-
            Other Party 1 |  | No |
            
            """);
    }

    @Test
    void shouldShowFormattedDateWhenOtherPartyHasDate() {
        final OtherParty otherParty = OtherParty.builder().confidentialityRequiredChangedDate(DATE).build();
        final Appeal appeal = Appeal.builder().appellant(null).build();

        final String result = ConfidentialityTabBuilder.buildConfidentialityTab(CHILD_SUPPORT, appeal,
            List.of(CcdValue.<OtherParty>builder().value(otherParty).build()));

        assertThat(result).isEqualToIgnoringWhitespace("""
            Party | Name | Confidentiality Status | Confidentiality Status Confirmed
            -|-|-|-
            Other Party 1 |  | Undetermined | 20 Jan 2024, 9:05:03 am
            
            """);
    }

    @Test
    void shouldShowFormattedDateWhenAppointeeHasDate() {
        final Appointee appointee = Appointee.builder().name(Name.builder().firstName("Joe").lastName("App").build()).build();
        final Appellant appellant = Appellant.builder()
                                             .isAppointee("Yes")
                                             .appointee(appointee)
                                             .confidentialityRequiredChangedDate(DATE)
                                             .build();
        final Appeal appeal = Appeal.builder().appellant(appellant).build();

        final String result = ConfidentialityTabBuilder.buildConfidentialityTab(CHILD_SUPPORT, appeal, null);

        assertThat(result).isEqualToIgnoringWhitespace("""
            Party | Name | Confidentiality Status | Confidentiality Status Confirmed
            -|-|-|-
            Appellant |  | Undetermined | 20 Jan 2024, 9:05:03 am
            Appointee | Joe App | Undetermined | 20 Jan 2024, 9:05:03 am
            
            """);
    }

    @Test
    void shouldIncludeAllRowsWhenAllPartiesArePresent() {
        final Appointee appointee = Appointee.builder().name(Name.builder().firstName("Ap").lastName("Pointee").build()).build();
        final Appellant appellant = Appellant.builder()
                                             .name(Name.builder().firstName("App").lastName("Ellant").build())
                                             .confidentialityRequired(YES)
                                             .confidentialityRequiredChangedDate(DATE)
                                             .isAppointee("Yes")
                                             .appointee(appointee)
                                             .build();
        final OtherParty otherParty = OtherParty.builder()
                                                .name(Name.builder().firstName("Other").lastName("Guy").build())
                                                .confidentialityRequired(NO)
                                                .build();
        final Appeal appeal = Appeal.builder().appellant(appellant).build();

        final String result = ConfidentialityTabBuilder.buildConfidentialityTab(CHILD_SUPPORT, appeal,
            List.of(CcdValue.<OtherParty>builder().value(otherParty).build()));

        assertThat(result).isEqualToIgnoringWhitespace("""
            Party | Name | Confidentiality Status | Confidentiality Status Confirmed
            -|-|-|-
            Appellant | App Ellant | Yes | 20 Jan 2024, 9:05:03 am
            Appointee | Ap Pointee | Yes | 20 Jan 2024, 9:05:03 am
            Other Party 1 | Other Guy | No |
            
            """);
    }

    @Test
    void shouldBuildTabWhenBenefitIsUc() {
        final Appellant appellant = Appellant.builder()
                                             .confidentialityRequired(YES)
                                             .name(Name.builder().firstName("Uc").lastName("User").build())
                                             .build();
        final Appeal appeal = Appeal.builder().appellant(appellant).build();

        final String result = ConfidentialityTabBuilder.buildConfidentialityTab(UC, appeal, null);

        assertThat(result).isEqualToIgnoringWhitespace("""
            Party | Name | Confidentiality Status | Confidentiality Status Confirmed
            -|-|-|-
            Appellant | Uc User | Yes |
            
            """);
    }

    @Test
    void shouldReturnNullWhenBenefitIsNotChildSupport() {
        final Appeal appeal = Appeal.builder().appellant(null).build();

        final String result = ConfidentialityTabBuilder.buildConfidentialityTab(INFECTED_BLOOD_COMPENSATION, appeal, null);

        assertThat(result).isNull();
    }

    @Test
    void shouldNotIncludePartyRowsWhenAppealIsNull() {
        final String result = ConfidentialityTabBuilder.buildConfidentialityTab(CHILD_SUPPORT, null, null);

        assertThat(result).isEqualTo("""
            Party | Name | Confidentiality Status | Confidentiality Status Confirmed
            -|-|-|-
            
            """);
    }

    @Test
    void shouldReturnNullWhenBenefitIsNull() {
        final Appeal appeal = Appeal.builder().appellant(null).build();

        final String result = ConfidentialityTabBuilder.buildConfidentialityTab(null, appeal, null);

        assertThat(result).isNull();
    }

}