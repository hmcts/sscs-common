package uk.gov.hmcts.reform.sscs.utility;

import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.YES;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import uk.gov.hmcts.reform.sscs.ccd.domain.YesNo;
import uk.gov.hmcts.reform.sscs.reference.data.model.DefaultPanelComposition;

@Slf4j
public class DefaultPanelCompositionParser {
    private static final String JOH_TIER_CSV_FILE = "src/main/resources/reference-data/CSV_JOHTier_PanelMemberComposition_1.4.csv";
    private static final String PANEL_CATEGORY_MAP_JSON_FILE = "src/main/resources/reference-data/panel-category-map.json";

    private final CsvMapper csvMapper;
    private final CsvSchema schema;
    private final ObjectMapper objectMapper;

    public DefaultPanelCompositionParser() {
        this.csvMapper = new CsvMapper().enable(CsvParser.Feature.TRIM_SPACES);
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
            .addArrayColumn("johTiers", ",")
            .addColumn("col9")
            .addColumn("col10")
            .setUseHeader(true)
            .build();

        this.objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static void main(String[] args) {
        DefaultPanelCompositionParser parser = new DefaultPanelCompositionParser();
        try {
            List<DefaultPanelComposition> defaultPanelCompositions = parser.createJson(new File(JOH_TIER_CSV_FILE));
            parser.writeToJson(defaultPanelCompositions, new File(PANEL_CATEGORY_MAP_JSON_FILE));
            log.info("Panel-category-map generation was successful");
        } catch (IOException e) {
            log.error("Failed to process files. CSV Path: {}, JSON Path: {}. Error: {}",
                JOH_TIER_CSV_FILE, PANEL_CATEGORY_MAP_JSON_FILE, e.getMessage(), e);
        }
    }

    public List<DefaultPanelComposition> createJson(File csvFile) throws IOException {
        log.info("Reading and parsing CSV file: {}", csvFile.getAbsolutePath());
        try (MappingIterator<DefaultPanelComposition> panelCompositionsIterator =
                     csvMapper.readerFor(DefaultPanelComposition.class).with(schema)
                             .readValues(csvFile)) {
            return panelCompositionsIterator.readAll()
                    .stream()
                    .map(this::processPanelCompositionConfig)
                    .toList();
        }
    }

    private DefaultPanelComposition processPanelCompositionConfig(DefaultPanelComposition defaultPanelComposition) {
        defaultPanelComposition.setFqpm(yesOrNull(defaultPanelComposition.getFqpm()));
        defaultPanelComposition.setMedicalMember(yesOrNull(defaultPanelComposition.getMedicalMember()));
        defaultPanelComposition.setSpecialismCount(blankToNull(defaultPanelComposition.getSpecialismCount()));
        return defaultPanelComposition;
    }

    public void writeToJson(List<DefaultPanelComposition> panelCategories, File jsonFile) throws IOException {
        log.info("Writing {} panel categories to JSON file: {}", panelCategories.size(), jsonFile.getAbsolutePath());
        objectMapper.writeValue(jsonFile, panelCategories);
    }

    private String blankToNull(String value) {
        return Optional.ofNullable(value)
            .map(String::trim)
            .filter(StringUtils::isNotEmpty)
            .orElse(null);
    }

    private String yesOrNull(String value) {
        return Optional.ofNullable(value)
            .map(String::trim)
            .filter(YesNo::isYes)
            .map(yesValue -> YES.getValue().toLowerCase())
            .orElse(null);
    }
}
