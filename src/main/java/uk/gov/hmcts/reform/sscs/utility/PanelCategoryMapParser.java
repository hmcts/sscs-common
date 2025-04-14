package uk.gov.hmcts.reform.sscs.utility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import uk.gov.hmcts.reform.sscs.reference.data.model.PanelCategory;

@Slf4j
public class PanelCategoryMapParser {
    private static final String JOH_TIER_FILE = "reference-data/JOHTier_PanelMemberComposition_1.1.xlsx";
    private static final String PANEL_CATEGORY_MAP_FILE = "src/main/resources/reference-data/panel-category-map.json";
    private static final String SHEET_NAME = "MASTER";
    private static final int BENEFIT_ISSUE_CODE_COLUMN = 0;
    private static final int CATEGORY_1_COLUMN = 5;
    private static final int CATEGORY_2_COLUMN = 6;
    private static final int PANEL_1_COLUMN = 8;
    private static final int PANEL_2_COLUMN = 10;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private PanelCategoryMapParser() {
    }

    public static void main(String[] args) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(JOH_TIER_FILE);
            Workbook workbook = new XSSFWorkbook(classPathResource.getInputStream());
            Sheet sheet = workbook.getSheet(SHEET_NAME);
            if (sheet == null) {
                log.error("Sheet {} not found in file {}", SHEET_NAME, JOH_TIER_FILE);
                throw new RuntimeException();
            }

            log.info("Parsing spreadsheet");

            List<PanelCategory> panelCategoryList = new ArrayList<>();

            for (int i = 1; i <=sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String benefitCode = getCellValue(row.getCell(BENEFIT_ISSUE_CODE_COLUMN));
                String category1 = getCellValue(row.getCell(CATEGORY_1_COLUMN));
                String category2 = getCellValue(row.getCell(CATEGORY_2_COLUMN));
                String panel1 = getCellValue(row.getCell(PANEL_1_COLUMN));
                String panel2 = getCellValue(row.getCell(PANEL_2_COLUMN));

                if (benefitCode.isEmpty()) continue;


                if (category1.toLowerCase().contains("specialism")) {
                    panelCategoryList.add(createPanelCategory(
                        benefitCode,
                        parseCategory(category1),
                        "1",
                        null,
                        parseJohTiers(panel1)));

                    if (!category2.isEmpty()) {
                        panelCategoryList.add(createPanelCategory(
                            benefitCode,
                            parseCategory(category2),
                            "2",
                            null,
                            parseJohTiers(panel2)));
                    }
                } else {
                    panelCategoryList.add(createPanelCategory(
                        benefitCode,
                        parseCategory(category1),
                        null,
                        null,
                        parseJohTiers(panel1)));

                    if (category2.toLowerCase().contains("fqpm")) {
                        panelCategoryList.add(createPanelCategory(
                            benefitCode,
                            parseCategory(category2),
                            null,
                            "true",
                            parseJohTiers(panel2)));
                    }
                }
            }

            File panelCategoryMapJson = new File(PANEL_CATEGORY_MAP_FILE);

            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .writerWithDefaultPrettyPrinter()
                .writeValue(panelCategoryMapJson, panelCategoryList);

            log.info("Created {} panel categories and saved in file: {}", panelCategoryList.size(), JOH_TIER_FILE);

        } catch (IOException e) {
            String message = "Unable to read in spreadsheet data: " + JOH_TIER_FILE;
            log.error(message, e);
            throw new RuntimeException(e);
        }
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    private static String parseCategory(String category) {
        if (category == null || category.isBlank()) return "";

        if ("N/A".equalsIgnoreCase(category.trim())) {
            return "N/A";
        }

        Matcher singleDigitNumber = Pattern.compile("\\b\\d\\b").matcher(category);

        return singleDigitNumber.find() ? singleDigitNumber.group() : "";
    }

    private static List<String> parseJohTiers(String tiers) {
        if (tiers == null || tiers.isBlank()) return Collections.emptyList();
        List<String> parsedTiers = new ArrayList<>();

        Matcher doubleDigitNumbers = Pattern.compile("\\b\\d{2}\\b").matcher(tiers);
        while (doubleDigitNumbers.find()) {
            parsedTiers.add(doubleDigitNumbers.group());
        }

        return parsedTiers;
    }

    private static PanelCategory createPanelCategory(String benefitIssueCode, String category, String specialismCount,
                                                  String fqpm, List<String> johTiers) {
        PanelCategory panelCategory = new PanelCategory();
        panelCategory.setBenefitIssueCode(benefitIssueCode);
        panelCategory.setCategory(category);
        if (specialismCount != null) panelCategory.setSpecialismCount(specialismCount);
        if (fqpm != null) panelCategory.setFqpm(fqpm);
        panelCategory.setJohTiers(johTiers);

        return panelCategory;
    }
}