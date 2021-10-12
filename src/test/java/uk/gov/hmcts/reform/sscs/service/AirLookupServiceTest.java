package uk.gov.hmcts.reform.sscs.service;

import static java.util.Arrays.stream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static uk.gov.hmcts.reform.sscs.service.AirLookupService.DEFAULT_VENUE;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.converters.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitType;
import uk.gov.hmcts.reform.sscs.model.AirlookupBenefitToVenue;

@RunWith(JUnitParamsRunner.class)
public class AirLookupServiceTest {

    private static final AirLookupService airLookupService;

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
    public void checkAirPostcodeWithNoPipReturnsBirminghamWithBenefitType() {
        assertEquals(DEFAULT_VENUE.getEsaOrUcVenue(), airLookupService.lookupAirVenueNameByPostCode("n1w1 wal", BenefitType.builder().code("Esa").build()));
    }

    @Test
    public void lookupAirVenueNameByPostCodeReturnsNonNullValuesForAllBenefits() {
        stream(Benefit.values())
                .forEach(benefit -> assertNotNull(airLookupService.lookupAirVenueNameByPostCode("n1w1 wal", BenefitType.builder().code(benefit.getShortName()).build())));
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

    @Test
    @Parameters({
            "b4 1lal, Birmingham, PIP",
            "CV9 1ss, Nuneaton, PIP",
            "NN85 1ss, Northampton, DLA",
            "NN85 1ss, Northampton, carersAllowance",
            "NN85 1ss, Northampton, attendanceAllowance",
            "DE4 1SS, Chesterfield, industrialInjuriesDisablement",
            "b4 1lal, Birmingham, JSA",
            "CV9 1ss, Birmingham Civil Justice Centre, bereavementBenefit",
            "CV9 1ss, Birmingham Civil Justice Centre, maternityAllowance",
            "b4 1lal, Birmingham, ESA",
            "NN85 1ss, Northampton, UC",
            "NN85 1ss, Northampton, HB",
            "CV9 1ss, Birmingham Civil Justice Centre, bereavementSupportPaymentScheme",
            "CV9 1ss, Birmingham Civil Justice Centre, industrialDeathBenefit",
            "CV9 1ss, Birmingham Civil Justice Centre, pensionCredit",
            "CV9 1ss, Birmingham Civil Justice Centre, retirementPension",
            "CV8 1ss, Coventry (CMCB), childSupport",

    })
    public void checkVenueForPostCodeWithPipBenefitType(String postcode, String expectedPipVenue, String benefitTypeCode) {
        String pipVenue = airLookupService.lookupAirVenueNameByPostCode(postcode, BenefitType.builder().code(benefitTypeCode).build());
        assertEquals(expectedPipVenue, pipVenue);
    }

    private int lookupVenueId(String venue) {
        return airLookupService.getLookupVenueIdByAirVenueName().get(venue);
    }
}
