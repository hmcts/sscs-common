package uk.gov.hmcts.reform.sscs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.service.AirLookupService.AIR_LOOKUP_FILE;
import static uk.gov.hmcts.reform.sscs.service.AirLookupService.AIR_LOOKUP_VENUE_IDS_CSV;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import uk.gov.hmcts.reform.sscs.model.AirlookupBenefitToVenue;
import uk.gov.hmcts.reform.sscs.model.VenueDetails;

public class AirLookupDataValidationTest {

    private final SoftAssertions softly = new SoftAssertions();

    private static Sheet airLookupVenueIdsSheet;

    private static final Map<String, String> venueIdNamesToIds = new HashMap<>();

    public static final int LOOKUP_NAME_CELL = 0;

    public static final int ID_CELL = 1;

    @BeforeAll
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
    public void testAirlookupPostcodesContainValidEntries() {
        assertThat(airLookupVenueIdsSheet)
            .isNotNull()
            .isNotEmpty();

        AirLookupService airLookupService = new AirLookupService();
        VenueDataLoader venueDataLoader = new VenueDataLoader();

        airLookupService.init();
        venueDataLoader.init();

        Map<String, VenueDetails> venueDetailsMap = venueDataLoader.getVenueDetailsMap();

        Map<String, AirlookupBenefitToVenue> lookupAirVenueNameByPostcode =
            airLookupService.getLookupAirVenueNameByPostcode();

        List<String> missingPostcodeVenues = new ArrayList<>();
        List<String> inactiveVenuesInUse = new ArrayList<>();

        Set<String> postCodeVenueNames = buildUniqueVenueNames(lookupAirVenueNameByPostcode);

        for (String postCodeVenueName : postCodeVenueNames) {
            if (!venueIdNamesToIds.containsKey(postCodeVenueName)) {
                missingPostcodeVenues.add(postCodeVenueName);
            } else {
                VenueDetails venueDetails = venueDetailsMap.get(venueIdNamesToIds.get(postCodeVenueName));
                if ("no".equalsIgnoreCase(venueDetails.getActive())) {
                    inactiveVenuesInUse.add(postCodeVenueName);
                }
            }
        }

        softly.assertThat(missingPostcodeVenues)
            .as("Unique entries in the postcode lookup worksheet must have a corresponding venue Id lookup.")
            .isEmpty();

        softly.assertThat(inactiveVenuesInUse)
            .as("Entries in the postcode lookup worksheet must correspond to an active venue.")
            .isEmpty();

        ensureAllAirLookupVenueIdEntriesAreInUse(postCodeVenueNames);
    }

    private void ensureAllAirLookupVenueIdEntriesAreInUse(Set<String> postCodeVenueNames) {
        List<String> venuesWithNoPostcodeEntries = new ArrayList<>();

        for (String venueNameToId : venueIdNamesToIds.keySet()) {
            if (!postCodeVenueNames.contains(venueNameToId)) {
                venuesWithNoPostcodeEntries.add(venueNameToId);
            }
        }

        softly.assertThat(venuesWithNoPostcodeEntries)
            .as("All entries in airLookupVenueIds should have at least one usage in the postcode lookup. ")
            .isEmpty();
    }

    private Set<String> buildUniqueVenueNames(Map<String, AirlookupBenefitToVenue> lookupAirVenueNameByPostcode) {
        Set<String> postCodeVenueNames = new HashSet<>();

        for (AirlookupBenefitToVenue airlookupBenefitToVenue : lookupAirVenueNameByPostcode.values()) {
            postCodeVenueNames.add(airlookupBenefitToVenue.getPipVenue());
            postCodeVenueNames.add(airlookupBenefitToVenue.getEsaOrUcVenue());
            postCodeVenueNames.add(airlookupBenefitToVenue.getJsaVenue());
            postCodeVenueNames.add(airlookupBenefitToVenue.getIidbVenue());
            postCodeVenueNames.add(airlookupBenefitToVenue.getCsaVenue());
        }

        return postCodeVenueNames;
    }

    @Test
    public void checkAirLookupIdsForDuplicateNames() throws IOException {
        assertThat(airLookupVenueIdsSheet)
            .isNotNull()
            .isNotEmpty();

        List<String> duplicates = checkForDuplicates(venueIdNamesToIds.keySet());

        assertThat(duplicates).isEmpty();
    }

    @Test
    public void checkAirLookupIdsForDuplicateGapsVenueIds() {
        assertThat(airLookupVenueIdsSheet)
            .isNotNull()
            .isNotEmpty();

        List<String> duplicates = checkForDuplicates(venueIdNamesToIds.values());

        assertThat(duplicates).isEmpty();
    }

    private static List<String> checkForDuplicates(Iterable<String> collection) {
        Set<String> uniqueEntries = new HashSet<>();

        List<String> duplicates = new ArrayList<>();

        for (String venueName : collection) {
            if (uniqueEntries.contains(venueName)) {
                duplicates.add(venueName);
            } else {
                uniqueEntries.add(venueName);
            }
        }

        return duplicates;
    }
}
