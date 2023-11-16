package uk.gov.hmcts.reform.sscs.ccd.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

public class ReadCcdCaseServiceTest {

    @Mock
    private IdamService idamService;
    @Mock
    private CcdClient ccdClient;
    @Mock
    private SscsCcdConvertService sscsCcdConvertService;

    ReadCcdCaseService readCcdCaseService;

    @Before
    public void setUp() throws Exception {
        openMocks(this);
        readCcdCaseService = new ReadCcdCaseService(idamService, ccdClient, sscsCcdConvertService);
    }

    @Test
    public void shouldReturnSscsCaseDetailsForGivenCaseId() {
        Long caseId = 1L;
        IdamTokens idamTokens = IdamTokens.builder().build();
        CaseDetails caseDetails = CaseDetails.builder().id(caseId).build();
        SscsCaseDetails sscsCaseDetails = SscsCaseDetails.builder().id(caseId).build();

        when(ccdClient.readForCaseworker(idamTokens, caseId)).thenReturn(caseDetails);
        when(sscsCcdConvertService.getCaseDetails(caseDetails)).thenReturn(sscsCaseDetails);

        SscsCaseDetails byCaseId = readCcdCaseService.getByCaseId(caseId, idamTokens);

        verify(ccdClient, times(1)).readForCaseworker(idamTokens, caseId);
        assertNotNull(byCaseId);
        assertEquals(caseId.longValue(), byCaseId.getId().longValue());
    }

    @Test
    public void shouldReturnSscsCaseDetailsForGivenCaseIdByGettingNewIdamToken() {
        Long caseId = 1L;
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
        Long caseId = 1L;
        IdamTokens idamTokens = IdamTokens.builder().build();

        when(ccdClient.readForCaseworker(idamTokens, caseId)).thenReturn(null);

        SscsCaseDetails byCaseId = readCcdCaseService.getByCaseId(caseId, idamTokens);

        verify(ccdClient, times(1)).readForCaseworker(idamTokens, caseId);
        assertNull(byCaseId);
    }
}
