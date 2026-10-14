package uk.gov.hmcts.reform.sscs.reference.data;

import static java.util.Objects.isNull;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.Separators;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Slf4j
class DefaultHearingDurationSpreadsheetTest {

    private static final boolean VALIDATE_COLOUR_CODED_ACTIONS = true;
    private static final Set<String> BENEFIT_CODES_TO_SKIP = Set.of("UC");

    private static final String SPREADSHEET = "default-duration-hearing.xlsx";
    private static final String HEARING_DURATIONS_JSON = "reference-data/hearing-durations.json";
    private static final String HEARING_DURATIONS_NEW_JSON = "src/main/resources/reference-data/hearing-durations-new.json";

    private static final int BENEFIT_CODE_COLUMN = 0;
    private static final int ISSUE_COLUMN = 1;
    private static final int FACE_TO_FACE_COLUMN = 2;
    private static final int INTERPRETER_COLUMN = 3;
    private static final int PAPER_COLUMN = 4;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    void shouldCheckThatProvidedSpreadsheetMatchesHearingDurationsJsonFile() throws IOException {
        final List<DefaultHearingDuration> spreadsheetDurations = parseSpreadsheet();
        final List<ExistingHearingDuration> existingDurations = parseHearingDurationsJson();

        final List<HearingDurationValues> expected = spreadsheetDurations
            .stream()
            .filter(duration -> !BENEFIT_CODES_TO_SKIP.contains(duration.getBenefitCode()))
            .map(DefaultHearingDurationSpreadsheetTest::toValues)
            .toList();
        final List<HearingDurationValues> actual = existingDurations
            .stream()
            .filter(duration -> !BENEFIT_CODES_TO_SKIP.contains(duration.getBenefitCode()))
            .map(DefaultHearingDurationSpreadsheetTest::toValues)
            .toList();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Disabled("Reads a hearing durations spreadsheet and updates existing durations with any changes. Only to be ran when changes provided and Json file needs to be updated")
    @Test
    void shouldGenerateHearingDurationsFromSpreadsheet() throws IOException {
        final List<DefaultHearingDuration> spreadsheetDurations = parseSpreadsheet();
        final List<ExistingHearingDuration> existingDurations = parseHearingDurationsJson();
        final Map<String, ExistingHearingDuration> existingDurationsByKey = existingDurations
            .stream()
            .collect(Collectors.toMap(duration -> key(duration.getBenefitCode(), duration.getIssue()), Function.identity()));

        final List<String> mismatches = new ArrayList<>();
        for (final DefaultHearingDuration spreadsheetDuration : spreadsheetDurations) {
            final String key = key(spreadsheetDuration.getBenefitCode(), spreadsheetDuration.getIssue());
            if (BENEFIT_CODES_TO_SKIP.contains(spreadsheetDuration.getBenefitCode())) {
                log.info("Skipping duration {}", key);
                continue;
            }
            final ExistingHearingDuration existingDuration = existingDurationsByKey.get(key);
            if (isNull(existingDuration)) {
                log.warn("Duration {} does not exist", key);
                continue;
            }
            update(existingDuration, spreadsheetDuration);
        }

        writeHearingDurationsJson(existingDurations);

        assertThat(existingDurations)
            .filteredOn(duration -> BENEFIT_CODES_TO_SKIP.contains(duration.getBenefitCode()))
            .isNotEmpty()
            .noneMatch(ExistingHearingDuration::isProcessed);

        log.info("Number of existing durations {}. Number of durations to process {}", existingDurations.size(),
            spreadsheetDurations.size());

        if (VALIDATE_COLOUR_CODED_ACTIONS) {
            assertThat(mismatches).isEmpty();
        }
    }

    private static void update(final ExistingHearingDuration existingDuration, final DefaultHearingDuration spreadsheetDuration) {
        existingDuration.setDurationFaceToFace(spreadsheetDuration.getDurationFaceToFace());
        existingDuration.setDurationInterpreter(spreadsheetDuration.getDurationInterpreter());
        existingDuration.setDurationPaper(spreadsheetDuration.getDurationPaper());
        existingDuration.setProcessed(true);
        spreadsheetDuration.setProcessed(true);
    }

    private static HearingDurationValues toValues(final DefaultHearingDuration duration) {
        return new HearingDurationValues(duration.getBenefitCode(), duration.getIssue(), duration.getDurationFaceToFace(),
            duration.getDurationInterpreter(), duration.getDurationPaper());
    }

    private static HearingDurationValues toValues(final ExistingHearingDuration duration) {
        return new HearingDurationValues(duration.getBenefitCode(), duration.getIssue(), duration.getDurationFaceToFace(),
            duration.getDurationInterpreter(), duration.getDurationPaper());
    }

    private static String key(final String benefitCode, final String issue) {
        return benefitCode + "_" + issue;
    }

    private static void writeHearingDurationsJson(final List<ExistingHearingDuration> durations) throws IOException {
        final DefaultIndenter indenter = new DefaultIndenter("  ", "\r\n");
        final DefaultPrettyPrinter printer = new DefaultPrettyPrinter().withSeparators(
            Separators.createDefaultInstance().withObjectFieldValueSpacing(Separators.Spacing.AFTER));
        printer.indentObjectsWith(indenter);
        printer.indentArraysWith(indenter);
        OBJECT_MAPPER.writer(printer).writeValue(Path.of(HEARING_DURATIONS_NEW_JSON).toFile(), durations);
    }

    private static String getString(final Cell cell) {
        if (isNull(cell) || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        final String value = cell.getStringCellValue().trim();
        return value.isEmpty() ? null : value;
    }

    private static Integer getInteger(final Cell cell) {
        if (isNull(cell) || cell.getCellType() != CellType.NUMERIC) {
            return null;
        }
        return (int) cell.getNumericCellValue();
    }

    record HearingDurationValues(String benefitCode,
                                 String issue,
                                 Integer durationFaceToFace,
                                 Integer durationInterpreter,
                                 Integer durationPaper) {
    }

    @Data
    @AllArgsConstructor
    static class DefaultHearingDuration {
        private String benefitCode;
        private String issue;
        private Integer durationFaceToFace;
        private Integer durationInterpreter;
        private Integer durationPaper;
        private boolean processed;
    }

    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class ExistingHearingDuration {
        private String benefitCode;
        private String issue;
        private Integer durationFaceToFace;
        private Integer durationInterpreter;
        private Integer durationPaper;
        @JsonIgnore
        private boolean processed;
    }

    private List<DefaultHearingDuration> parseSpreadsheet() throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(SPREADSHEET)) {
            Assertions.assertNotNull(inputStream);
            try (Workbook workbook = WorkbookFactory.create(inputStream)) {
                final List<DefaultHearingDuration> durations = new ArrayList<>();
                for (final Row row : workbook.getSheetAt(0)) {
                    if (row.getRowNum() == 0) {
                        continue;
                    }
                    final String benefitCode = getString(row.getCell(BENEFIT_CODE_COLUMN));
                    final String issue = getString(row.getCell(ISSUE_COLUMN));
                    if (isNull(benefitCode) || isNull(issue)) {
                        continue;
                    }
                    durations.add(new DefaultHearingDuration(benefitCode, issue, getInteger(row.getCell(FACE_TO_FACE_COLUMN)),
                        getInteger(row.getCell(INTERPRETER_COLUMN)), getInteger(row.getCell(PAPER_COLUMN)), false));
                }
                return durations;
            }
        }
    }

    private List<ExistingHearingDuration> parseHearingDurationsJson() throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(HEARING_DURATIONS_JSON)) {
            return OBJECT_MAPPER.readValue(inputStream, new TypeReference<>() {
            });
        }
    }
}
