package uk.gov.hmcts.reform.sscs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import uk.gov.hmcts.reform.sscs.model.AirlookupBenefitToVenue;

public class AirLookupServiceTest {
    AirLookupService airLookupService;

    private static AirlookupBenefitToVenue DEFAULT_VENUE = AirlookupBenefitToVenue.builder().pipVenue("Birmingham").esaVenue("Birmingham").build();

    @Before
    public void setUp() {
        airLookupService = new AirLookupService();
        airLookupService.init();
    }

    @Test
    public void lookupPostcode() {
        String adminGroup = airLookupService.lookupRegionalCentre("BR3 8JK");
        assertEquals("Sutton", adminGroup);
    }

    @Test
    public void lookupPostcodeLowerCase() {
        String adminGroup = airLookupService.lookupRegionalCentre("br3 8JK");
        assertEquals("Sutton", adminGroup);
    }

    @Test
    public void lookupPostcodeNotThere() {
        String adminGroup = airLookupService.lookupRegionalCentre("aa1 1aa");
        assertEquals(null, adminGroup);
    }

    @Test
    public void lookupLastValue() {
        String adminGroup = airLookupService.lookupRegionalCentre("ze3 4gh");
        assertEquals("Glasgow", adminGroup);
    }

    @Test
    public void lookupFirstValue() {
        String adminGroup = airLookupService.lookupRegionalCentre("ab1 2gh");
        assertEquals("Glasgow", adminGroup);
    }

    @Test
    public void lookupShortPostcode() {
        String adminGroup = airLookupService.lookupRegionalCentre("l2 1RT");
        assertEquals("Liverpool", adminGroup);
    }

    @Test
    public void lookupLongPostcode() {
        String adminGroup = airLookupService.lookupRegionalCentre("HP27 1RT");
        assertEquals("Birmingham", adminGroup);
    }

    @Test
    public void lookupShortPostcodeNoSpace() {
        String adminGroup = airLookupService.lookupRegionalCentre("l21RT");
        assertEquals("Liverpool", adminGroup);
    }


    @Test
    public void lookupLongPostcodeNoSpace() {
        String adminGroup = airLookupService.lookupRegionalCentre("HP271RT");
        assertEquals("Birmingham", adminGroup);
    }

    @Test
    public void lookupLongPostcodeOutcode() {
        String adminGroup = airLookupService.lookupRegionalCentre("HP27");
        assertEquals("Birmingham", adminGroup);
    }

    @Test
    public void lookupPostcodePostcodesThatWereMissing() {
        String adminGroup = airLookupService.lookupRegionalCentre("bl11");
        assertEquals("Liverpool", adminGroup);

        adminGroup = airLookupService.lookupRegionalCentre("bl78");
        assertEquals("Liverpool", adminGroup);

        adminGroup = airLookupService.lookupRegionalCentre("s31");
        assertEquals("Leeds", adminGroup);

        adminGroup = airLookupService.lookupRegionalCentre("s30");
        assertEquals("Leeds", adminGroup);
    }

    @Test
    public void checkAirPostcodeWithNoPipReturnsBirmingham() {
        //n1w1 is a sorting office
        assertEquals(DEFAULT_VENUE, airLookupService.lookupAirVenueNameByPostCode("n1w1"));
    }

    @Test
    public void checkVenueIdForPostCodeWithNoPip() {
        AirlookupBenefitToVenue venues = airLookupService.lookupAirVenueNameByPostCode("b4");

        assertEquals(1234, lookupVenueId(venues.getPipVenue()));
    }

    @Test
    public void checkVenueIdForValidPostCode() {
        AirlookupBenefitToVenue venueName = airLookupService.lookupAirVenueNameByPostCode("NN85");

        assertEquals(1223, lookupVenueId(venueName.getPipVenue()));
    }

    private int lookupVenueId(String venue) {
        return airLookupService.getLookupVenueIdByAirVenueName().get(venue);
    }
}
