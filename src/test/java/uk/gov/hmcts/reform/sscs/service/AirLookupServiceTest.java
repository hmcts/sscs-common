package uk.gov.hmcts.reform.sscs.service;

import static org.junit.Assert.assertEquals;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.converters.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.gov.hmcts.reform.sscs.model.AirlookupBenefitToVenue;

@RunWith(JUnitParamsRunner.class)
public class AirLookupServiceTest {

    private static AirlookupBenefitToVenue DEFAULT_VENUE = AirlookupBenefitToVenue.builder()
        .pipVenue("Birmingham")
        .esaOrUcVenue("Birmingham")
        .build();

    private static AirLookupService airLookupService;

    static {
        airLookupService = new AirLookupService();
        airLookupService.init();
    }

    @Test
    @Parameters({
        "BR3 8JK, Sutton",
        "br3 8JK, Sutton",
        "aa1 1aa, null",
        "ze3 4gh, Glasgow",
        "ab1 2gh, Glasgow",
        "l2 1RT, Liverpool",
        "HP27 1RT, Bradford",
        "l21RT, Liverpool",
        "HP271RT, Bradford",
        "HP27, Bradford",
        "bl11, Liverpool",
        "bl78, Liverpool",
        "s31, Leeds",
        "s30, Leeds",
        "br2, Sutton"
    })
    public void lookupPostcode(String postcode, @Nullable String expectedAdminGroup) {
        assertEquals(expectedAdminGroup, airLookupService.lookupRegionalCentre(postcode));
    }

    @Test
    public void checkAirPostcodeWithNoPipReturnsBirmingham() {
        //n1w1 is a sorting office
        assertEquals(DEFAULT_VENUE, airLookupService.lookupAirVenueNameByPostCode("n1w1"));
    }

    @Test
    @Parameters({
        "b4, 1234",
        "NN85, 1223",
    })
    public void checkVenueIdForPostCodeWithNoPip(String postcode, int expectedPipVenue) {
        AirlookupBenefitToVenue venues = airLookupService.lookupAirVenueNameByPostCode(postcode);

        assertEquals(expectedPipVenue, lookupVenueId(venues.getPipVenue()));
    }

    private int lookupVenueId(String venue) {
        return airLookupService.getLookupVenueIdByAirVenueName().get(venue);
    }
}
