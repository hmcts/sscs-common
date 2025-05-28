package uk.gov.hmcts.reform.sscs.service;

import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static uk.gov.hmcts.reform.sscs.service.AirLookupService.DEFAULT_VENUE;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitType;
import uk.gov.hmcts.reform.sscs.model.AirlookupBenefitToVenue;

public class AirLookupServiceTest {

    private AirLookupService airLookupService;

    @BeforeEach
    void setUp() throws Exception {
        airLookupService = new AirLookupService();
        airLookupService.init();
    }

    @ParameterizedTest
    @CsvSource({
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
            "bl1, Liverpool",
            "bl7, Liverpool",
            "s3, Leeds",
            "s35, Leeds",
            "br2, Sutton"
    })
    public void lookupPostcode(String postcode, String expectedAdminGroup) {
        if ("null".equals(expectedAdminGroup)) {
            expectedAdminGroup = null;
        }
        assertEquals(expectedAdminGroup, airLookupService.lookupRegionalCentre(postcode));
    }

    @ParameterizedTest
    @CsvSource({
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
            "bl1, Liverpool",
            "bl7, Liverpool",
            "s3, Leeds",
            "s35, Leeds",
            "br2, Sutton"
    })
    public void lookupIbcPostcode(String postcode, String expectedAdminGroup) {
        if ("null".equals(expectedAdminGroup)) {
            expectedAdminGroup = null;
        }
        assertEquals(expectedAdminGroup, airLookupService.lookupIbcRegionalCentre(postcode));
    }

    @ParameterizedTest
    @CsvSource({
            "BT3 9EP, Glasgow",
            "BT1 3WH, Glasgow"
    })
    public void lookupIbcNIPostcode(String postcode, String expectedAdminGroup) throws Exception {
        assertEquals("null".equals(expectedAdminGroup) ? null : expectedAdminGroup, airLookupService.lookupIbcRegionalCentre(postcode));
    }

    @ParameterizedTest
    @CsvSource({
            "Truro Hearing Centre, 1155",
            "Worle, 1159",
            "Llanelli, 1186",
            "Derby, 1216",
            "Belfast RCJ, 1270"
    })
    public void getLookupVenueIdByAirVenueNameReturnsExpected(String processingVenue, String expVenueId) throws Exception {
        org.springframework.core.io.ClassPathResource classPathResource = new org.springframework.core.io.ClassPathResource("reference-data/AIRLookup_23.2.xlsx");
        try (org.apache.poi.ss.usermodel.Workbook wb2 = org.apache.poi.ss.usermodel.WorkbookFactory.create(classPathResource.getInputStream())) {
            airLookupService.parseAirLookupData(wb2);
            org.apache.poi.ss.usermodel.Sheet sheet = wb2.getSheet(AirLookupService.AIR_LOOKUP_VENUE_IDS_CSV);

            if (sheet == null) {
                throw new IllegalStateException("Sheet with name '" + AirLookupService.AIR_LOOKUP_VENUE_IDS_CSV + "' not found in the workbook.");
            }
        }
        String venueId = String.valueOf(airLookupService.getLookupVenueIdByAirVenueName().get(processingVenue));
        assertNotNull(venueId, "Venue ID should not be null");
        assertEquals(expVenueId, venueId);
    }

    @ParameterizedTest
    @CsvSource({
            "GBATGLO00, Cardiff",
            "GBATKEM00, Cardiff",
            "GBATLAS00, Cardiff",
            "GBATOAK00, Bradford",
            "GBATDSA00, Leeds",
            "GBATSYW00, Birmingham",
            "GBATSTY00, Glasgow",
            " GBATWAR00, Liverpool",
            "GBATWIK00 , Glasgow",
            "GbATYVL00, Cardiff",
            "gBATBZN00, Bradford",
            "gbATFFD00, Cardiff",
            "gbatflT00, Bradford"
    })
    public void lookupPortOfEntryCode(String portOfEntryCode, String expectedAdminGroup) {
        assertEquals(expectedAdminGroup, airLookupService.lookupRegionalCentre(portOfEntryCode));
    }

    @ParameterizedTest
    @CsvSource({
            "GBATGLO00, Cardiff",
            "GBATKEM00, Cardiff",
            "GBATLAS00, Cardiff",
            "GBATOAK00, Bradford",
            "GBATDSA00, Leeds",
            "GBATSYW00, Birmingham",
            "GBATSTY00, Glasgow",
            " GBATWAR00, Liverpool",
            "GBATWIK00 , Glasgow",
            "GbATYVL00, Cardiff",
            "gBATBZN00, Bradford",
            "gbATFFD00, Cardiff",
            "gbatflT00, Bradford"
    })
    public void lookupPortOfEntryIbcCode(String portOfEntryCode, String expectedAdminGroup) {
        assertEquals(expectedAdminGroup, airLookupService.lookupIbcRegionalCentre(portOfEntryCode));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "GBSTTRT00,Northampton,Birmingham",
            "GBSTBLY00,Bedlington,Leeds",
            "GBSTWAT00,Barnstaple,Cardiff",
            "GBSTAVO00,Bristol Magistrates,Cardiff",
            "GBSTLON00,Southend,Bradford",
            "GBSTBAR00,Barrow,Liverpool",
            "GBSTIMM00,Grimsby,Leeds",
            "GBSTCDF00,Cardiff,Cardiff",
            "GBSTIMM01,Hull,Leeds",
            "GBSTTRT01,Chesterfield,Birmingham",
            "GBSTBLY01,Newcastle,Leeds",
            "GBSTWAT01,Barnstaple,Cardiff",
            "GBSTLIV00,Birkenhead,Liverpool",
            "GBSTBLY02,Bedlington,Leeds",
            "GBSTBOS00,Boston,Birmingham",
            "GBSTWAT02,Taunton,Cardiff",
            "GBSTHUL00,Scarborough,Leeds",
            "GBSTWEY00,Dorchester,Cardiff",
            "GBSTCOL00,Ipswich,Bradford",
            "GBSTPTB00,Port Talbot CJC,Cardiff",
            "GBSTTNM00,Newton Abbot,Cardiff",
            "GBSTCOL01,Chelmsford,Bradford",
            "GBSTTRT02,Doncaster,Leeds",
            "GBSTLLA00,Caernarfon,Cardiff",
            "GBSTCDF01,Cardiff,Cardiff",
            "GBSTFOY00,Truro Hearing Centre,Cardiff",
            "GBSTNPT00,Langstone\\, Newport,Cardiff",
            "GBSTCOL02,Chelmsford,Bradford",
            "GBSTCOW00,Newport,Cardiff",
            "GBSTTNM01,Newton Abbot,Cardiff",
            "GBSTDOV00,Ashford,Bradford",
            "GBSTELL00,Chester,Liverpool",
            "GBSTEXE00,Exeter,Cardiff",
            "GBSTEXE01,Exeter,Cardiff",
            "GBSTFAL00,Truro Hearing Centre,Cardiff",
            "GBSTWTS00,Ashford,Bradford",
            "GBSTFXT00,Ipswich,Bradford",
            "GBSTCOL03,Chelmsford,Bradford",
            "GBSTFIS00,Haverfordwest,Cardiff",
            "GBSTFLE00,Blackpool,Liverpool",
            "GBSTTRT03,Doncaster,Leeds",
            "GBSTFOY01,Truro Hearing Centre,Cardiff",
            "GBSTTRT04,Lincoln,Birmingham",
            "GBSTLIV01,Liverpool,Liverpool",
            "GBSTHEY00,Lancaster,Liverpool",
            "GBSTSSS00,Gloucester,Cardiff",
            "GBSTGOO00,Doncaster,Leeds",
            "GBSTGTY00,Norwich,Bradford",
            "GBSTGRI00,Grimsby,Leeds",
            "GBSTTRT05,Doncaster,Leeds",
            "GBSTTRT06,Doncaster,Leeds",
            "GBSTHTP00,Teesside JC,Leeds",
            "GBSTHRH00,Ipswich,Bradford",
            "GBSTHEY01,Lancaster,Liverpool",
            "GBSTLON01,Basildon CC,Bradford",
            "GBSTHLD00,Caernarfon,Cardiff",
            "GBSTGOO01,Doncaster,Leeds",
            "GBSTHUL01,Hull,Leeds",
            "GBSTIMM02,Grimsby,Leeds",
            "GBSTIPS00,Ipswich,Bradford",
            "GBSTTRT07,Doncaster,Leeds",
            "GBSTKLN00,Kings Lynn Mags,Bradford",
            "GBSTHEY02,Lancaster,Liverpool",
            "GBSTLON02,Basildon CC,Bradford",
            "GBSTSHO00,Brighton,Bradford",
            "GBSTLIV02,Liverpool,Liverpool",
            "GBSTLLA01,Prestatyn,Cardiff",
            "GBSTSWA00,Llanelli,Cardiff",
            "GBSTLON03,Fox Court (S),Sutton",
            "GBSTLGP00,Basildon CC,Bradford",
            "GBSTLOW00,Norwich,Bradford",
            "GBSTSTN00,Southampton Magistrates Court,Cardiff",
            "GBSTCOL04,Chelmsford,Bradford",
            "GBSTMNC00,Manchester,Liverpool",
            "GBSTMED00,Chatham,Bradford",
            "GBSTMID00,Teesside JC,Leeds",
            "GBSTMIL00,Aldershot,Cardiff",
            "GBSTBAR01,Barrow,Liverpool",
            "GBSTHRH01,Ipswich,Bradford",
            "GBSTMOS00,Prestatyn,Cardiff",
            "GBSTTRT08,Doncaster,Leeds",
            "GBSTPTB01,Port Talbot CJC,Cardiff",
            "GBSTTYN00,Newcastle,Leeds",
            "GBSTNHV00,Brighton,Bradford",
            "GBSTIMM03,Grimsby,Leeds",
            "GBSTPEN00,Truro Hearing Centre,Cardiff",
            "GBSTNPT01,Langstone\\, Newport,Cardiff",
            "GBSTTYN01,North Shields,Leeds",
            "GBSTNTH00,Chester,Liverpool",
            "GBSTNOR00,Norwich,Bradford",
            "GBSTPLY00,Truro Hearing Centre,Cardiff",
            "GBSTPAR00,Truro Hearing Centre,Cardiff",
            "GBSTHRH02,Ipswich,Bradford",
            "GBSTMNC01,Manchester,Liverpool",
            "GBSTMIL01,Haverfordwest,Cardiff",
            "GBSTMIL02,Haverfordwest,Cardiff",
            "GBSTMIL03,Haverfordwest,Cardiff",
            "GBSTFAL01,Truro Hearing Centre,Cardiff",
            "GBSTPEN01,Truro Hearing Centre,Cardiff",
            "GBSTPLY01,Plymouth,Cardiff",
            "GBSTPOO00,Poole,Cardiff",
            "GBSTLLA02,Caernarfon,Cardiff",
            "GBSTAVO01,Bristol Magistrates,Cardiff",
            "GBSTPTL00,Dorchester,Cardiff",
            "GBSTPTM00,Havant,Cardiff",
            "GBSTPTB02,Port Talbot CJC,Cardiff",
            "GBSTMED01,Chatham,Bradford",
            "GBSTRMG00,Margate,Bradford",
            "GBSTMOS01,Prestatyn,Cardiff",
            "GBSTMED02,Chatham,Bradford",
            "GBSTMED03,Chatham,Bradford",
            "GBSTLON04,Southend,Bradford",
            "GBSTCOL05,Chelmsford,Bradford",
            "GBSTAVO02,Bristol Magistrates,Cardiff",
            "GBSTRUN00,St.Helens,Liverpool",
            "GBSTFOL00,Ashford,Bradford",
            "GBSTSCA00,Scarborough,Leeds",
            "GBSTTRT10,Doncaster,Leeds",
            "GBSTSUN00,Sunderland A,Leeds",
            "GBSTGOO02,York,Leeds",
            "GBSTSSS01,Bristol Magistrates,Cardiff",
            "GBSTMED04,Chatham,Bradford",
            "GBSTSHO01,Brighton,Bradford",
            "GBSTMOS02,Wrexham,Cardiff",
            "GBSTSIL00,Carlisle,Liverpool",
            "GBSTSTN01,Southampton Magistrates Court,Cardiff",
            "GBSTLON05,Southend,Bradford",
            "GBSTTYN02,South Shields,Leeds",
            "GBSTSUN01,Sunderland A,Leeds",
            "GBSTSWA01,Port Talbot CJC,Cardiff",
            "GBSTMID01,Teesside JC,Leeds",
            "GBSTTNM02,Newton Abbot,Cardiff",
            "GBSTLON06,Fox Court (S),Sutton",
            "GBSTTEP00,East London (S),Sutton",
            "GBSTTHP00,Chatham,Bradford",
            "GBSTEXE02,Exeter,Cardiff",
            "GBSTTNM03,Newton Abbot,Cardiff",
            "GBSTTRT11,Stoke,Birmingham",
            "GBSTFAL02,Truro Hearing Centre,Cardiff",
            "GBSTTYN03,Newcastle,Leeds",
            "GBSTRUN01,St.Helens,Liverpool",
            "GBSTWAT03,Taunton,Cardiff",
            "GBSTNOR01,Bristol Magistrates,Cardiff",
            "GBSTWEY01,Dorchester,Cardiff",
            "GBSTWTB00,Scarborough,Leeds",
            "GBSTWHV00,Workington,Liverpool",
            "GBSTWTS01,Margate,Bradford",
            "GBSTWIS00,Kings Lynn Mags,Bradford",
            "GBSTCOL06,Chelmsford,Bradford",
            "GBSTWOR00,Workington,Liverpool",
            "GBSTABD00,Aberdeen,Glasgow",
            "GBSTGRK00,Ayr,Glasgow",
            "GBSTDUN00,Dundee,Glasgow",
            "GBSTARD00,Ayr,Glasgow",
            "GBSTAYR00,Ayr,Glasgow",
            "GBSTGRK01,Oban,Glasgow",
            "GBSTGRG00,Kirkcaldy,Glasgow",
            "GBSTGLW00,Glasgow,Glasgow",
            "GBSTFRB00,Inverness,Glasgow",
            "GBSTINV00,Inverness,Glasgow",
            "GBSTGRG01,Kirkcaldy,Glasgow",
            "GBSTGRK02,Glasgow,Glasgow",
            "GBSTINV01,Inverness,Glasgow",
            "GBSTDUN01,Dundee,Glasgow",
            "GBSTGRK03,Glasgow,Glasgow",
            "GBSTGRK04,Glasgow,Glasgow",
            "GBSTFRB01,Aberdeen,Glasgow",
            "GBSTGRK05,Glasgow,Glasgow",
            "GBSTGRK06,Ayr,Glasgow",
            "GBSTAYR01,Ayr,Glasgow",
            "GBSTGLW01,Glasgow,Glasgow",
            "GBSTGRG02,Stirling,Glasgow",
            "GBSTGRK07,Greenock,Glasgow",
            "GBSTHPT00,Dunfermline,Glasgow",
            "GBSTGRK08,Ayr,Glasgow",
            "GBSTIVG00,Inverness,Glasgow",
            "GBSTGRG03,Dunfermline,Glasgow",
            "GBSTINV02,Inverness,Glasgow",
            "GBSTIRV00,Ayr,Glasgow",
            "GBSTGRK09,Glasgow,Glasgow",
            "GBSTKKD00,Kirkcaldy,Glasgow",
            "GBSTKWL00,Inverness,Glasgow",
            "GBSTLEI00,Edinburgh,Glasgow",
            "GBSTLER00,Inverness,Glasgow",
            "GBSTINV03,Inverness,Glasgow",
            "GBSTINV04,Inverness,Glasgow",
            "GBSTFRB02,Aberdeen,Glasgow",
            "GBSTGRG04,Kirkcaldy,Glasgow",
            "GBSTMON00,Dundee,Glasgow",
            "GBSTGRK10,Oban,Glasgow",
            "GBSTGRK11,Ayr,Glasgow",
            "GBSTDUN02,Dundee,Glasgow",
            "GBSTPHD00,Aberdeen,Glasgow",
            "GBSTROS00,Dunfermline,Glasgow",
            "GBSTLER01,Inverness,Glasgow",
            "GBSTINV05,Inverness,Glasgow",
            "GBSTSTO00,Lewis,Glasgow",
            "GBSTGRK12,Ayr,Glasgow",
            "GBSTKWL01,Inverness,Glasgow",
            "GBSTDUN03,Dundee,Glasgow",
            "GBSTINV06,Inverness,Glasgow",
            "GBSTINV07,Inverness,Glasgow",
            "GBATABZ00,Aberdeen,Glasgow",
            "GBATBQH00,Sutton,Sutton",
            "GBATBHX00,Birmingham CJC,Birmingham",
            "GBATBLK00,Blackpool,Liverpool",
            "GBATBOH00,Poole,Cardiff",
            "GBATBRS00,Bristol Magistrates,Cardiff",
            "GBATCBG00,Cambridge,Bradford",
            "GBATCWL00,Cardiff,Cardiff",
            "GBATCVT00,Coventry (CMCB),Birmingham",
            "GBATMME00,Darlington,Leeds",
            "GBATEMA00,Derby,Birmingham",
            "GBATEDI00,Edinburgh,Glasgow",
            "GBATEXT00,Exeter,Cardiff",
            "GBATFAB00,Aldershot,Cardiff",
            "GBATFZO00,Bristol Magistrates,Cardiff",
            "GBATGLA00,Glasgow,Glasgow",
            "GBATHUY00,Grimsby,Leeds",
            "GBATLBA00,Leeds,Leeds",
            "GBATLPL00,Leeds,Leeds",
            "GBATLYD00,Ashford,Bradford",
            "GBATLCY00,East London (S),Sutton",
            "GBATLGW00,Sutton,Sutton",
            "GBATLHR00,Hatton Cross,Sutton",
            "GBATLTN00,Luton,Bradford",
            "GBATLSA00,Chelmsford,Bradford",
            "GBATMAN00,Manchester,Liverpool",
            "GBATMSE00,Margate,Bradford",
            "GBATNCL00,Newcastle,Leeds",
            "GBATNQY00,Truro Hearing Centre,Cardiff",
            "GBATNWI00,Norwich,Bradford",
            "GBATPLH00,Plymouth,Cardiff",
            "GBATPIK00,Ayr,Glasgow",
            "GBATESH00,Brighton,Bradford",
            "GBATSOU00,Southampton Magistrates Court,Cardiff",
            "GBATSEN00,Southend,Bradford",
            "GBATLSI00,Inverness,Glasgow",
            "GBATBCC00,Norwich,Bradford",
            "GBATGLO00,Gloucester,Cardiff",
            "GBATKEM00,Gloucester,Cardiff",
            "GBATLAS00,Aldershot,Cardiff",
            "GBATOAK00,Oxford,Bradford",
            "GBATDSA00,Doncaster,Leeds",
            "GBATSYW00,Northampton,Birmingham",
            "GBATSTY00,Lewis,Glasgow",
            "GBATWAR00,Blackpool,Liverpool",
            "GBATWIK00,Inverness,Glasgow",
            "GBATYVL00,Taunton,Cardiff",
            "GBATBZN00,Oxford,Bradford",
            "GBATFFD00,Gloucester,Cardiff",
            "GBATFLT00,Norwich,Bradford",
            "GBATLKH00,Norwich,Bradford",
            "GBATLYH00,Swindon,Cardiff",
            "GBATMWH00,Leeds,Leeds",
            "GBATMDH00,Cambridge,Bradford",
            "GBATNHT00,Watford,Bradford",
            "GBATWAD00,Lincoln,Birmingham",
            "GBRTASD00,Ashford,Bradford",
            "GBRTLBK00,East London (S),Sutton",
            "GBRTDAG00,Romford,Sutton",
            "GBRTDVY00,Northampton,Birmingham",
            "GBRTNGO00,Ashford,Bradford",
            "GBRTDON00,Doncaster,Leeds",
            "GBRTWDN00,St.Helens,Liverpool",
            "GBUTDEU00,Ashford,Bradford",
            "GBUTEUT00,Ashford,Bradford",
            "GBUTKIL00,Grimsby,Leeds",
            "GBUTPUF00,Romford,Sutton"
    })
    public void lookupRpcAndVenueFromPortOfEntryCode(String row) {
        row = row.replace("\\,", "&comma;");
        String[] parts = row.split(",");
        String portOfEntryCode = parts[0].trim().replace("&comma;", ",");
        String expectedVenue = parts[1].trim().replace("&comma;", ",");
        String expectedRpc = parts[2].trim().replace("&comma;", ",");

        assertEquals(expectedVenue, airLookupService.lookupAirVenueNameByPostCode(portOfEntryCode, BenefitType.builder().code("infectedBloodCompensation").build()));
        assertEquals(expectedRpc, airLookupService.lookupRegionalCentre(portOfEntryCode));
    }

    @Test
    public void checkAirPostcodeWithNoPipReturnsBirmingham() {
        //n1w1 is a sorting office
        assertEquals(DEFAULT_VENUE, airLookupService.lookupAirVenueNameByPostCode("n1w1"));
    }

    @ParameterizedTest
    @CsvSource({
            "BT3 9EP",
            "BT1 3WH",
            "bt12 7sl"
    })
    public void checkLookupAirVenueNameByPostCodeReturnsBelfastforNIPostcodesWhenIBC(String postcode) {
        assertEquals("Belfast RCJ", airLookupService.lookupAirVenueNameByPostCode(postcode, BenefitType.builder().code("infectedBloodCompensation").build()));
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
    public void lookupAirVenueNameByPortOfEntryCodeReturnsNonNullValuesForAllBenefits() {
        stream(Benefit.values())
                .forEach(benefit -> assertNotNull(airLookupService.lookupAirVenueNameByPostCode("GB000150", BenefitType.builder().code(benefit.getShortName()).build())));
    }

    @Test
    public void lookupAirVenueIdByVenueNameReturnsCorrectVenueId() {
        assertEquals(Optional.of(1127), Optional.ofNullable(airLookupService.lookupVenueIdByAirVenueName("Worcester Mags")));
    }

    @ParameterizedTest
    @CsvSource({
            "b4, 1234",
            "NN85, 1223",
    })
    public void checkVenueIdForPostCodeWithNoPip(String postcode, int expectedPipVenue) {
        AirlookupBenefitToVenue venues = airLookupService.lookupAirVenueNameByPostCode(postcode);

        assertEquals(expectedPipVenue, lookupVenueId(venues.getPipVenue()));
    }

    @ParameterizedTest
    @CsvSource({
            "b4 1lal, Birmingham, PIP",
            "CV9 1ss, Nuneaton, PIP",
            "NN85 1ss, Northampton, DLA",
            "NN85 1ss, Northampton, carersAllowance",
            "NN85 1ss, Northampton, attendanceAllowance",
            "DE4 1SS, Chesterfield, industrialInjuriesDisablement",
            "b4 1lal, Birmingham, JSA",
            "CV9 1ss, Birmingham CJC, bereavementBenefit",
            "CV9 1ss, Birmingham CJC, maternityAllowance",
            "b4 1lal, Birmingham, ESA",
            "NN85 1ss, Northampton, UC",
            "NN85 1ss, Northampton, HB",
            "CV9 1ss, Birmingham CJC, bereavementSupportPaymentScheme",
            "CV9 1ss, Birmingham CJC, industrialDeathBenefit",
            "CV9 1ss, Birmingham CJC, pensionCredit",
            "CV9 1ss, Birmingham CJC, retirementPension",
            "CV8 1ss, Coventry (CMCB), childSupport",
            "CV9 1ss, Birmingham CJC, taxCredit",
            "CV9 1ss, Birmingham CJC, guardiansAllowance",
            "CV9 1ss, Birmingham CJC, taxFreeChildcare",
            "CV9 1ss, Birmingham CJC, homeResponsibilitiesProtection",
            "CV9 1ss, Birmingham CJC, childBenefit",
            "CV9 1ss, Birmingham CJC, thirtyHoursFreeChildcare",
            "CV9 1ss, Birmingham CJC, guaranteedMinimumPension",
            "CV9 1ss, Birmingham CJC, nationalInsuranceCredits"
    })
    public void checkVenueForPostCodeWithPipBenefitType(String postcode, String expectedPipVenue, String benefitTypeCode) {
        String pipVenue = airLookupService.lookupAirVenueNameByPostCode(postcode, BenefitType.builder().code(benefitTypeCode).build());
        assertEquals(expectedPipVenue, pipVenue);
    }

    private int lookupVenueId(String venue) {
        return airLookupService.getLookupVenueIdByAirVenueName().get(venue);
    }
}
