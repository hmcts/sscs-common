package uk.gov.hmcts.reform.sscs.ccd.client;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.sscs.ccd.config.CcdRequestDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.service.SscsCcdConvertService;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

public class CcdClientTest {

    private CoreCaseDataApi coreCaseDataApi = mock(CoreCaseDataApi.class);
    private IdamService idamService = mock(IdamService.class);
    private HashMap<String, String> searchCriteria;
    private String jurisdiction;
    private String caseTypeId;
    private CcdRequestDetails ccdRequestDetails;
    private String oauthToken;
    private String serviceAuthToken;
    private String userId;
    private IdamTokens idamTokens;
    private long caseDetailsId;
    private CaseDetails caseDetails;
    private CcdClient underTest;
    private SscsCcdConvertService sscsCcdConvertService;

    @Before
    public void setUp() {
        searchCriteria = new HashMap<>();
        jurisdiction = "jurisdiction";
        caseTypeId = "caseTypeId";
        ccdRequestDetails = CcdRequestDetails.builder()
                .jurisdictionId(jurisdiction)
                .caseTypeId(caseTypeId)
                .build();
        oauthToken = "oauthToken";
        serviceAuthToken = "serviceAuthToken";
        userId = "userId";
        idamTokens = IdamTokens.builder()
                .idamOauth2Token(oauthToken)
                .serviceAuthorization(serviceAuthToken)
                .userId(userId)
                .build();
        caseDetailsId = 123L;
        caseDetails = CaseDetails.builder()
                .id(caseDetailsId)
                .build();
        sscsCcdConvertService = mock(SscsCcdConvertService.class);
        underTest = new CcdClient(ccdRequestDetails, coreCaseDataApi, idamService, sscsCcdConvertService);

        when(idamService.getIdamTokens()).thenReturn(idamTokens);

    }

    @Test
    public void canSearchForACase() {
        when(coreCaseDataApi.searchForCaseworker(
                oauthToken, serviceAuthToken, userId, jurisdiction, caseTypeId, searchCriteria)
        ).thenReturn(singletonList(caseDetails));

        SscsCaseDetails sscsCaseDetails = SscsCaseDetails.builder().build();
        when(sscsCcdConvertService.getCaseDetails(caseDetails)).thenReturn(sscsCaseDetails);

        List<SscsCaseDetails> caseDetailsList = underTest.findCaseBy(searchCriteria);

        assertThat(caseDetailsList.size(), is(1));
        assertThat(caseDetailsList.get(0), is(sscsCaseDetails));
    }

    @Test
    public void givenACaseId_thenGetTheCaseDetailsById() {
        when(coreCaseDataApi.readForCaseWorker(
                oauthToken, serviceAuthToken, userId, jurisdiction, caseTypeId, "1")
        ).thenReturn(caseDetails);

        CaseDetails result = underTest.getByCaseId("1");

        assertThat(result, is(caseDetails));
    }

    @Test
    public void canCreateACase() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().build();
        StartEventResponse startEventResponse = StartEventResponse.builder().build();
        when(coreCaseDataApi.startForCaseworker(oauthToken,
                serviceAuthToken,
                userId,
                jurisdiction,
                caseTypeId,
                "appealCreated"
                )).thenReturn(startEventResponse);
        CaseDataContent caseDataContent = CaseDataContent.builder().build();
        when(sscsCcdConvertService.getCaseDataContent(
                sscsCaseData,
                startEventResponse,
                "SSCS - appeal created event",
                "Created SSCS")).thenReturn(caseDataContent);

        when(coreCaseDataApi.submitForCaseworker(
                oauthToken,
                serviceAuthToken,
                userId,
                jurisdiction,
                caseTypeId,
                true,
                caseDataContent
        )).thenReturn(caseDetails);

        SscsCaseDetails expectedSscsCaseDetails = SscsCaseDetails.builder().build();
        when(sscsCcdConvertService.getCaseDetails(caseDetails)).thenReturn(expectedSscsCaseDetails);

        SscsCaseDetails sscsCaseDetails = underTest.createCase(sscsCaseData, "Created SSCS");

        assertThat(sscsCaseDetails, is(expectedSscsCaseDetails));
    }

    @Test
    public void givenAnUpdateCaseRequest_thenUpdateTheCase() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().build();
        StartEventResponse startEventResponse = StartEventResponse.builder().build();

        when(coreCaseDataApi.startEventForCaseWorker(oauthToken,
                serviceAuthToken,
                userId,
                jurisdiction,
                caseTypeId,
                "1",
                "appealReceived"
        )).thenReturn(startEventResponse);

        CaseDataContent caseDataContent = CaseDataContent.builder().build();
        when(sscsCcdConvertService.getCaseDataContent(
                sscsCaseData,
                startEventResponse,
                "SSCS - update event",
                "Updated case")).thenReturn(caseDataContent);

        when(coreCaseDataApi.submitEventForCaseWorker(
                oauthToken,
                serviceAuthToken,
                userId,
                jurisdiction,
                caseTypeId,
                "1",
                true,
                caseDataContent
        )).thenReturn(caseDetails);

        SscsCaseDetails expectedSscsCaseDetails = SscsCaseDetails.builder().build();
        when(sscsCcdConvertService.getCaseDetails(caseDetails)).thenReturn(expectedSscsCaseDetails);

        SscsCaseDetails sscsCaseDetails = underTest.updateCase(sscsCaseData, 1L, "appealReceived", "SSCS - update event", "Updated case");

        assertThat(sscsCaseDetails, is(expectedSscsCaseDetails));
    }
}