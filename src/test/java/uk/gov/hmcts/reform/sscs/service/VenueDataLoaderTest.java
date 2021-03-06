package uk.gov.hmcts.reform.sscs.service;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class VenueDataLoaderTest {

    private static final VenueDataLoader venueDataLoader = new VenueDataLoader();

    static {
        venueDataLoader.init();
    }

    @Test
    @Parameters({"1158", "1144", "1145", "1027", "1028", "1012", "1", "3", "8", "11", "51", "75", "105", "178", "186",
            "228", "268", "1035", "1037", "1045", "1046", "1048", "1052", "1133", "1134", "1142",
            "1160", "1183", "1196", "1203", "1239"})
    public void venuesShouldNotBeActive(String id) {
        assertThat(venueDataLoader.getVenueDetailsMap().get(id).getActive(), is("No"));
    }

    @Test
    @Parameters({"63", "94", "97", "106", "115", "1043"})
    public void venuesShouldBeActiveAndHaveAGoogleLink(String id) {
        assertThat(venueDataLoader.getVenueDetailsMap().get(id).getActive(), is("Yes"));
        assertThat(venueDataLoader.getVenueDetailsMap().get(id).getUrl(), containsString("https://"));
        assertThat(venueDataLoader.getVenueDetailsMap().get(id).getUrl(), containsString("goo"));
    }

    @Test
    public void venuesShouldEitherBeActiveOrNotActive() {
        venueDataLoader.getVenueDetailsMap().values().forEach(venueDetails -> {
            assertThat(format("%s is incorrect", venueDetails.getVenueId()), venueDetails.getActive(), containsString(venueDetails.getComments().isEmpty() ? "Yes" : "No"));
        });
    }

    @Test
    public void venuesActiveShouldHaveAGoogleLink() {
        venueDataLoader.getVenueDetailsMap().values().stream().filter(venueDetails -> venueDetails.getActive().equals("Yes")).forEach(venueDetails -> {
            assertThat(format("%s is incorrect", venueDetails.getVenueId()), venueDetails.getUrl(), containsString("https://"));
            assertThat(format("%s is incorrect", venueDetails.getVenueId()), venueDetails.getUrl(), containsString("goo"));
        });
    }

    @Test
    public void venuesThatAreActiveTheirThreeDigitReferenceShouldBeUnique() {
        long maxSize = venueDataLoader.getVenueDetailsMap().values().stream().filter(v -> v.getActive().equals("Yes")).count();
        long distinctSize = venueDataLoader.getVenueDetailsMap().values().stream().filter(v -> v.getActive().equals("Yes")).map(v -> v.getThreeDigitReference()).distinct().count();

        long adjustForDuplicateSc238 = 1;

        assertThat(maxSize, is(distinctSize + adjustForDuplicateSc238));
    }
}
