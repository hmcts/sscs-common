package uk.gov.hmcts.reform.sscs.reference.data;

import static java.util.Objects.nonNull;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;

@Slf4j
class DefaultHearingDurationSpreadsheetTest {

    private static final String SPREADSHEET = "Default Hearing Duration.xlsx";
    private static final String HEARING_DURATIONS_JSON = "reference-data/hearing-durations.json";
    private static final String HEARING_DURATIONS_NEW_JSON =
            "src/main/resources/reference-data/hearing-durations-new.json";

    @Data
    @AllArgsConstructor
    static class DefaultHearingDuration {
        private String benefitCode;
        private String issue;
        private Integer durationFaceToFace;
        private Integer durationInterpreter;
        private Integer durationPaper;
        private boolean processed;

        String key() {
            return benefitCode + "_" + issue;
        }
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

    @Test
    void shouldParseSpreadsheetIntoDefaultHearingDurations() throws IOException {
        final Map<String, DefaultHearingDuration> durations = parseSpreadsheet().stream()
                .collect(Collectors.toMap(DefaultHearingDuration::key, Function.identity()));
        final List<ExistingHearingDuration> existingDurationList = parseHearingDurationsJson();
        final Map<String, ExistingHearingDuration> existingDurations = existingDurationList.stream()
                .collect(Collectors.toMap(
                        duration -> duration.getBenefitCode() + "_" + duration.getIssue(),
                        Function.identity()));

        durations.values().forEach(duration -> {
            ExistingHearingDuration existingHearingDuration = existingDurations.get(duration.key());
            if (nonNull(existingHearingDuration)) {

                existingHearingDuration.setDurationFaceToFace(duration.getDurationFaceToFace());
                existingHearingDuration.setDurationInterpreter(duration.getDurationInterpreter());
                existingHearingDuration.setDurationPaper(duration.getDurationPaper());

                duration.setProcessed(true);
                existingHearingDuration.setProcessed(true);
            } else {
                log.warn("Duration {} does not exist", duration.key());
            }
        });

        writeHearingDurationsJson(existingDurationList);

        log.info("Number of existing durations {}. Number of durations to process {}", existingDurations.size(), durations.size());

    }

    private List<DefaultHearingDuration> parseSpreadsheet() throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(SPREADSHEET);
             Workbook workbook = WorkbookFactory.create(inputStream)) {
            final Sheet sheet = workbook.getSheetAt(0);
            final List<DefaultHearingDuration> durations = new ArrayList<>();
            for (final Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                final String benefitCode = getString(row.getCell(0));
                final String issue = getString(row.getCell(1));
                if (benefitCode == null || issue == null) {
                    continue;
                }
                durations.add(new DefaultHearingDuration(
                        benefitCode,
                        issue,
                        getInteger(row.getCell(2)),
                        getInteger(row.getCell(3)),
                        getInteger(row.getCell(4)), false));
            }
            return durations;
        }
    }

    private List<ExistingHearingDuration> parseHearingDurationsJson() throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(HEARING_DURATIONS_JSON)) {
            return new ObjectMapper().readValue(inputStream, new TypeReference<>() {});
        }
    }

    private void writeHearingDurationsJson(final List<ExistingHearingDuration> durations) throws IOException {
        final DefaultIndenter indenter = new DefaultIndenter("  ", "\r\n");
        final DefaultPrettyPrinter printer = new DefaultPrettyPrinter()
                .withSeparators(Separators.createDefaultInstance()
                        .withObjectFieldValueSpacing(Separators.Spacing.AFTER));
        printer.indentObjectsWith(indenter);
        printer.indentArraysWith(indenter);
        new ObjectMapper().writer(printer).writeValue(Path.of(HEARING_DURATIONS_NEW_JSON).toFile(), durations);
    }

    private static String getString(final Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        final String value = cell.getStringCellValue().trim();
        return value.isEmpty() ? null : value;
    }

    private static Integer getInteger(final Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            return null;
        }
        return (int) cell.getNumericCellValue();
    }
}
