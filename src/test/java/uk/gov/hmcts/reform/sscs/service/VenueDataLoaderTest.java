package uk.gov.hmcts.reform.sscs.service;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.*;

import java.util.Arrays;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.domain.Venue;
import uk.gov.hmcts.reform.sscs.model.VenueDetails;

@RunWith(JUnitParamsRunner.class)
public class VenueDataLoaderTest {

    private VenueDataLoader venueDataLoader;

    private static final List<String> venueDetailsByLeedsRpc = Arrays.asList(
            "517400", "449358", "563156",
            "45900", "744412", "572158",
            "288691", "562808", "720624",
            "427519", "366796", "999974",
            "107581", "197852", "495952",
            "852649", "491107", "195520",
            "641199", "574546");

    @Before
    public void setUp() {
        venueDataLoader = new VenueDataLoader();
        venueDataLoader.init();
    }

    @Test
    @Parameters({"1158", "1144", "1145", "1027", "1028", "1012", "1", "3", "8", "11", "51", "75", "105", "178", "186",
            "228", "268", "1046", "1048", "1133", "1134", "1142",
            "1160", "1183", "1196", "1203", "1239"})
    public void venuesShouldNotBeActive(String id) {
        assertThat(venueDataLoader.getVenueDetailsMap().get(id).getActive(), is(NO));
    }

    @Test
    @Parameters({"63", "94", "97", "106", "115", "1043"})
    public void venuesShouldBeActiveAndHaveAGoogleLink(String id) {
        assertThat(venueDataLoader.getVenueDetailsMap().get(id).getActive(), is(YES));
        assertThat(venueDataLoader.getVenueDetailsMap().get(id).getUrl(), containsString("https://"));
        assertThat(venueDataLoader.getVenueDetailsMap().get(id).getUrl(), containsString("goo"));
    }

    @Test
    public void venuesShouldEitherBeActiveOrNotActive() {
        venueDataLoader.getVenueDetailsMap().values().forEach(venueDetails -> {
            assertThat(format("%s is incorrect", venueDetails.getVenueId()), venueDetails.getActive(), is(venueDetails.getComments().isEmpty() ? YES : NO));
        });
    }

    @Test
    public void venuesActiveShouldHaveAGoogleLink() {
        venueDataLoader.getVenueDetailsMap().values().stream().filter(venueDetails -> isYes(venueDetails.getActive())).forEach(venueDetails -> {
            assertThat(format("%s is incorrect", venueDetails.getVenueId()), venueDetails.getUrl(), containsString("https://"));
            assertThat(format("%s is incorrect", venueDetails.getVenueId()), venueDetails.getUrl(), containsString("goo"));
        });
    }

    @Test
    public void venuesThatAreActiveTheirThreeDigitReferenceShouldBeUnique() {
        long maxSize = venueDataLoader.getVenueDetailsMap().values().stream().filter(v -> isYes(v.getActive())).count();
        long distinctSize = venueDataLoader.getVenueDetailsMap().values().stream().filter(v -> isYes(v.getActive())).map(v -> v.getThreeDigitReference()).distinct().count();

        long adjustForDuplicateSc238 = 2;

        assertThat(maxSize, is(distinctSize + adjustForDuplicateSc238));
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
        assertEquals("815997", result);
    }

    @Test
    public void testActiveVenueEpimsIdsMapByRpcReturnsVenues() {
        List<VenueDetails> result = venueDataLoader.getActiveVenueEpimsIdsMapByRpc().get("SSCS Leeds");
        assertFalse(result.isEmpty());
        assertEquals(20, result.size());
        result.forEach(vd -> assertTrue(venueDetailsByLeedsRpc.contains(vd.getEpimsId())));
    }

    @Test
    public void testActiveVenueEpimsIdsMapByRpcReturnsNullOnErroneousInput() {
        List<VenueDetails> result = venueDataLoader.getActiveVenueEpimsIdsMapByRpc().get("SSCS Newcastle");
        assertNull(result);
    }
}
