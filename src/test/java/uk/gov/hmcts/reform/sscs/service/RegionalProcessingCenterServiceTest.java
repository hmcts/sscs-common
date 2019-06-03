package uk.gov.hmcts.reform.sscs.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Map;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import uk.gov.hmcts.reform.sscs.ccd.domain.RegionalProcessingCenter;

@RunWith(JUnitParamsRunner.class)
public class RegionalProcessingCenterServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private AirLookupService airLookupService;

    private RegionalProcessingCenterService regionalProcessingCenterService;

    @Before
    public void setUp() {
        regionalProcessingCenterService = new RegionalProcessingCenterService(airLookupService);
    }

    @Test
    public void givenVenuesCvsFile_shouldLoadSccodeToRpcMap() {
        //When
        regionalProcessingCenterService.init();

        //Then
        Map<String, String> sccodeRegionalProcessingCentermap
            = regionalProcessingCenterService.getSccodeRegionalProcessingCentermap();
        assertEquals(245, sccodeRegionalProcessingCentermap.size());
        assertEquals("SSCS Birmingham", sccodeRegionalProcessingCentermap.get("SC038"));
        assertEquals("SSCS Leeds", sccodeRegionalProcessingCentermap.get("SC001"));
        assertEquals("SSCS Cardiff", sccodeRegionalProcessingCentermap.get("SC293"));
    }

    @Test
    @Parameters(method = "getDifferentRpcScenarios")
    public void givenRpcMetaData_shouldLoadRpcMetadataToMap(RegionalProcessingCenter expectedRpc, String rpcKey) {
        //When
        regionalProcessingCenterService.init();

        //Then
        Map<String, RegionalProcessingCenter> regionalProcessingCenterMap
            = regionalProcessingCenterService.getRegionalProcessingCenterMap();

        assertEquals(7, regionalProcessingCenterMap.size());
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
        regionalProcessingCenterService.init();

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
    }

    @Test
    public void shouldReturnBirminghamRegionalProcessingCenterAsDefault() {

        //Given
        String referenceNumber = "SC000/13/00010";
        regionalProcessingCenterService.init();

        //When
        RegionalProcessingCenter regionalProcessingCenter =
            regionalProcessingCenterService.getByScReferenceCode(referenceNumber);

        //Then
        assertBirminghamRpc(regionalProcessingCenter);

    }

    @Test
    public void shouldReturnBirminghamRpcIfTheScNumberIsNull() {
        //Given
        regionalProcessingCenterService.init();

        //When
        RegionalProcessingCenter regionalProcessingCenter =
            regionalProcessingCenterService.getByScReferenceCode(null);

        //Then
        assertBirminghamRpc(regionalProcessingCenter);
    }

    @Test
    public void shouldReturnBirminghamRpcIfTheScNumberIsEmpty() {
        //Given
        regionalProcessingCenterService.init();

        //When
        RegionalProcessingCenter regionalProcessingCenter =
            regionalProcessingCenterService.getByScReferenceCode("");

        //Then
        assertBirminghamRpc(regionalProcessingCenter);
    }

    @Test
    public void getRegionalProcessingCentreFromVenueId() {
        regionalProcessingCenterService.init();

        String leedsVenueId = "10";
        RegionalProcessingCenter rpc = regionalProcessingCenterService.getByVenueId(leedsVenueId);

        assertEquals("LEEDS", rpc.getName());
    }

    @Test
    public void getRegionalProcessingCentreFromPostcode() {
        regionalProcessingCenterService.init();

        String somePostcode = "AB1 1AB";
        when(airLookupService.lookupRegionalCentre(somePostcode)).thenReturn("Leeds");
        RegionalProcessingCenter rpc = regionalProcessingCenterService.getByPostcode(somePostcode);

        assertEquals("LEEDS", rpc.getName());
    }

    @Test
    public void givenAPostcode_thenRemoveLastThreeCharacters() {
        assertEquals("AB12", regionalProcessingCenterService.getFirstHalfOfPostcode("AB12 3TH"));
    }

    @Test
    public void givenAPostcodeWithLessThanThreeCharacters_thenReturnEmpty() {
        assertEquals("", regionalProcessingCenterService.getFirstHalfOfPostcode("AB"));

    }

    @Test
    public void givenANullPostcode_thenReturnEmpty() {
        assertEquals("", regionalProcessingCenterService.getFirstHalfOfPostcode(null));
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

    }
}