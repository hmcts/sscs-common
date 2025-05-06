package uk.gov.hmcts.reform.sscs.service;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.domain.Venue;
import uk.gov.hmcts.reform.sscs.model.VenueDetails;

public class VenueDataLoaderTest {

    private VenueDataLoader venueDataLoader;

    private static final List<String> venueDetailsByLeedsRpc = Arrays.asList(
            "517400", "449358", "563156", "45900", "744412", "572158", "288691", "562808", "720624", "427519",
            "366796", "107581", "495952", "852649", "491107", "195520", "641199", "574546", "320113");

    @BeforeEach
    public void setUp() {
        venueDataLoader = new VenueDataLoader();
        venueDataLoader.init();
    }

    @ParameterizedTest
    @CsvSource({"1", "2", "3", "8", "11", "22", "24", "30", "34", "35", "37", "38", "40", "44", "45", "48", "52", "75",
        "85", "86", "92", "94", "97", "105", "106", "109", "110", "114", "115", "132", "133", "146", "147", "151",
        "170", "171", "175", "177", "178", "186", "189", "199", "203", "221", "224", "225", "226", "227", "228", "234",
        "241", "260", "266", "268", "991", "996", "998", "1000", "1001", "1003", "1004", "1005", "1008", "1009", "1012",
        "1013", "1014", "1015", "1016", "1020", "1021", "1027", "1028", "1030", "1033", "1035", "1038", "1045", "1046", "1047",
        "1048", "1049", "1050", "1052", "1106", "1113", "1114", "1115", "1120", "1121", "1122", "1126", "1128", "1133",
        "1134", "1137", "1138", "1140", "1141", "1142", "1144", "1145", "1147", "1149", "1156", "1157", "1158", "1160",
        "1183", "1185", "1189", "1192", "1193", "1194", "1195", "1196", "1197", "1198", "1199", "1200", "1201", "1202", "1203",
        "1207", "1208", "1212", "1214", "1217", "1218", "1220", "1224", "1232", "1239", "1253", "1261"})
    public void venuesShouldNotBeActive(String id) {
        assertTrue(venueDataLoader.getVenueDetailsMap().get(id).getActive().equals("No"));
    }

    @ParameterizedTest
    @CsvSource({"7", "9", "10", "43", "49", "51", "53", "60", "61", "62", "63", "64", "65", "67", "68", "70", "71",
        "79", "81", "84", "89", "90", "91", "96", "101", "104", "108", "111", "112", "122", "123", "134", "140", "141",
        "142", "143", "154", "156", "168", "173", "179", "185", "187", "192", "194", "195", "198", "200", "201", "205",
        "206", "209", "210", "232", "233", "236", "240", "261", "262", "265", "267", "992", "993", "1002", "1025",
        "1029", "1037", "1043", "1044", "1053", "1104", "1107", "1111", "1112", "1118", "1127", "1129", "1130",
        "1131", "1132", "1135", "1136", "1139", "1143", "1146", "1148", "1150", "1151", "1152", "1153", "1154", "1155",
        "1159", "1161", "1182", "1184", "1186", "1188", "1190", "1209", "1210", "1211", "1213", "1215", "1216",
        "1219", "1221", "1222", "1223", "1225", "1226", "1227", "1228", "1229", "1230", "1233", "1234", "1235", "1236",
        "1237", "1238", "1240", "1241", "1248", "1249", "1254", "1250", "1256", "1257", "1259"})
    public void venuesShouldBeActiveAndHaveAGoogleLink(String id) {
        assertTrue(venueDataLoader.getVenueDetailsMap().get(id).getActive().equals("Yes"));
        assertTrue(venueDataLoader.getVenueDetailsMap().get(id).getUrl().contains("https://"));
        assertTrue(venueDataLoader.getVenueDetailsMap().get(id).getUrl().contains("goo"));
    }

    @Test
    public void venuesShouldEitherBeActiveOrNotActive() {
        venueDataLoader.getVenueDetailsMap().values()
                .forEach(venueDetails ->
                        assertTrue(
                            venueDetails.getActive().contains(venueDetails.getComments().isEmpty() ? "Yes" : "No"),
                            format("%s is incorrect", venueDetails.getVenueId()))
                );
    }

    @Test
    public void venuesActiveShouldHaveAGoogleLink() {
        venueDataLoader.getVenueDetailsMap().values()
                .stream()
                .filter(venueDetails -> venueDetails.getActive().equals("Yes"))
                .forEach(venueDetails -> {
                    assertTrue(
                        venueDetails.getUrl().contains("https://"),
                        format("%s is incorrect", venueDetails.getVenueId()));
                    assertTrue(
                        venueDetails.getUrl().contains("goo"),
                        format("%s is incorrect", venueDetails.getVenueId()));
                });
    }

    @Test
    public void venuesThatAreActiveTheirThreeDigitReferenceShouldBeUnique() {
        long maxSize = venueDataLoader.getVenueDetailsMap().values().stream().filter(v -> v.getActive().equals("Yes")).count();
        long distinctSize = venueDataLoader.getVenueDetailsMap().values().stream().filter(v -> v.getActive().equals("Yes")).map(v -> v.getThreeDigitReference()).distinct().count();

        long adjustForDuplicateSc238 = 2;

        assertTrue(maxSize == distinctSize + adjustForDuplicateSc238);
    }

    @Test
    public void venuesShouldHaveGapsVenueName() {
        venueDataLoader.getVenueDetailsMap().values().forEach(venueDetails -> {
            assertNotNull(format("%s is not null", venueDetails.getVenueId()), venueDetails.getGapsVenName());
        });
    }

    @Test
    public void shouldGetGapsVenueNameForGivenVenueId() {
        Venue venue = Venue.builder().name("test").address(Address.builder().postcode("postcode").build()).build();
        String result = venueDataLoader.getGapVenueName(venue, "68");
        assertEquals("Liverpool", result);
    }

    @Test
    public void shouldGetGapsVenueNameIfVenueIdIsBlankAndVenueIsNotBlank() {
        Venue venue = Venue.builder().name("Barnsley SSCS Hearing Venue").address(Address.builder().postcode("S70 1WA").build()).build();
        String result = venueDataLoader.getGapVenueName(venue, null);
        assertEquals("Barnsley", result);
    }

    @Test
    public void shouldGetExistingVenueNameIfVenueIdIsBlankAndVenueIsNotFound() {
        Venue venue = Venue.builder().name("test").address(Address.builder().postcode("postcode").build()).build();
        String result = venueDataLoader.getGapVenueName(venue, null);
        assertEquals("test", result);
    }

    @Test
    public void shouldGetExistingVenueNameIfBothVenueIdAndVenueAreBlank() {
        String result = venueDataLoader.getGapVenueName(null, null);
        assertNull(result);
    }

    @DisplayName("When a valid epims ID is searched, getVenue return the correct venue details")
    @Test
    public void testGetVenue() {
        VenueDetails result = venueDataLoader.getActiveVenueDetailsMapByEpimsId().get("45900");

        assertNotNull(result);
        assertEquals("1236", result.getVenueId());
        assertEquals("SC287", result.getThreeDigitReference());
        assertEquals("45900", result.getEpimsId());
    }

    public void shouldGetEpimsIdForGivenVenueId() {
        String result = venueDataLoader.getVenueDetailsMap().get("68").getEpimsId();
        assertEquals("196538", result);
    }

    @Test
    public void shouldGetEpimsIdForGivenPostcode() {
        String result = venueDataLoader.getActiveVenueDetailsMapByPostcode().get("MK9 2AJ").getEpimsId();
        assertEquals("497356", result);
    }

    @Test
    public void testActiveVenueEpimsIdsMapByRpcReturnsVenues() {
        List<VenueDetails> result = venueDataLoader.getActiveVenueEpimsIdsMapByRpc().get("SSCS Leeds");
        assertFalse(result.isEmpty());
        assertEquals(19, result.size());
        result.forEach(vd -> assertTrue(venueDetailsByLeedsRpc.contains(vd.getEpimsId())));
    }

    @Test
    public void testActiveVenueEpimsIdsMapByRpcReturnsNullOnErroneousInput() {
        List<VenueDetails> result = venueDataLoader.getActiveVenueEpimsIdsMapByRpc().get("SSCS Newcastle");
        assertNull(result);
    }
}
