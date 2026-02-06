package uk.gov.hmcts.reform.sscs.ccd.domain.views;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appeal;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appellant;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appointee;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitType;
import uk.gov.hmcts.reform.sscs.ccd.domain.CcdValue;
import uk.gov.hmcts.reform.sscs.ccd.domain.ConfidentialityTabEntry;
import uk.gov.hmcts.reform.sscs.ccd.domain.Name;
import uk.gov.hmcts.reform.sscs.ccd.domain.OtherParty;
import uk.gov.hmcts.reform.sscs.ccd.domain.YesNo;

class ConfidentialityTabTest {

    @Test
    void returnsEmptyWhenAppealIsNull() {
        List<CcdValue<ConfidentialityTabEntry>> results = ConfidentialityTab.getConfidentialityTabEntries(null, null);

        assertThat(results).isEmpty();
    }

    @Test
    void returnsEmptyWhenBenefitTypeIsMissingOrCodeNull() {
        Appeal noBenefitType = Appeal.builder().build();
        List<CcdValue<ConfidentialityTabEntry>> resultsNoBenefitType =
            ConfidentialityTab.getConfidentialityTabEntries(null, noBenefitType);

        Appeal noBenefitCode = Appeal.builder()
            .benefitType(BenefitType.builder().build())
            .appellant(Appellant.builder().name(Name.builder().firstName("App").lastName("Ellant").build()).build())
            .build();
        List<CcdValue<ConfidentialityTabEntry>> resultsNoBenefitCode =
            ConfidentialityTab.getConfidentialityTabEntries(null, noBenefitCode);

        assertThat(resultsNoBenefitType).isEmpty();
        assertThat(resultsNoBenefitCode).isEmpty();
    }

    @Test
    void returnsEmptyWhenBenefitTypeIsNotChildSupport() {
        Appeal appeal = Appeal.builder()
            .benefitType(BenefitType.builder().code("pip").build())
            .appellant(Appellant.builder().name(Name.builder().firstName("App").lastName("Ellant").build()).build())
            .build();
        OtherParty otherParty = OtherParty.builder()
            .name(Name.builder().firstName("Other").lastName("Party").build())
            .build();

        List<CcdValue<ConfidentialityTabEntry>> results =
            ConfidentialityTab.getConfidentialityTabEntries(
                List.of(CcdValue.<OtherParty>builder().value(otherParty).build()),
                appeal
            );

        assertThat(results).isEmpty();
    }

    @Test
    void buildsEntriesForAppellantAppointeeAndOtherParties() {
        Appellant appellant = Appellant.builder()
            .name(Name.builder().title("Ms").firstName("App").lastName("Ellant").build())
            .confidentialityRequired(YesNo.YES)
            .confidentialityRequiredChangedDate("2020-01-01")
            .isAppointee("Yes")
            .appointee(Appointee.builder()
                .name(Name.builder().title("Mr").firstName("Apo").lastName("Intee").build())
                .build())
            .build();

        OtherParty otherParty1 = OtherParty.builder()
            .name(Name.builder().firstName("Other").lastName("PartyOne").build())
            .confidentialityRequired(YesNo.NO)
            .confidentialityRequiredChangedDate("2020-02-02")
            .build();

        OtherParty otherParty2 = OtherParty.builder()
            .name(Name.builder().firstName("Other").lastName("PartyTwo").build())
            .confidentialityRequired(YesNo.NO)
            .confidentialityRequiredChangedDate("2020-02-03")
            .build();

        Appeal appeal = Appeal.builder()
            .appellant(appellant)
            .benefitType(BenefitType.builder().code("childSupport").build())
            .build();

        List<CcdValue<ConfidentialityTabEntry>> results = ConfidentialityTab.getConfidentialityTabEntries(
            List.of(CcdValue.<OtherParty>builder().value(otherParty1).build(), CcdValue.<OtherParty>builder().value(otherParty2).build()),
            appeal
        );

        assertThat(results).hasSize(4);

        ConfidentialityTabEntry appellantEntry = results.getFirst().getValue();
        assertThat(appellantEntry.getName()).isEqualTo("App Ellant");
        assertThat(appellantEntry.getRole()).isEqualTo("Appellant");
        assertThat(appellantEntry.getConfidentialityRequired()).isEqualTo("Yes");
        assertThat(appellantEntry.getConfidentialityRequiredChangedDate()).isEqualTo("2020-01-01");

        ConfidentialityTabEntry appointeeEntry = results.get(1).getValue();
        assertThat(appointeeEntry.getName()).isEqualTo("Apo Intee");
        assertThat(appointeeEntry.getRole()).isEqualTo("Appointee");
        assertThat(appointeeEntry.getConfidentialityRequired()).isEqualTo("Yes");
        assertThat(appointeeEntry.getConfidentialityRequiredChangedDate()).isEqualTo("2020-01-01");

        ConfidentialityTabEntry otherPartyEntry = results.get(2).getValue();
        assertThat(otherPartyEntry.getName()).isEqualTo("Other PartyOne");
        assertThat(otherPartyEntry.getRole()).isEqualTo("Other Party #1");
        assertThat(otherPartyEntry.getConfidentialityRequired()).isEqualTo("No");
        assertThat(otherPartyEntry.getConfidentialityRequiredChangedDate()).isEqualTo("2020-02-02");

        // TODO This should be other party 2
        ConfidentialityTabEntry otherPartyEntry2 = results.get(3).getValue();
        assertThat(otherPartyEntry2.getName()).isEqualTo("Other PartyTwo");
        assertThat(otherPartyEntry2.getRole()).isEqualTo("Other Party #2");
        assertThat(otherPartyEntry2.getConfidentialityRequired()).isEqualTo("No");
        assertThat(otherPartyEntry2.getConfidentialityRequiredChangedDate()).isEqualTo("2020-02-03");
    }

    @Test
    void usesUndeterminedWhenConfidentialityRequiredIsNull() {
        Appellant appellant = Appellant.builder()
            .name(Name.builder().firstName("App").lastName("Ellant").build())
            .build();

        OtherParty otherParty = OtherParty.builder()
            .name(Name.builder().firstName("Other").lastName("Party").build())
            .build();

        Appeal appeal = Appeal.builder()
            .appellant(appellant)
            .benefitType(BenefitType.builder().code("childSupport").build())
            .build();

        List<CcdValue<ConfidentialityTabEntry>> results = ConfidentialityTab.getConfidentialityTabEntries(
            List.of(CcdValue.<OtherParty>builder().value(otherParty).build()),
            appeal
        );

        assertThat(results).hasSize(2);
        assertThat(results.get(0).getValue().getConfidentialityRequired()).isEqualTo("Undetermined");
        assertThat(results.get(1).getValue().getConfidentialityRequired()).isEqualTo("Undetermined");
    }

    @Test
    void buildsOnlyOtherPartyWhenAppellantMissing() {
        OtherParty otherParty = OtherParty.builder().build();
        Appeal appeal = Appeal.builder()
            .benefitType(BenefitType.builder().code("childSupport").build())
            .build();

        List<CcdValue<ConfidentialityTabEntry>> results = ConfidentialityTab.getConfidentialityTabEntries(
            List.of(CcdValue.<OtherParty>builder().value(otherParty).build()),
            appeal
        );

        assertThat(results).hasSize(1);
        ConfidentialityTabEntry entry = results.getFirst().getValue();
        assertThat(entry.getName()).isNull();
        assertThat(entry.getRole()).isEqualTo("Other Party #1");
        assertThat(entry.getConfidentialityRequired()).isEqualTo("Undetermined");
    }

    @Test
    void handlesNullNamesAndAppointeeWithUndeterminedConfidentiality() {
        Appellant appellant = Appellant.builder()
            .isAppointee("Yes")
            .appointee(Appointee.builder().build())
            .build();

        Appeal appeal = Appeal.builder()
            .appellant(appellant)
            .benefitType(BenefitType.builder().code("ChildSupport").build())
            .build();

        List<CcdValue<ConfidentialityTabEntry>> results = ConfidentialityTab.getConfidentialityTabEntries(
            List.of(),
            appeal
        );

        assertThat(results).hasSize(2);
        assertThat(results.get(0).getValue().getName()).isNull();
        assertThat(results.get(0).getValue().getConfidentialityRequired()).isEqualTo("Undetermined");
        assertThat(results.get(1).getValue().getName()).isNull();
        assertThat(results.get(1).getValue().getConfidentialityRequired()).isEqualTo("Undetermined");
        assertThat(results.get(1).getValue().getConfidentialityRequiredChangedDate()).isNull();
    }

    @Test
    void returnsOnlyAppellantWhenAppointeeMissing() {
        Appellant appellant = Appellant.builder()
            .name(Name.builder().firstName("App").lastName("Ellant").build())
            .build();

        Appeal appeal = Appeal.builder()
            .appellant(appellant)
            .benefitType(BenefitType.builder().code("childSupport").build())
            .build();

        List<CcdValue<ConfidentialityTabEntry>> results =
            ConfidentialityTab.getConfidentialityTabEntries(List.of(), appeal);

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getValue().getRole()).isEqualTo("Appellant");
    }

    @Test
    void skipsOtherPartyWhenValueIsNull() throws Exception {
        Appellant appellant = Appellant.builder()
            .name(Name.builder().firstName("App").lastName("Ellant").build())
            .build();

        Appeal appeal = Appeal.builder()
            .appellant(appellant)
            .benefitType(BenefitType.builder().code("childSupport").build())
            .build();

        CcdValue<OtherParty> ccdValue = CcdValue.<OtherParty>builder()
            .value(OtherParty.builder().build())
            .build();
        Field valueField = CcdValue.class.getDeclaredField("value");
        valueField.setAccessible(true);
        valueField.set(ccdValue, null);

        List<CcdValue<ConfidentialityTabEntry>> results =
            ConfidentialityTab.getConfidentialityTabEntries(List.of(ccdValue), appeal);

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getValue().getRole()).isEqualTo("Appellant");
    }

    @Test
    void doesNotAddAppointeeWhenIsAppointeeIsNotYes() {
        Appellant appellant = Appellant.builder()
            .name(Name.builder().firstName("App").lastName("Ellant").build())
            .isAppointee("No")
            .appointee(Appointee.builder().build())
            .build();

        Appeal appeal = Appeal.builder()
            .appellant(appellant)
            .benefitType(BenefitType.builder().code("childSupport").build())
            .build();

        List<CcdValue<ConfidentialityTabEntry>> results =
            ConfidentialityTab.getConfidentialityTabEntries(List.of(), appeal);

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getValue().getRole()).isEqualTo("Appellant");
    }

    @Test
    void returnsNullFromPrivateBuildersWhenAppealIsNull() throws Exception {
        Method buildAppellant = ConfidentialityTab.class
            .getDeclaredMethod("buildAppellantConfidentialityTabEntry", Appeal.class);
        buildAppellant.setAccessible(true);
        Object appellantResult = buildAppellant.invoke(null, new Object[] {null});
        assertThat(appellantResult).isNull();

        Method buildAppointee = ConfidentialityTab.class
            .getDeclaredMethod("buildAppellantAppointeeConfidentialityTabEntry", Appeal.class);
        buildAppointee.setAccessible(true);
        Object appointeeResult = buildAppointee.invoke(null, new Object[] {null});
        assertThat(appointeeResult).isNull();
    }
}
