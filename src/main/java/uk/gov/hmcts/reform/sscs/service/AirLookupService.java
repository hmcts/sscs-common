package uk.gov.hmcts.reform.sscs.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.exception.AirLookupServiceException;
import uk.gov.hmcts.reform.sscs.model.AirlookupBenefitToVenue;

/**
 * Service that ingests a spreadsheet and a csv file containing the
 * venues and regional centres for handling all post codes.
 */

@Service
public class AirLookupService {
    public String lookupRegionalCentre(String postcode) {
        //full post code
        if (postcode.length() >= 5) {
            int index = postcode.length() - 3;
            //trim last 3 chars to leave the outcode
            String outcode = postcode.toLowerCase().substring(0, index).trim();
            return lookupRegionalCentreByPostCode.get(outcode);
        } else {
            //try it as the outcode
            return lookupRegionalCentreByPostCode.get(postcode.toLowerCase().trim());
        }
    }

    private static final Logger LOG = getLogger(AirLookupService.class);
    private static int POSTCODE_COLUMN = 0;
    private static int REGIONAL_CENTRE_COLUMN = 7;
    private static int ESA_UC_COLUMN = 3;
    private static int PIP_COLUMN = 6;

    private static AirlookupBenefitToVenue DEFAULT_VENUE = AirlookupBenefitToVenue.builder().pipVenue("Birmingham").esaOrUcVenue("Birmingham").build();

    private Map<String, String> lookupRegionalCentreByPostCode;
    private Map<String, AirlookupBenefitToVenue> lookupAirVenueNameByPostCode;
    Map<String, Integer> lookupVenueIdByAirVenueName;

    /**
     * Read in the AIRLookup RC spreadsheet and the venue id csv file.
     */
    @PostConstruct
    public void init() {
        lookupRegionalCentreByPostCode = new HashMap<>();
        lookupAirVenueNameByPostCode = new HashMap<>();
        lookupVenueIdByAirVenueName = new HashMap<>();

        String airlookupFilePath = "reference-data/AIRLookup9.xlsx";
        try {
            ClassPathResource classPathResource = new ClassPathResource(airlookupFilePath);

            OPCPackage pkg = OPCPackage.open(classPathResource.getInputStream());
            Workbook wb2 = WorkbookFactory.create(pkg);

            parseAirLookupData(wb2);
            parseVenueData(wb2);

            LOG.debug("Successfully loaded lookup data for postcode endpoint");
        } catch (IOException e) {
            String message = "Unable to read in spreadsheet with post code data: " + airlookupFilePath;
            AirLookupServiceException ex = new AirLookupServiceException(e);
            LOG.error(message, ex);
        } catch (InvalidFormatException e) {
            String message = "Format of airlookup file not valid in path: " + airlookupFilePath;
            AirLookupServiceException ex = new AirLookupServiceException(e);
            LOG.error(message, ex);
        }
    }

    /**
     * Read in spreadsheet and populate the Map with postcode.
     * @param wb the file on classpath
     */
    private void parseAirLookupData(Workbook wb)  {

        for (Sheet sheet: wb) {
            if (sheet.getSheetName().equalsIgnoreCase("All")) {
                for (Row row : sheet) {
                    if (row.getRowNum() == 0 || row.getRowNum() == 1) {
                        continue;
                    }
                    Cell postcodeCell = row.getCell(POSTCODE_COLUMN);
                    Cell adminGroupCell = row.getCell(REGIONAL_CENTRE_COLUMN);
                    Cell esaCell = row.getCell(ESA_UC_COLUMN);
                    Cell pipOrUcCell = row.getCell(PIP_COLUMN);

                    if (postcodeCell != null && adminGroupCell != null
                            && postcodeCell.getCellTypeEnum() == CellType.STRING && adminGroupCell.getCellTypeEnum() == CellType.STRING) {
                        lookupRegionalCentreByPostCode.put(postcodeCell.getRichStringCellValue().getString().toLowerCase().trim(), adminGroupCell.getRichStringCellValue().getString());

                        lookupAirVenueNameByPostCode.put(postcodeCell.getRichStringCellValue().getString().toLowerCase().trim(),
                            AirlookupBenefitToVenue.builder()
                                    .esaOrUcVenue(esaCell.getRichStringCellValue().getString())
                                    .pipVenue(pipOrUcCell.getRichStringCellValue().getString())
                                    .build());
                    }
                }
            }
        }
    }

    /**
     * Read in csv file with mapping values between AIRLookup and GAPS.
     * @param wb the file on classpath
     */
    public void parseVenueData(Workbook wb) {

        for (Sheet sheet: wb) {
            if (sheet.getSheetName().equalsIgnoreCase("airLookupVenueIds.csv")) {
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        continue;
                    }
                    Cell lookupName = row.getCell(0);
                    Cell venueId = row.getCell(1);
                    lookupVenueIdByAirVenueName.put(lookupName.getRichStringCellValue().getString(), Double.valueOf(venueId.getNumericCellValue()).intValue());
                }
            }
        }
    }

    /**
     * Get the map with the first half of post code as the key and
     * the Regional Centre as the value.
     * @return map with the first half of post code as the key and the Regional Centre as the value
     */
    protected Map<String, String> getLookupRegionalCentreByPostCode() {
        return lookupRegionalCentreByPostCode;
    }

    /**
     * Get the map with the first half of the post code as the key
     * and the venue name as the value.
     * @return map with the first half of the post code as the key and the venue names as the values
     */
    protected Map<String, AirlookupBenefitToVenue> getLookupAirVenueNameByPostCode() {
        return lookupAirVenueNameByPostCode;
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
    protected AirlookupBenefitToVenue lookupAirVenueNameByPostCode(String postcode) {
        AirlookupBenefitToVenue value = lookupAirVenueNameByPostCode.get(postcode.toLowerCase());
        if (value == null) {
            return DEFAULT_VENUE;
        }
        return value;
    }
}
