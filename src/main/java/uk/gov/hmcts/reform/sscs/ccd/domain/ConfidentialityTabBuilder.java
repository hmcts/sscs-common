package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Builder;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class ConfidentialityTabBuilder {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d MMM yyyy, h:mm:ss a", Locale.UK);

    public static String buildConfidentialityTab(final Appeal appeal, final List<CcdValue<OtherParty>> otherParties) {
        final List<CcdValue<ConfidentialitySummaryEntry>> results = new ArrayList<>();

        addIfNotNull(results, buildAppellantConfidentialityTabEntry(appeal));
        addIfNotNull(results, buildAppellantAppointeeConfidentialityTabEntry(appeal));

        final AtomicInteger atomicInteger = new AtomicInteger(1);

        emptyIfNull(otherParties).stream().filter(Objects::nonNull).map(CcdValue::getValue)
                                 .map(otherParty -> buildOtherPartyEntry(otherParty, atomicInteger.getAndIncrement()))
                                 .forEach(entry -> results.add(CcdValue.<ConfidentialitySummaryEntry>builder().value(entry).build()));

        final var confidentialityMarkdown = new StringBuilder();
        results.forEach(entry -> confidentialityMarkdown.append(
            String.format("%s | %s | %s | %s\r%n", entry.getValue().party(), entry.getValue().name(),
                entry.getValue().confidentialityRequired(), entry.getValue().confidentialityRequiredChangedDate())));

        return """
            Party | Name | Confidentiality Status | Confidentiality Status Confirmed
            -|-|-|-
            %s
            """.formatted(confidentialityMarkdown.toString());
    }

    private static void addIfNotNull(final List<CcdValue<ConfidentialitySummaryEntry>> list, final ConfidentialitySummaryEntry entry) {
        if (entry != null) {
            list.add(CcdValue.<ConfidentialitySummaryEntry>builder().value(entry).build());
        }
    }

    private static ConfidentialitySummaryEntry buildAppellantConfidentialityTabEntry(final Appeal appeal) {
        if (missingAppellant(appeal)) {
            return null;
        }
        final Appellant appellant = appeal.getAppellant();
        return ConfidentialitySummaryEntry.builder().name(extractFullName(appellant.getName())).party("Appellant")
                                          .confidentialityRequired(getConfidentialityStatus(appellant.getConfidentialityRequired()))
                                          .confidentialityRequiredChangedDate(formatDate(appellant.getConfidentialityRequiredChangedDate())).build();
    }

    private static ConfidentialitySummaryEntry buildAppellantAppointeeConfidentialityTabEntry(final Appeal appeal) {
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

    private static ConfidentialitySummaryEntry buildOtherPartyEntry(final OtherParty otherParty, final int displayIndex) {
        return ConfidentialitySummaryEntry.builder().name(extractFullName(otherParty.getName()))
                                          .party("Other Party " + displayIndex)
                                          .confidentialityRequired(getConfidentialityStatus(otherParty.getConfidentialityRequired()))
                                          .confidentialityRequiredChangedDate(formatDate(otherParty.getConfidentialityRequiredChangedDate())).build();
    }

    private static boolean missingAppellant(final Appeal appeal) {
        return appeal == null || appeal.getAppellant() == null;
    }

    private static String formatDate(final LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(FORMATTER) : "";
    }

    private static String getConfidentialityStatus(final YesNo confidentialityRequired) {
        return confidentialityRequired != null ? confidentialityRequired.getValue() : "Undetermined";
    }

    private static String extractFullName(final Name name) {
        return name != null ? name.getFullNameNoTitle() : "";
    }

    @Builder
    record ConfidentialitySummaryEntry(String party, String name, String confidentialityRequired,
                                       String confidentialityRequiredChangedDate) {
    }
}
