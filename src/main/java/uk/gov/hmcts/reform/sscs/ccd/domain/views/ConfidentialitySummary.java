package uk.gov.hmcts.reform.sscs.ccd.domain.views;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.experimental.UtilityClass;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appeal;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appellant;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appointee;
import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;
import uk.gov.hmcts.reform.sscs.ccd.domain.CcdValue;
import uk.gov.hmcts.reform.sscs.ccd.domain.ConfidentialitySummaryEntry;
import uk.gov.hmcts.reform.sscs.ccd.domain.Name;
import uk.gov.hmcts.reform.sscs.ccd.domain.OtherParty;
import uk.gov.hmcts.reform.sscs.ccd.domain.YesNo;

@UtilityClass
public class ConfidentialitySummary {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d MMM yyyy, h:mm:ss a", Locale.UK);

    public static List<CcdValue<ConfidentialitySummaryEntry>> getConfidentialitySummaryEntries(
        List<CcdValue<OtherParty>> otherParties, Appeal appeal) {

        if (!isChildSupportBenefit(appeal)) {
            return List.of();
        }
        List<CcdValue<ConfidentialitySummaryEntry>> results = new ArrayList<>();

        addIfNotNull(results, buildAppellantConfidentialityTabEntry(appeal));
        addIfNotNull(results, buildAppellantAppointeeConfidentialityTabEntry(appeal));

        final AtomicInteger atomicInteger = new AtomicInteger(1);

        emptyIfNull(otherParties).stream().filter(Objects::nonNull).map(CcdValue::getValue).filter(Objects::nonNull)
            .map(otherParty -> buildOtherPartyEntry(otherParty, atomicInteger.getAndIncrement()))
            .forEach(entry -> results.add(CcdValue.<ConfidentialitySummaryEntry>builder().value(entry).build()));

        return results;
    }

    private static void addIfNotNull(List<CcdValue<ConfidentialitySummaryEntry>> list, ConfidentialitySummaryEntry entry) {
        if (entry != null) {
            list.add(CcdValue.<ConfidentialitySummaryEntry>builder().value(entry).build());
        }
    }

    private static String getConfidentialityStatus(YesNo confidentialityRequired) {
        return confidentialityRequired != null ? confidentialityRequired.getValue() : "Undetermined";
    }

    private static String extractFullName(Name name) {
        return name != null ? name.getFullNameNoTitle() : null;
    }

    private static ConfidentialitySummaryEntry buildAppellantConfidentialityTabEntry(Appeal appeal) {
        if (missingAppellant(appeal)) {
            return null;
        }
        final Appellant appellant = appeal.getAppellant();
        return ConfidentialitySummaryEntry.builder().name(extractFullName(appellant.getName())).party("Appellant")
            .confidentialityRequired(getConfidentialityStatus(appellant.getConfidentialityRequired()))
            .confidentialityRequiredChangedDate(formatDate(appellant.getConfidentialityRequiredChangedDate())).build();
    }

    private static ConfidentialitySummaryEntry buildAppellantAppointeeConfidentialityTabEntry(Appeal appeal) {
        if (missingAppellant(appeal) || appeal.getAppellant().getAppointee() == null || !YesNo.isYes(
            appeal.getAppellant().getIsAppointee())) {
            return null;
        }
        final Appointee appointee = appeal.getAppellant().getAppointee();

        return ConfidentialitySummaryEntry.builder().name(extractFullName(appointee.getName())).party("Appointee")
            .confidentialityRequired(getConfidentialityStatus(appeal.getAppellant().getConfidentialityRequired()))
            .confidentialityRequiredChangedDate(formatDate(appeal.getAppellant().getConfidentialityRequiredChangedDate()))
            .build();
    }

    private static boolean missingAppellant(Appeal appeal) {
        return appeal.getAppellant() == null;
    }

    private String formatDate(final LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(FORMATTER) : null;
    }

    private static ConfidentialitySummaryEntry buildOtherPartyEntry(OtherParty otherParty, int displayIndex) {
        return ConfidentialitySummaryEntry.builder().name(extractFullName(otherParty.getName()))
            .party("Other Party #" + displayIndex)
            .confidentialityRequired(getConfidentialityStatus(otherParty.getConfidentialityRequired()))
            .confidentialityRequiredChangedDate(formatDate(otherParty.getConfidentialityRequiredChangedDate())).build();
    }

    private static boolean isChildSupportBenefit(Appeal appeal) {
        if (appeal == null || appeal.getBenefitType() == null || appeal.getBenefitType().getCode() == null) {
            return false;
        }
        return Benefit.CHILD_SUPPORT.getShortName().equalsIgnoreCase(appeal.getBenefitType().getCode());
    }
}
