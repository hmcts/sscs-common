package uk.gov.hmcts.reform.sscs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.sscs.service.RegionalProcessingCenterService.*;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.sscs.ccd.domain.HearingRoute;
import uk.gov.hmcts.reform.sscs.ccd.domain.RegionalProcessingCenter;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class RegionalProcessingCenterServiceTest {

    @Mock
    AirLookupService mockedAirLookupService;

    private static final RegionalProcessingCenterService regionalProcessingCenterService;

    static {
        AirLookupService airLookupService = new AirLookupService();
        airLookupService.init();
        regionalProcessingCenterService = new RegionalProcessingCenterService(airLookupService);
        regionalProcessingCenterService.init();
    }

    @Test
    public void givenVenuesCvsFile_shouldLoadSccodeToRpcMap() {
        Map<String, String> sccodeRegionalProcessingCentermap
            = regionalProcessingCenterService.getSccodeRegionalProcessingCentermap();
        assertEquals(248, sccodeRegionalProcessingCentermap.size());
        assertEquals("SSCS Birmingham", sccodeRegionalProcessingCentermap.get("SC049"));
        assertEquals("SSCS Leeds", sccodeRegionalProcessingCentermap.get("SC001"));
        assertEquals("SSCS Cardiff", sccodeRegionalProcessingCentermap.get("SC293"));
    }

    @ParameterizedTest
    @MethodSource("getDifferentRpcScenarios")
    public void givenRpcMetaData_shouldLoadRpcMetadataToMap(RegionalProcessingCenter expectedRpc, String rpcKey) {
        //Then
        Map<String, RegionalProcessingCenter> regionalProcessingCenterMap
            = regionalProcessingCenterService.getRegionalProcessingCenterMap();

        int rpcCenters = 8;
        assertEquals(rpcCenters, regionalProcessingCenterMap.size());
        RegionalProcessingCenter actualRpc = regionalProcessingCenterMap.get(rpcKey);
        assertEquals(expectedRpc.getName(), actualRpc.getName());
        assertEquals(expectedRpc.getAddress1(), actualRpc.getAddress1());
        assertEquals(expectedRpc.getAddress2(), actualRpc.getAddress2());
        assertEquals(expectedRpc.getAddress3(), actualRpc.getAddress3());
        assertEquals(expectedRpc.getAddress4(), actualRpc.getAddress4());
        assertEquals(expectedRpc.getCity(), actualRpc.getCity());
        assertEquals(expectedRpc.getPostcode(), actualRpc.getPostcode());
        assertEquals(expectedRpc.getPhoneNumber(), actualRpc.getPhoneNumber());
        assertEquals(expectedRpc.getFaxNumber(), actualRpc.getFaxNumber());
        assertEquals(expectedRpc.getEmail(), actualRpc.getEmail());
    }

    @SuppressWarnings("unused")
    private Object[] getDifferentRpcScenarios() {
        RegionalProcessingCenter liverpoolRpc = RegionalProcessingCenter.builder()
            .name("LIVERPOOL")
            .address1("HM Courts & Tribunals Service")
            .address2("Social Security & Child Support Appeals")
            .address3("Prudential Buildings")
            .address4("36 Dale Street")
            .city("LIVERPOOL")
            .postcode("L2 5UZ")
            .phoneNumber("0300 123 1142")
            .faxNumber("0870 324 0109")
            .email("Liverpool_SYA_Resp@justice.gov.uk")
            .hearingRoute(HearingRoute.GAPS)
            .build();

        RegionalProcessingCenter cardiffRpc = RegionalProcessingCenter.builder()
            .name("CARDIFF")
            .address1("HM Courts & Tribunals Service")
            .address2("Social Security & Child Support Appeals")
            .address3("Eastgate House")
            .address4("Newport Road")
            .city("CARDIFF")
            .postcode("CF24 0AB")
            .phoneNumber("0300 123 1142")
            .faxNumber("0870 739 4438")
            .email("Cardiff_SYA_Respon@justice.gov.uk")
            .hearingRoute(HearingRoute.GAPS)
            .build();


        return new Object[]{
            new Object[]{liverpoolRpc, "SSCS Liverpool"},
            new Object[]{cardiffRpc, "SSCS Cardiff"}
        };
    }

    @Test
    public void shouldReturnRegionalProcessingCenterForGivenAppealReferenceNumber() {
        //Given
        String referenceNumber = "SC274/13/00010";

        //When
        RegionalProcessingCenter regionalProcessingCenter =
            regionalProcessingCenterService.getByScReferenceCode(referenceNumber);

        //Then
        assertEquals("LIVERPOOL", regionalProcessingCenter.getName());
        assertEquals("HM Courts & Tribunals Service", regionalProcessingCenter.getAddress1());
        assertEquals("Social Security & Child Support Appeals", regionalProcessingCenter.getAddress2());
        assertEquals("Prudential Buildings", regionalProcessingCenter.getAddress3());
        assertEquals("36 Dale Street", regionalProcessingCenter.getAddress4());
        assertEquals("LIVERPOOL", regionalProcessingCenter.getCity());
        assertEquals("L2 5UZ", regionalProcessingCenter.getPostcode());
        assertEquals("0300 123 1142", regionalProcessingCenter.getPhoneNumber());
        assertEquals("0870 324 0109", regionalProcessingCenter.getFaxNumber());
        assertEquals("Liverpool_SYA_Resp@justice.gov.uk", regionalProcessingCenter.getEmail());
        assertTrue("gaps".equalsIgnoreCase(regionalProcessingCenter.getHearingRoute().toString()));
    }

    @Test
    public void shouldReturnBirminghamRegionalProcessingCenterAsDefault() {

        //Given
        String referenceNumber = "SC000/13/00010";

        //When
        RegionalProcessingCenter regionalProcessingCenter =
            regionalProcessingCenterService.getByScReferenceCode(referenceNumber);

        //Then
        assertBirminghamRpc(regionalProcessingCenter);

    }

    @Test
    public void shouldReturnBirminghamRpcIfTheScNumberIsNull() {
        //When
        RegionalProcessingCenter regionalProcessingCenter =
            regionalProcessingCenterService.getByScReferenceCode(null);

        //Then
        assertBirminghamRpc(regionalProcessingCenter);
    }

    @Test
    public void shouldReturnBirminghamRpcIfTheScNumberIsEmpty() {
        //When
        RegionalProcessingCenter regionalProcessingCenter =
            regionalProcessingCenterService.getByScReferenceCode("");

        //Then
        assertBirminghamRpc(regionalProcessingCenter);
    }

    @Test
    public void getRegionalProcessingCentreFromVenueId() {
        String leedsVenueId = "10";
        RegionalProcessingCenter rpc = regionalProcessingCenterService.getByVenueId(leedsVenueId);

        assertEquals("LEEDS", rpc.getName());
    }

    @Test
    public void getRegionalProcessingCentreFromPostcode() {
        String somePostcode = "SP10 1AB";
        RegionalProcessingCenter rpc = regionalProcessingCenterService.getByPostcode(somePostcode, false);

        assertEquals("CARDIFF", rpc.getName());
    }

    @Test
    public void getIbcRegionalProcessingCentreFromPostcode() {
        String somePostcode = "SP10 1AB";
        RegionalProcessingCenter rpc = regionalProcessingCenterService.getByPostcode(somePostcode, true);

        assertEquals("CARDIFF", rpc.getName());
    }

    @Test
    public void getIbcNiRegionalProcessingCentreFromPostcode() {
        String somePostcode = "BT1 1AB";
        RegionalProcessingCenter rpc = regionalProcessingCenterService.getByPostcode(somePostcode, true);

        assertEquals("GLASGOW", rpc.getName());
    }

    @Test
    public void shouldNotGetNonIbcNiRegionalProcessingCentreFromPostcode() {
        String somePostcode = "BT1 1AB";
        RegionalProcessingCenter rpc = regionalProcessingCenterService.getByPostcode(somePostcode, false);

        assertNull(rpc);
    }

    @Test
    public void regionalProcessingCentreNameScotlandFromGBLocationCodeReturnGlasgowRpc() {
        RegionalProcessingCenterService regionalProcessingCenterServiceMockedAirLookup = new RegionalProcessingCenterService(mockedAirLookupService);
        regionalProcessingCenterServiceMockedAirLookup.init();
        when(mockedAirLookupService.lookupRegionalCentre(anyString())).thenReturn("Scotland");
        RegionalProcessingCenter rpc = regionalProcessingCenterServiceMockedAirLookup.getByPostcode("GB0120394", false);
        assertEquals("GLASGOW", rpc.getName());
    }

    @Test
    public void ibcRegionalProcessingCentreNameScotlandFromGBLocationCodeReturnGlasgowRpc() {
        RegionalProcessingCenterService regionalProcessingCenterServiceMockedAirLookup = new RegionalProcessingCenterService(mockedAirLookupService);
        regionalProcessingCenterServiceMockedAirLookup.init();
        when(mockedAirLookupService.lookupIbcRegionalCentre(anyString())).thenReturn("Scotland");
        RegionalProcessingCenter rpc = regionalProcessingCenterServiceMockedAirLookup.getByPostcode("GB0120394", true);
        assertEquals("GLASGOW", rpc.getName());
    }

    @Test
    public void givenAPostcode_thenRemoveLastThreeCharacters() {
        assertEquals("AB12", getFirstHalfOfPostcode("AB12 3TH"));
    }

    @Test
    public void givenAPostcodeWithLessThanThreeCharacters_thenReturnEmpty() {
        assertEquals("", getFirstHalfOfPostcode("AB"));
    }

    @Test
    public void givenANullPostcode_thenReturnEmpty() {
        assertEquals("", getFirstHalfOfPostcode(null));
    }

    private void assertBirminghamRpc(RegionalProcessingCenter regionalProcessingCenter) {
        assertEquals("BIRMINGHAM", regionalProcessingCenter.getName());
        assertEquals("HM Courts & Tribunals Service", regionalProcessingCenter.getAddress1());
        assertEquals("Social Security & Child Support Appeals", regionalProcessingCenter.getAddress2());
        assertEquals("Administrative Support Centre", regionalProcessingCenter.getAddress3());
        assertEquals("PO Box 14620", regionalProcessingCenter.getAddress4());
        assertEquals("BIRMINGHAM", regionalProcessingCenter.getCity());
        assertEquals("B16 6FR", regionalProcessingCenter.getPostcode());
        assertEquals("0300 123 1142", regionalProcessingCenter.getPhoneNumber());
        assertEquals("0126 434 7983", regionalProcessingCenter.getFaxNumber());
        assertEquals("Birmingham-SYA-Receipts@justice.gov.uk", regionalProcessingCenter.getEmail());
        assertEquals("GAPS", regionalProcessingCenter.getHearingRoute().toString().toUpperCase());
    }
}
