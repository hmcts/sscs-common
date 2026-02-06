package uk.gov.hmcts.reform.sscs.ccd.domain.views;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appeal;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appellant;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appointee;
import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;
import uk.gov.hmcts.reform.sscs.ccd.domain.CcdValue;
import uk.gov.hmcts.reform.sscs.ccd.domain.ConfidentialityTabEntry;
import uk.gov.hmcts.reform.sscs.ccd.domain.Name;
import uk.gov.hmcts.reform.sscs.ccd.domain.OtherParty;
import uk.gov.hmcts.reform.sscs.ccd.domain.YesNo;

@UtilityClass
public class ConfidentialityTab {

    public static List<CcdValue<ConfidentialityTabEntry>> getConfidentialityTabEntries(List<CcdValue<OtherParty>> otherParties,
        Appeal appeal) {
        if (!isChildSupportBenefit(appeal)) {
            return List.of();
        }
        List<CcdValue<ConfidentialityTabEntry>> results = new ArrayList<>();

        ConfidentialityTabEntry appellantEntry = buildAppellantConfidentialityTabEntry(appeal);
        if (appellantEntry != null) {
            results.add(CcdValue.<ConfidentialityTabEntry>builder().value(appellantEntry).build());
        }

        ConfidentialityTabEntry appointeeEntry = buildAppellantAppointeeConfidentialityTabEntry(appeal);
        if (appointeeEntry != null) {
            results.add(CcdValue.<ConfidentialityTabEntry>builder().value(appointeeEntry).build());
        }

        List<CcdValue<OtherParty>> parties = emptyIfNull(otherParties);
        java.util.stream.IntStream.range(0, parties.size()).mapToObj(index -> {
                OtherParty otherParty = parties.get(index).getValue();
                if (otherParty == null) {
                    return null;
                }
                return buildConfidentialityTabEntry(otherParty, index + 1);
            }).filter(Objects::nonNull).map(entry -> CcdValue.<ConfidentialityTabEntry>builder().value(entry).build())
            .forEach(results::add);

        return results;
    }

    private static boolean isChildSupportBenefit(Appeal appeal) {
        if (appeal == null || appeal.getBenefitType() == null || appeal.getBenefitType().getCode() == null) {
            return false;
        }
        return Benefit.CHILD_SUPPORT.getShortName().equalsIgnoreCase(appeal.getBenefitType().getCode());
    }

    private static ConfidentialityTabEntry buildAppellantConfidentialityTabEntry(Appeal appeal) {
        if (appeal == null || appeal.getAppellant() == null) {
            return null;
        }
        Appellant appellant = appeal.getAppellant();
        Name name = appellant.getName();
        String fullName = name != null ? name.getFullNameNoTitle() : null;
        return ConfidentialityTabEntry.builder().name(fullName).role("Appellant").confidentialityRequired(
                appellant.getConfidentialityRequired() != null ? appellant.getConfidentialityRequired().getValue() : "Undetermined")
            .confidentialityRequiredChangedDate(appellant.getConfidentialityRequiredChangedDate()).build();
    }

    private static ConfidentialityTabEntry buildAppellantAppointeeConfidentialityTabEntry(Appeal appeal) {
        if (appeal == null || appeal.getAppellant() == null || appeal.getAppellant().getAppointee() == null
            || !YesNo.isYes(appeal.getAppellant().getIsAppointee())) {
            return null;
        }
        Appellant appellant = appeal.getAppellant();
        Appointee appointee = appellant.getAppointee();
        Name name = appointee.getName();
        String fullName = name != null ? name.getFullNameNoTitle() : null;
        return ConfidentialityTabEntry.builder().name(fullName).role("Appointee").confidentialityRequired(
                appellant.getConfidentialityRequired() != null ? appellant.getConfidentialityRequired().getValue() : "Undetermined")
            .confidentialityRequiredChangedDate(appellant.getConfidentialityRequiredChangedDate()).build();
    }

    private static ConfidentialityTabEntry buildConfidentialityTabEntry(OtherParty otherParty, int displayIndex) {
        Name name = otherParty.getName();
        String fullName = name != null ? name.getFullNameNoTitle() : null;
        return ConfidentialityTabEntry.builder().name(fullName).role("Other Party #" + displayIndex).confidentialityRequired(
                otherParty.getConfidentialityRequired() != null ? otherParty.getConfidentialityRequired().getValue() : "Undetermined")
            .confidentialityRequiredChangedDate(otherParty.getConfidentialityRequiredChangedDate()).build();
    }

}
