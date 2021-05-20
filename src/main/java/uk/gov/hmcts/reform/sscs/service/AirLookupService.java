package uk.gov.hmcts.reform.sscs.service;

import static java.util.Optional.ofNullable;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.*;
import static uk.gov.hmcts.reform.sscs.service.RegionalProcessingCenterService.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitType;
import uk.gov.hmcts.reform.sscs.exception.AirLookupServiceException;
import uk.gov.hmcts.reform.sscs.model.AirlookupBenefitToVenue;

/**
 * Service that ingests a spreadsheet and a csv file containing the
 * venues and regional centres for handling all post codes.
 */

@Service
@Slf4j
public class AirLookupService {
    private static final String AIR_LOOKUP_FILE = "reference-data/AIRLookup14.xlsx";
    protected static final AirlookupBenefitToVenue DEFAULT_VENUE = AirlookupBenefitToVenue.builder().pipVenue("Birmingham").jsaVenue("Birmingham").esaOrUcVenue("Birmingham").build();
    private static final String AIR_LOOKUP_VENUE_IDS_CSV = "airLookupVenueIds.csv";
    private Map<String, String> lookupRegionalCentreByPostcode;
    private Map<String, AirlookupBenefitToVenue> lookupAirVenueNameByPostcode;
    private Map<String, Integer> lookupVenueIdByAirVenueName;
    private static final int POSTCODE_COLUMN = 0;
    private static final int REGIONAL_CENTRE_COLUMN = 7;
    private static final int ESA_UC_COLUMN = 3;
    private static final int JSA_COLUMN = 5;
    private static final int PIP_COLUMN = 6;
    private static final int IIDB_COLUMN = 1;

    public String lookupRegionalCentre(String postcode) {
        if (isFullPostCodeGiven(postcode)) {
            String outcode = trimLastThreeCharsForOutcode(postcode);
            return lookupRegionalCentreByOutcode(outcode);
        }
        return lookupRegionalCentreByOutcode(postcode.toLowerCase().trim());
    }

    private String trimLastThreeCharsForOutcode(String postcode) {
        int index = postcode.length() - 3;
        return postcode.toLowerCase().substring(0, index).trim();
    }

    private String lookupRegionalCentreByOutcode(String outcode) {
        return lookupRegionalCentreByPostcode.get(outcode);
    }

    private boolean isFullPostCodeGiven(String postcode) {
        return postcode.length() >= 5;
    }

    /**
     * Read in the AIRLookup RC spreadsheet and the venue id csv file.
     */
    @PostConstruct
    public void init() {
        initialiseLookupMaps();
        try {
            loadAndParseAirLookupFile();
        } catch (IOException e) {
            logErrorWithAirLookup(e);
        }
    }

    private void initialiseLookupMaps() {
        lookupRegionalCentreByPostcode = new HashMap<>();
        lookupAirVenueNameByPostcode = new HashMap<>();
        lookupVenueIdByAirVenueName = new HashMap<>();
    }

    private void logErrorWithAirLookup(IOException e) {
        String message = "Unable to read in spreadsheet with post code data: " + AIR_LOOKUP_FILE;
        AirLookupServiceException ex = new AirLookupServiceException(e);
        log.error(message, ex);
    }

    private void loadAndParseAirLookupFile() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(AIR_LOOKUP_FILE);
        Workbook wb2 = WorkbookFactory.create(classPathResource.getInputStream());
        parseAirLookupData(wb2);
        parseVenueData(wb2);
        log.debug("Successfully loaded lookup data for postcode endpoint");
    }

    /**
     * Read in spreadsheet and populate the Map with postcode.
     * @param wb the file on classpath
     */
    private void parseAirLookupData(Workbook wb)  {
        for (Sheet sheet: wb) {
            if (sheet.getSheetName().equalsIgnoreCase("All")) {
                parseLookupDataRows(sheet);
            }
        }
    }

    private void parseLookupDataRows(Sheet sheet) {
        for (Row row : sheet) {
            if (row.getRowNum() == 0 || row.getRowNum() == 1) {
                continue;
            }
            populateLookupData(row);
        }
    }

    private void populateLookupData(Row row) {
        Cell postcodeCell = row.getCell(POSTCODE_COLUMN);
        Cell adminGroupCell = row.getCell(REGIONAL_CENTRE_COLUMN);

        if (postcodeCell != null && adminGroupCell != null
                && postcodeCell.getCellType() == CellType.STRING
                && adminGroupCell.getCellType() == CellType.STRING) {
            populateLookupByPostcodeMaps(row);
        }
    }

    private void populateLookupByPostcodeMaps(Row row) {
        String postcode = getStringValue(row.getCell(POSTCODE_COLUMN)).toLowerCase().trim();
        populateLookupRegionalCentreByPostcodeMap(row, postcode);
        populateLookupAirVenueNameByPostcodeMap(postcode, row);
    }

    private void populateLookupRegionalCentreByPostcodeMap(Row row, String postcode) {
        Cell adminGroupCell = row.getCell(REGIONAL_CENTRE_COLUMN);
        lookupRegionalCentreByPostcode.put(postcode, getStringValue(adminGroupCell));
    }

    private void populateLookupAirVenueNameByPostcodeMap(String postcode, Row row) {
        Cell esaOrUcCell = row.getCell(ESA_UC_COLUMN);
        Cell jsaCell = row.getCell(JSA_COLUMN);
        Cell pipCell = row.getCell(PIP_COLUMN);
        Cell iidbCell = row.getCell(IIDB_COLUMN);
        AirlookupBenefitToVenue airlookupBenefitToVenue = AirlookupBenefitToVenue.builder()
                .esaOrUcVenue(getStringValue(esaOrUcCell))
                .jsaVenue(getStringValue(jsaCell))
                .pipVenue(getStringValue(pipCell))
                .iidbVenue(getStringValue(iidbCell))
                .build();
        lookupAirVenueNameByPostcode.put(postcode, airlookupBenefitToVenue);
    }

    /**
     * Read in csv file with mapping values between AIRLookup and GAPS.
     * @param wb the file on classpath
     */
    public void parseVenueData(Workbook wb) {

        for (Sheet sheet: wb) {
            if (sheet.getSheetName().equalsIgnoreCase(AIR_LOOKUP_VENUE_IDS_CSV)) {
                populateVenueDataRow(sheet);
            }
        }
    }

    private void populateVenueDataRow(Sheet sheet) {
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            populateLookupVenueIdByAirVenueNameMap(row);
        }
    }

    private void populateLookupVenueIdByAirVenueNameMap(Row row) {
        Cell lookupName = row.getCell(0);
        Cell venueId = row.getCell(1);
        lookupVenueIdByAirVenueName.put(getStringValue(lookupName), getIntValue(venueId));
    }

    private int getIntValue(Cell venueId) {
        return Double.valueOf(venueId.getNumericCellValue()).intValue();
    }

    private String getStringValue(Cell lookupName) {
        System.out.println(lookupName.getRichStringCellValue().getString());
        return lookupName.getRichStringCellValue().getString();
    }

    /**
     * Get the map with the first half of post code as the key and
     * the Regional Centre as the value.
     * @return map with the first half of post code as the key and the Regional Centre as the value
     */
    protected Map<String, String> getLookupRegionalCentreByPostcode() {
        return lookupRegionalCentreByPostcode;
    }

    /**
     * Get the map with the first half of the post code as the key
     * and the venue name as the value.
     * @return map with the first half of the post code as the key and the venue names as the values
     */
    protected Map<String, AirlookupBenefitToVenue> getLookupAirVenueNameByPostcode() {
        return lookupAirVenueNameByPostcode;
    }

    /**
     * Get the map with the air venue name as the key and the venue id
     * as the value.
     * @return map with the air venue name as the key and the venue id as the value
     */
    protected Map<String, Integer> getLookupVenueIdByAirVenueName() {
        return lookupVenueIdByAirVenueName;
    }

    /**
     * Return the venue names in the AirLookup spreadsheet for the given post code.
     * @param postcode The first half of a post code
     * @return venues
     */
    public AirlookupBenefitToVenue lookupAirVenueNameByPostCode(String postcode) {
        return ofNullable(lookupAirVenueNameByPostcode.get(postcode.toLowerCase())).orElse(DEFAULT_VENUE);
    }

    public String lookupAirVenueNameByPostCode(String postcode, @NonNull BenefitType benefitType) {
        AirlookupBenefitToVenue venue = lookupAirVenueNameByPostCode(getFirstHalfOfPostcode(postcode));
        Optional<Benefit> benefitOptional = findBenefitByShortName(benefitType.getCode());

        if (isAirLookupColumnForBenefitTheSameAsPip(benefitOptional)) {
            return venue.getPipVenue();
        } else if (isAirLookupColumnForBenefitTheSameAsJsa(benefitOptional)) {
            return venue.getJsaVenue();
        } else if (isAirLookupColumnForBenefitTheSameAsIidb(benefitOptional)) {
            return venue.getIidbVenue();
        }
        return venue.getEsaOrUcVenue();
    }

    private boolean isAirLookupColumnForBenefitTheSameAsPip(Optional<Benefit> benefitOptional) {
        return benefitOptional.map(Benefit::isAirLookupSameAsPip).orElse(false);
    }

    private boolean isAirLookupColumnForBenefitTheSameAsJsa(Optional<Benefit> benefitOptional) {
        return benefitOptional.map(Benefit::isAirLookupSameAsJsa).orElse(false);
    }

    private boolean isAirLookupColumnForBenefitTheSameAsIidb(Optional<Benefit> benefitOptional) {
        return benefitOptional.map(Benefit::isAirLookupSameAsIidb).orElse(false);
    }
}
