package uk.gov.hmcts.reform.sscs.service;

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
    @Parameters({"1158", "1144", "1145", "1027", "1028", "1012"})
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

}
