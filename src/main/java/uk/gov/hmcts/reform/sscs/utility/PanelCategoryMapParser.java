package uk.gov.hmcts.reform.sscs.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.reform.sscs.reference.data.model.PanelCategory;

@Slf4j
public class PanelCategoryMapParser {
    private static final String JOH_TIER_CSV_FILE = "src/main/resources/reference-data/JOHTier_PanelMemberComposition_1.2.csv";
    private static final String PANEL_CATEGORY_MAP_JSON_FILE = "src/main/resources/reference-data/panel-category-map.json";

    private final CsvMapper csvMapper;
    private final CsvSchema schema;
    private final ObjectMapper objectMapper;

    public PanelCategoryMapParser() {
        this.csvMapper = new CsvMapper();
        this.schema = CsvSchema.builder()
            .addColumn("benefitIssueCode")
            .addColumn("col1")
            .addColumn("col2")
            .addColumn("col3")
            .addColumn("col4")
            .addColumn("category")
            .addColumn("fqpm")
            .addColumn("medicalMember")
            .addColumn("specialismCount")
            .addColumn("johTiers")
            .addColumn("col9")
            .addColumn("col10")
            .setUseHeader(true)
            .build();

        this.objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static void main(String[] args) {
        PanelCategoryMapParser parser = new PanelCategoryMapParser();
        try {
            List<PanelCategory> panelCategories = parser.createJson(new File(JOH_TIER_CSV_FILE));
            parser.writeToJson(panelCategories, new File(PANEL_CATEGORY_MAP_JSON_FILE));
            log.info("Panel-category-map generation was successful");
        } catch (IOException e) {
            log.error("Failed to process files. CSV Path: {}, JSON Path: {}. Error: {}",
                JOH_TIER_CSV_FILE, PANEL_CATEGORY_MAP_JSON_FILE, e.getMessage(), e);
        }
    }

    public List<PanelCategory> createJson(File csvFile) throws IOException {
        log.info("Reading and parsing CSV file: {}", csvFile.getAbsolutePath());

        try (MappingIterator<PanelCategory> panelCategoriesIterator = csvMapper
            .readerFor(PanelCategory.class)
            .with(schema)
            .readValues(csvFile)) {

            List<PanelCategory> panelCategories = panelCategoriesIterator.readAll();

            panelCategories.forEach(this::processPanelCategory);

            return panelCategories;
        }
    }

    private void processPanelCategory(PanelCategory panelCategory) {
        panelCategory.setJohTiers(parseJohTiers(panelCategory.getJohTiers()));
        panelCategory.setFqpm(yesToTrue(panelCategory.getFqpm()));
        panelCategory.setMedicalMember(yesToTrue(panelCategory.getMedicalMember()));
        panelCategory.setSpecialismCount(blankToNull(panelCategory.getSpecialismCount()));
    }

    public void writeToJson(List<PanelCategory> panelCategories, File jsonFile) throws IOException {
        log.info("Writing {} panel categories to JSON file: {}", panelCategories.size(), jsonFile.getAbsolutePath());
        objectMapper.writeValue(jsonFile, panelCategories);
    }

    private List<String> parseJohTiers(List<String> tiers) {
        return Optional.ofNullable(tiers)
            .orElse(List.of())
            .stream()
            .flatMap(tier -> Arrays.stream(tier.split(",")))
            .map(String::trim)
            .collect(Collectors.toList());
    }

    private String blankToNull(String value) {
        return Optional.ofNullable(value)
            .map(String::trim)
            .filter(trimmed -> !trimmed.isEmpty())
            .orElse(null);
    }

    private String yesToTrue(String value) {
        return Optional.ofNullable(value)
            .map(String::trim)
            .filter("yes"::equalsIgnoreCase)
            .map(trimmed -> "true")
            .orElse(null);
    }
}