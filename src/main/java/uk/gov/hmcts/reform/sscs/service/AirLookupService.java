package uk.gov.hmcts.reform.sscs.service;

import static java.util.Optional.ofNullable;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.*;
import static uk.gov.hmcts.reform.sscs.service.RegionalProcessingCenterService.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
    private static final Set<Benefit> AIR_LOOKUP_COLUMN_SAME_AS_PIP = Set.of(PIP, DLA, CARERS_ALLOWANCE, ATTENDANCE_ALLOWANCE);
    private static final AirlookupBenefitToVenue DEFAULT_VENUE = AirlookupBenefitToVenue.builder().pipVenue("Birmingham").esaOrUcVenue("Birmingham").build();

    private Map<String, String> lookupRegionalCentreByPostCode;
    private Map<String, AirlookupBenefitToVenue> lookupAirVenueNameByPostCode;
    private Map<String, Integer> lookupVenueIdByAirVenueName;

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
        lookupRegionalCentreByPostCode = new HashMap<>();
        lookupAirVenueNameByPostCode = new HashMap<>();
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
                for (Row row : sheet) {
                    if (row.getRowNum() == 0 || row.getRowNum() == 1) {
                        continue;
                    }
                    int postcodeColumn = 0;
                    Cell postcodeCell = row.getCell(postcodeColumn);
                    int regionalCentreColumn = 7;
                    Cell adminGroupCell = row.getCell(regionalCentreColumn);
                    int esaUcColumn = 3;
                    Cell esaCell = row.getCell(esaUcColumn);
                    int pipColumn = 6;
                    Cell pipOrUcCell = row.getCell(pipColumn);

                    if (postcodeCell != null && adminGroupCell != null
                            && postcodeCell.getCellType() == CellType.STRING && adminGroupCell.getCellType() == CellType.STRING) {
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
    public AirlookupBenefitToVenue lookupAirVenueNameByPostCode(String postcode) {
        return ofNullable(lookupAirVenueNameByPostCode.get(postcode.toLowerCase())).orElse(DEFAULT_VENUE);
    }

    public String lookupAirVenueNameByPostCode(String postcode, @NonNull BenefitType benefitType) {
        AirlookupBenefitToVenue venue = lookupAirVenueNameByPostCode(getFirstHalfOfPostcode(postcode));
        Optional<Benefit> benefitOptional = findBenefitByShortName(benefitType.getCode());

        if (isAirLookupColumnForBenefitTheSameAsPip(benefitOptional)) {
            return venue.getPipVenue();
        }
        return venue.getEsaOrUcVenue();
    }

    private boolean isAirLookupColumnForBenefitTheSameAsPip(Optional<Benefit> benefitOptional) {
        return benefitOptional.isPresent() && AIR_LOOKUP_COLUMN_SAME_AS_PIP.contains(benefitOptional.get());
    }
}
