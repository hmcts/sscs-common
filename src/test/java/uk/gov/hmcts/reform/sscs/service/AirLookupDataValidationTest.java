package uk.gov.hmcts.reform.sscs.service;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import uk.gov.hmcts.reform.sscs.model.AirlookupBenefitToVenue;
import uk.gov.hmcts.reform.sscs.model.VenueDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.service.AirLookupService.AIR_LOOKUP_FILE;
import static uk.gov.hmcts.reform.sscs.service.AirLookupService.AIR_LOOKUP_VENUE_IDS_CSV;

public class AirLookupDataValidationTest {

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    private static Sheet airLookupVenueIdsSheet;

    private static final Map<String, String> venueIdNamesToIds = new HashMap<>();

    public static final int LOOKUP_NAME_CELL = 0;

    public static final int ID_CELL = 1;

    private AirLookupService airLookupService;

    private VenueDataLoader venueDataLoader;

    @BeforeClass
    public static void setup() throws IOException {
        DataFormatter dataFormatter = new DataFormatter();
        ClassPathResource airLookupFile = new ClassPathResource(AIR_LOOKUP_FILE);
        Workbook workbook = WorkbookFactory.create(airLookupFile.getInputStream());

        airLookupVenueIdsSheet = workbook.getSheet(AIR_LOOKUP_VENUE_IDS_CSV);

        for (Row row : airLookupVenueIdsSheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            String venueName = row.getCell(LOOKUP_NAME_CELL).getRichStringCellValue().getString();
            String venueId = dataFormatter.formatCellValue(row.getCell(ID_CELL));
            venueIdNamesToIds.put(venueName, venueId);
        }
    }


    @Test
    public void testAirlookupPostcodesContainValidEntries(){
        assertThat(airLookupVenueIdsSheet)
            .isNotNull()
            .isNotEmpty();

        airLookupService = new AirLookupService();
        venueDataLoader = new VenueDataLoader();

        airLookupService.init();
        venueDataLoader.init();

        Map<String, VenueDetails> venueDetailsMap = venueDataLoader.getVenueDetailsMap();

        Set<String> postCodeVenueNames = new HashSet<>();

        Map<String, AirlookupBenefitToVenue> lookupAirVenueNameByPostcode =
            airLookupService.getLookupAirVenueNameByPostcode();

        List<String> missingVenues = new ArrayList<>();
        List<String> inactiveVenuesInUse = new ArrayList<>();

        buildUniqueVenueNames(postCodeVenueNames, lookupAirVenueNameByPostcode);

        for (String postCodeVenueName : postCodeVenueNames) {
            if (!venueIdNamesToIds.containsKey(postCodeVenueName)) {
                missingVenues.add(postCodeVenueName);
            } else {
                VenueDetails venueDetails = venueDetailsMap.get(venueIdNamesToIds.get(postCodeVenueName));
                if ("no".equalsIgnoreCase(venueDetails.getActive())) {
                    inactiveVenuesInUse.add(postCodeVenueName);
                }
            }
        }

        softly.assertThat(missingVenues)
            .as("Unique entries in the postcode lookup worksheet must have a corresponding venue Id lookup.")
            .isEmpty();

        softly.assertThat(inactiveVenuesInUse)
            .as("Entries in the postcode lookup worksheet must correspond to an active venue.")
            .isEmpty();
    }

    private static void buildUniqueVenueNames(Set<String> postCodeVenueNames,
                                  Map<String, AirlookupBenefitToVenue> lookupAirVenueNameByPostcode) {
        for (AirlookupBenefitToVenue airlookupBenefitToVenue : lookupAirVenueNameByPostcode.values()) {
            postCodeVenueNames.add(airlookupBenefitToVenue.getPipVenue());
            postCodeVenueNames.add(airlookupBenefitToVenue.getEsaOrUcVenue());
            postCodeVenueNames.add(airlookupBenefitToVenue.getJsaVenue());
            postCodeVenueNames.add(airlookupBenefitToVenue.getIidbVenue());
            postCodeVenueNames.add(airlookupBenefitToVenue.getCsaVenue());
        }
    }


    @Test
    public void checkAirLookupIdsForDuplicateNames() throws IOException {
        assertThat(airLookupVenueIdsSheet)
            .isNotNull()
            .isNotEmpty();

        Set<String> duplicateSet = new HashSet<>();

        List<String> results = new ArrayList<>();

        for (String venueName : venueIdNamesToIds.keySet()) {
            if (duplicateSet.contains(venueName)) {
                results.add(venueName);
            } else {
                duplicateSet.add(venueName);
            }
        }

        assertThat(results).isEmpty();
    }
}
