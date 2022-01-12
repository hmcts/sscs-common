package uk.gov.hmcts.reform.sscs.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.service.ReadCcdCaseService;
import uk.gov.hmcts.reform.sscs.ccd.service.SscsCcdConvertService;
import uk.gov.hmcts.reform.sscs.client.RefDataApi;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class RefDataServiceTest {

    @Mock
    private IdamService idamService;
    @Mock
    private RefDataApi refDataApi;

    RefDataService refDataService;

    @Before
    public void setUp() throws Exception {
        openMocks(this);
        refDataService = new RefDataService(refDataApi, idamService);
    }

    @Test
    public void shouldReturnCourtVenue() {
        IdamTokens idamTokens = IdamTokens.builder().idamOauth2Token("auth2").serviceAuthorization("s2s").build();

        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(refDataApi.courtVenueByName("auth2", "s2s", "venue_name")).thenReturn(caseDetails);

        when(sscsCcdConvertService.getCaseDetails(caseDetails)).thenReturn(sscsCaseDetails);

        SscsCaseDetails byCaseId = readCcdCaseService.getByCaseId(caseId, idamTokens);

        verify(ccdClient, times(1)).readForCaseworker(idamTokens, caseId);
        assertNotNull(byCaseId);
        assertEquals(caseId.longValue(), byCaseId.getId().longValue());
    }

    @Test
    public void shouldReturnSscsCaseDetailsForGivenCaseIdByGettingNewIdamToken() {
        Long caseId = new Long(1);
        IdamTokens idamTokens = IdamTokens.builder().build();
        CaseDetails caseDetails = CaseDetails.builder().id(caseId).build();
        SscsCaseDetails sscsCaseDetails = SscsCaseDetails.builder().id(caseId).build();

        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(ccdClient.readForCaseworker(idamTokens, caseId)).thenReturn(caseDetails);
        when(sscsCcdConvertService.getCaseDetails(caseDetails)).thenReturn(sscsCaseDetails);

        SscsCaseDetails byCaseId = readCcdCaseService.recover(caseId);

        verify(ccdClient, times(1)).readForCaseworker(idamTokens, caseId);
        assertNotNull(byCaseId);
        assertEquals(caseId.longValue(), byCaseId.getId().longValue());
    }


    @Test
    public void shouldReturnNullForGivenCaseId() {
        Long caseId = new Long(1);
        IdamTokens idamTokens = IdamTokens.builder().build();

        when(ccdClient.readForCaseworker(idamTokens, caseId)).thenReturn(null);

        SscsCaseDetails byCaseId = readCcdCaseService.getByCaseId(caseId, idamTokens);

        verify(ccdClient, times(1)).readForCaseworker(idamTokens, caseId);
        assertNull(byCaseId);
    }
}
