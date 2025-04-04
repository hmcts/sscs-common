package uk.gov.hmcts.reform.sscs.ccd.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

public class CreateCcdCaseServiceTest {


    private IdamTokens idamTokens;
    private CaseDetails caseDetails;
    private SscsCaseData sscsCaseData;

    @Mock
    private IdamService idamService;

    @Mock
    private CcdClient ccdClient;

    @Mock
    private SearchCcdCaseService searchCcdCaseService;

    private CreateCcdCaseService createCcdCaseService;


    @BeforeEach
    public void setUp() {
        openMocks(this);
        idamTokens = IdamTokens.builder()
                .idamOauth2Token("oauthToken")
                .serviceAuthorization("serviceAuthToken")
                .userId("user-id")
                .build();
        caseDetails = CaseDataUtils.buildCaseDetails();
        sscsCaseData = CaseDataUtils.buildCaseData();

        createCcdCaseService = new CreateCcdCaseService(idamService, new SscsCcdConvertService(), ccdClient);
    }

    @Test
    public void shouldCreateTheCaseInCcd() {
        StartEventResponse startEventResponse = StartEventResponse.builder().build();
        when(ccdClient.startCaseForCaseworker(idamTokens, "appealCreated")).thenReturn(startEventResponse);

        when(ccdClient.submitForCaseworker(eq(idamTokens), any())).thenReturn(caseDetails);
        when(searchCcdCaseService.findCaseByCaseRefOrCaseId(any(), any())).thenReturn(null);

        SscsCaseDetails sscsCaseDetails = createCcdCaseService.createCase(sscsCaseData, "appealCreated", "Summary", "Description", idamTokens);

        assertNotNull(sscsCaseDetails);
    }

}