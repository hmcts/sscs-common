package uk.gov.hmcts.reform.sscs.service;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.RegionalProcessingCenter;

public class RegionalProcessingCenterServiceTest {

    public static final String SSCS_LIVERPOOL = "SSCS Liverpool";

    private RegionalProcessingCenterService regionalProcessingCenterService;

    @Before
    public void setUp() throws Exception {
        regionalProcessingCenterService = new RegionalProcessingCenterService();
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
    public void givenRpcMetaData_shouldLoadRpcMetadataToMap() {
        //When
        regionalProcessingCenterService.init();

        //Then
        Map<String, RegionalProcessingCenter> regionalProcessingCenterMap
                = regionalProcessingCenterService.getRegionalProcessingCenterMap();

        assertEquals(6, regionalProcessingCenterMap.size());
        RegionalProcessingCenter regionalProcessingCenter = regionalProcessingCenterMap.get(SSCS_LIVERPOOL);
        assertEquals("LIVERPOOL", regionalProcessingCenter.getName());
        assertEquals("HM Courts & Tribunals Service", regionalProcessingCenter.getAddress1());
        assertEquals("Social Security & Child Support Appeals", regionalProcessingCenter.getAddress2());
        assertEquals("Prudential Buildings", regionalProcessingCenter.getAddress3());
        assertEquals("36 Dale Street", regionalProcessingCenter.getAddress4());
        assertEquals("LIVERPOOL", regionalProcessingCenter.getCity());
        assertEquals("L2 5UZ", regionalProcessingCenter.getPostcode());
        assertEquals("0300 123 1142", regionalProcessingCenter.getPhoneNumber());
        assertEquals("0870 324 0109", regionalProcessingCenter.getFaxNumber());

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
    }
}