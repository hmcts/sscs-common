package uk.gov.hmcts.reform.sscs.ccd.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
class UpdateCcdCaseServiceTest {

    @InjectMocks
    private UpdateCcdCaseService updateCcdCaseService;

    private IdamTokens idamTokens;
    private CaseDetails caseDetails;
    private SscsCaseDetails sscsCaseDetails;
    private Long caseId = 1L;
    private String eventType = "EVENT_TYPE";
    private String eventId = "EVENT_ID";
    private String eventToken = "EVENT_TOKEN";

    @Mock
    private IdamService idamService;
    @Mock
    private SscsCcdConvertService sscsCcdConvertService;
    @Mock
    private CcdClient ccdClient;
    @Mock
    private StartEventResponse startEventResponse;
    @Mock
    private Function<SscsCaseData, UpdateCcdCaseService.UpdateResult> mutator;
    @Mock
    private Consumer<SscsCaseData> consumer;
    @Mock
    private CaseDataContent caseDataContent;
    @Mock
    private SscsCaseData caseData;


    @BeforeEach
    public void setUp() {
        openMocks(this);
        String userId = "userId";
        idamTokens = IdamTokens.builder()
                .idamOauth2Token("oauthToken")
                .serviceAuthorization("serviceAuthToken")
                .userId(userId)
                .build();
        caseDetails = CaseDataUtils.buildCaseDetails();
        sscsCaseDetails = CaseDataUtils.convertCaseDetailsToSscsCaseDetails(caseDetails);

        updateCcdCaseService = new UpdateCcdCaseService(idamService, sscsCcdConvertService, ccdClient);
    }

    @Test
    public void shouldUpdateCaseV2() {
        when(ccdClient.startEvent(idamTokens, caseId, eventType)).thenReturn(startEventResponse);
        SscsCaseData returnedCaseData = SscsCaseData.builder().build();
        when(sscsCcdConvertService.getCaseData(caseDetails.getData())).thenReturn(returnedCaseData);
        when(sscsCcdConvertService.getCaseDataContent(eq(returnedCaseData), eq(startEventResponse), any(), any()))
                .thenReturn(caseDataContent);
        when(startEventResponse.getCaseDetails()).thenReturn(caseDetails);

        CaseDetails submittedDetails = Mockito.mock(CaseDetails.class);
        when(ccdClient.submitEventForCaseworker(idamTokens, caseId, caseDataContent)).thenReturn(submittedDetails);
        SscsCaseDetails updatedCaseDetails = Mockito.mock(SscsCaseDetails.class);
        when(sscsCcdConvertService.getCaseDetails(submittedDetails)).thenReturn(updatedCaseDetails);
        when(mutator.apply(returnedCaseData)).thenReturn(new UpdateCcdCaseService.UpdateResult("summary", "description"));

        updateCcdCaseService.updateCaseV2(caseId, eventType, idamTokens, mutator);

        assertThat(returnedCaseData.getCcdCaseId()).isEqualTo(caseId.toString());
        verify(mutator, times(1)).apply(returnedCaseData);
    }

    @Test
    public void shouldUpdateCaseV2WithSummaryAndDescription() {
        when(ccdClient.startEvent(idamTokens, caseId, eventType)).thenReturn(startEventResponse);
        SscsCaseData returnedCaseData = SscsCaseData.builder().build();
        when(sscsCcdConvertService.getCaseData(caseDetails.getData())).thenReturn(returnedCaseData);
        when(sscsCcdConvertService.getCaseDataContent(eq(returnedCaseData), eq(startEventResponse), any(), any()))
                .thenReturn(caseDataContent);
        when(startEventResponse.getCaseDetails()).thenReturn(caseDetails);

        CaseDetails submittedDetails = Mockito.mock(CaseDetails.class);
        when(ccdClient.submitEventForCaseworker(idamTokens, caseId, caseDataContent)).thenReturn(submittedDetails);
        SscsCaseDetails updatedCaseDetails = Mockito.mock(SscsCaseDetails.class);
        when(sscsCcdConvertService.getCaseDetails(submittedDetails)).thenReturn(updatedCaseDetails);

        updateCcdCaseService.updateCaseV2(caseId, eventType, "summary", "description", idamTokens, consumer);

        assertThat(returnedCaseData.getCcdCaseId()).isEqualTo(caseId.toString());
        verify(consumer, times(1)).accept(returnedCaseData);
    }

    @Test
    public void shouldUpdateCase() {
        when(ccdClient.startEvent(idamTokens, caseId, eventType)).thenReturn(startEventResponse);
        when(sscsCcdConvertService.getCaseDataContent(caseData, startEventResponse, "summary", "description"))
                .thenReturn(caseDataContent);
        when(ccdClient.submitEventForCaseworker(idamTokens, caseId, caseDataContent)).thenReturn(caseDetails);
        SscsCaseDetails updatedCaseDetails = Mockito.mock(SscsCaseDetails.class);
        when(sscsCcdConvertService.getCaseDetails(caseDetails)).thenReturn(updatedCaseDetails);

        SscsCaseDetails returnedCaseDetails =
                updateCcdCaseService.updateCase(caseData, caseId, eventType, "summary", "description", idamTokens);

        assertThat(returnedCaseDetails).isEqualTo(updatedCaseDetails);
    }

    @Test
    public void shouldUpdateCaseWithEventIdAndToken() {
        when(sscsCcdConvertService.getCaseDataContent(eventToken, eventId, caseData, "summary", "description"))
                .thenReturn(caseDataContent);
        when(ccdClient.submitEventForCaseworker(idamTokens, caseId, caseDataContent)).thenReturn(caseDetails);
        SscsCaseDetails updatedCaseDetails = Mockito.mock(SscsCaseDetails.class);
        when(sscsCcdConvertService.getCaseDetails(caseDetails)).thenReturn(updatedCaseDetails);

        SscsCaseDetails returnedCaseDetails =
                updateCcdCaseService.updateCase(caseData, caseId, eventId, eventToken, eventType,
                        "summary", "description", idamTokens);

        assertThat(returnedCaseDetails).isEqualTo(updatedCaseDetails);
    }

    @Test
    public void shouldUpdateCaseWithoutRetry() {
        when(ccdClient.startEvent(idamTokens, caseId, eventType)).thenReturn(startEventResponse);
        when(sscsCcdConvertService.getCaseDataContent(caseData, startEventResponse, "summary", "description"))
                .thenReturn(caseDataContent);
        when(ccdClient.submitEventForCaseworker(idamTokens, caseId, caseDataContent)).thenReturn(caseDetails);
        SscsCaseDetails updatedCaseDetails = Mockito.mock(SscsCaseDetails.class);
        when(sscsCcdConvertService.getCaseDetails(caseDetails)).thenReturn(updatedCaseDetails);

        SscsCaseDetails returnedCaseDetails =
                updateCcdCaseService.updateCaseWithoutRetry(caseData, caseId, eventType,
                        "summary", "description", idamTokens);

        assertThat(returnedCaseDetails).isEqualTo(updatedCaseDetails);
    }

    @Test
    public void shouldRecover() {
        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(ccdClient.startEvent(idamTokens, caseId, eventType)).thenReturn(startEventResponse);
        when(sscsCcdConvertService.getCaseDataContent(caseData, startEventResponse, "summary", "description"))
                .thenReturn(caseDataContent);
        when(ccdClient.submitEventForCaseworker(idamTokens, caseId, caseDataContent)).thenReturn(caseDetails);
        SscsCaseDetails updatedCaseDetails = Mockito.mock(SscsCaseDetails.class);
        when(sscsCcdConvertService.getCaseDetails(caseDetails)).thenReturn(updatedCaseDetails);

        SscsCaseDetails returnedCaseDetails =
                updateCcdCaseService.recover(caseData, caseId, eventType,
                        "summary", "description", idamTokens);

        assertThat(returnedCaseDetails).isEqualTo(updatedCaseDetails);
    }

    @Test
    public void shouldSetSupplementaryData() {
        Map<String, Map<String, Map<String, Object>>> supplementaryData = new HashMap<>();

        updateCcdCaseService.setSupplementaryData(idamTokens, caseId, supplementaryData);

        verify(ccdClient, times(1)).setSupplementaryData(idamTokens, caseId, supplementaryData);
    }
}
