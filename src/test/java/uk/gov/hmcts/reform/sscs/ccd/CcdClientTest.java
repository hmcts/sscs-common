package uk.gov.hmcts.reform.sscs.ccd;

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
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
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
        underTest = new CcdClient(coreCaseDataApi, idamService, sscsCcdConvertService);
    }

    @Test
    public void canSearchForACase() {
        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(coreCaseDataApi.searchForCaseworker(
                oauthToken, serviceAuthToken, userId, jurisdiction, caseTypeId, searchCriteria)
        ).thenReturn(singletonList(caseDetails));

        SscsCaseDetails sscsCaseDetails = SscsCaseDetails.builder().build();
        when(sscsCcdConvertService.getCaseDetails(caseDetails)).thenReturn(sscsCaseDetails);

        List<SscsCaseDetails> caseDetailsList = underTest.findCaseBy(ccdRequestDetails, searchCriteria);

        assertThat(caseDetailsList.size(), is(1));
        assertThat(caseDetailsList.get(0), is(sscsCaseDetails));
    }

    @Test
    public void canCreateACase() {
        when(idamService.getIdamTokens()).thenReturn(idamTokens);
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


        SscsCaseDetails sscsCaseDetails = underTest.createCase(ccdRequestDetails, sscsCaseData);

        assertThat(sscsCaseDetails, is(expectedSscsCaseDetails));
    }
}