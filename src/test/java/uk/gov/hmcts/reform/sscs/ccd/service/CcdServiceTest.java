package uk.gov.hmcts.reform.sscs.ccd.service;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils.buildCaseDetailsWithSurname;

import java.util.HashMap;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.*;
import uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

@RunWith(MockitoJUnitRunner.class)
public class CcdServiceTest {

    private IdamService idamService = mock(IdamService.class);
    private HashMap<String, String> searchCriteria;
    private String userId;
    private IdamTokens idamTokens;
    private CaseDetails caseDetails;
    private SscsCaseDetails sscsCaseDetails;
    private CcdClient ccdClient;
    private CcdService ccdService;
    private SscsCcdConvertService sscsCcdConvertService;
    private CreateCcdCaseService createCcdCaseService;
    private SearchCcdCaseService searchCcdCaseService;
    private UpdateCcdCaseService updateCcdCaseService;
    private ReadCcdCaseService readCcdCaseService;
    @Captor
    private ArgumentCaptor<CaseDataContent> captor;

    @Before
    public void setUp() {
        searchCriteria = new HashMap<String, String>() {{
                put("case.subscriptions.appellantSubscription.tya", "12345");
            }
        };
        userId = "userId";
        idamTokens = IdamTokens.builder()
                .idamOauth2Token("oauthToken")
                .serviceAuthorization("serviceAuthToken")
                .userId(userId)
                .build();
        caseDetails = CaseDataUtils.buildCaseDetails();
        sscsCaseDetails = CaseDataUtils.convertCaseDetailsToSscsCaseDetails(caseDetails);
        ccdClient = mock(CcdClient.class);
        sscsCcdConvertService = new SscsCcdConvertService();
        createCcdCaseService = new CreateCcdCaseService(idamService, sscsCcdConvertService, ccdClient);
        searchCcdCaseService = new SearchCcdCaseService(idamService, sscsCcdConvertService, ccdClient);
        updateCcdCaseService = new UpdateCcdCaseService(idamService, sscsCcdConvertService, ccdClient);
        readCcdCaseService = new ReadCcdCaseService(idamService, ccdClient);
        ccdService = new CcdService(createCcdCaseService, searchCcdCaseService, updateCcdCaseService, readCcdCaseService);
    }

    @Test
    public void canSearchForACase() {
        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria)).thenReturn(singletonList(caseDetails));

        List<SscsCaseDetails> caseDetailsList = ccdService.findCaseBy(searchCriteria, idamTokens);

        assertThat(caseDetailsList.size(), is(1));
        assertThat(caseDetailsList.get(0), is(sscsCaseDetails));
    }

    @Test
    public void givenACaseId_thenGetTheCaseDetailsById() {
        when(ccdClient.readForCaseworker(idamTokens, 1L)).thenReturn(caseDetails);

        CaseDetails result = ccdService.getByCaseId(1L, idamTokens);

        assertThat(result, is(caseDetails));
    }

    @Test
    public void canCreateACase() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().build();
        StartEventResponse startEventResponse = StartEventResponse.builder().build();
        when(ccdClient.startCaseForCaseworker(idamTokens, "appealCreated")).thenReturn(startEventResponse);

        when(ccdClient.submitForCaseworker(eq(idamTokens), any())).thenReturn(caseDetails);

        SscsCaseDetails result = ccdService.createCase(sscsCaseData, idamTokens);

        assertThat(result, is(sscsCaseDetails));
    }

    @Test
    public void givenAnUpdateCaseRequest_thenUpdateTheCase() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().build();
        StartEventResponse startEventResponse = StartEventResponse.builder().build();

        when(ccdClient.startEvent(idamTokens, 1L, "appealReceived")).thenReturn(startEventResponse);

        when(ccdClient.submitEventForCaseworker(eq(idamTokens), eq(1L),  any())).thenReturn(caseDetails);

        SscsCaseDetails result = ccdService.updateCase(sscsCaseData, 1L, "appealReceived", "SSCS - update event", "Updated case", idamTokens);

        assertThat(result, is(sscsCaseDetails));
    }

    @Test
    public void shouldUpdateSubscriptionInCcd() {

        StartEventResponse startEventResponse = StartEventResponse.builder().build();

        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria)).thenReturn(singletonList(caseDetails));
        when(ccdClient.startEvent(idamTokens, 1L, "subscriptionUpdated")).thenReturn(startEventResponse);

        when(ccdClient.submitEventForCaseworker(eq(idamTokens), eq(1L), captor.capture())).thenReturn(caseDetails);

        ccdService.updateSubscription("12345", "updated@test.com", idamTokens);

        assertEquals("updated@test.com", ((SscsCaseData) captor.getAllValues().get(0).getData()).getSubscriptions().getAppellantSubscription().getEmail());
        assertEquals("yes", ((SscsCaseData) captor.getAllValues().get(0).getData()).getSubscriptions().getAppellantSubscription().getSubscribeEmail());
    }

    @Test
    public void shouldUnSubscribeEmailNotification() {
        StartEventResponse startEventResponse = StartEventResponse.builder().build();

        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria)).thenReturn(singletonList(caseDetails));
        when(ccdClient.startEvent(idamTokens, 1L, "subscriptionUpdated")).thenReturn(startEventResponse);

        when(ccdClient.submitEventForCaseworker(eq(idamTokens), eq(1L), captor.capture())).thenReturn(caseDetails);

        ccdService.updateSubscription("12345", null, idamTokens);

        assertNull(((SscsCaseData) captor.getAllValues().get(0).getData()).getSubscriptions().getAppellantSubscription().getEmail());
        assertEquals("no", ((SscsCaseData) captor.getAllValues().get(0).getData()).getSubscriptions().getAppellantSubscription().getSubscribeEmail());
    }

    @Test
    public void shouldReturnCaseGivenSurnameAndAppealNumber() {
        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria)).thenReturn(singletonList(caseDetails));

        SscsCaseData caseData = ccdService.findCcdCaseByAppealNumberAndSurname("12345", "Test", idamTokens);

        assertNotNull(caseData);
        assertEquals("Test", caseData.getAppeal().getAppellant().getName().getLastName());
    }

    @Test
    public void shouldReturnCaseGivenWhenSurnameHasAccent() {
        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria)).thenReturn(singletonList(caseDetails));

        SscsCaseData caseData = ccdService.findCcdCaseByAppealNumberAndSurname("12345", "Tést", idamTokens);

        assertNotNull(caseData);
        assertEquals("Test", caseData.getAppeal().getAppellant().getName().getLastName());
    }

    @Test
    public void shouldReturnCaseGivenWhenSurnameInCcdHasAccent() {
        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria))
                .thenReturn(singletonList(buildCaseDetailsWithSurname("Tést")));

        SscsCaseData caseData = ccdService.findCcdCaseByAppealNumberAndSurname("12345", "Test", idamTokens);

        assertNotNull(caseData);
        assertEquals("Tést", caseData.getAppeal().getAppellant().getName().getLastName());
    }

    @Test
    public void shouldReturnCaseGivenSurnameContainsNotAlphaCharacter() {
        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria)).thenReturn(singletonList(caseDetails));

        SscsCaseData caseData = ccdService.findCcdCaseByAppealNumberAndSurname("12345", "Te-st", idamTokens);

        assertNotNull(caseData);
        assertEquals("Test", caseData.getAppeal().getAppellant().getName().getLastName());
    }

    @Test
    public void shouldReturnCaseGivenSurnameInCcdContainsNotAlphaCharacter() {
        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria))
                .thenReturn(singletonList(buildCaseDetailsWithSurname("Te-st")));

        SscsCaseData caseData = ccdService.findCcdCaseByAppealNumberAndSurname("12345", "Test", idamTokens);

        assertNotNull(caseData);
        assertEquals("Te-st", caseData.getAppeal().getAppellant().getName().getLastName());
    }

    @Test
    public void shouldReturnNullIfSurnameInvalid() {
        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria)).thenReturn(singletonList(caseDetails));

        SscsCaseData caseData = ccdService.findCcdCaseByAppealNumberAndSurname("12345", "XXX", idamTokens);

        verify(ccdClient).searchForCaseworker(eq(idamTokens), any());

        assertNull(caseData);
    }

    @Test
    public void givenABenefitTypeAndNinoAndMrnDate_thenReturnCaseDetailsIfTheyExistInCcd() {

        SscsCaseData caseData = SscsCaseData.builder().generatedNino("JT123456B")
                .appeal(Appeal.builder()
                        .benefitType(BenefitType.builder().code("JSA").build())
                        .mrnDetails(MrnDetails.builder().mrnDate("2018-01-01").build()).build()).build();

        searchCriteria = new HashMap<String, String>() {{
                put("case.generatedNino", "JT123456B");
                put("case.appeal.benefitType.code", "JSA");
                put("case.appeal.mrnDetails.mrnDate", "2018-01-01");
            }
        };

        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria))
                .thenReturn(singletonList(caseDetails));

        SscsCaseDetails caseDetails = ccdService.findCcdCaseByNinoAndBenefitTypeAndMrnDate(caseData, idamTokens);

        assertNotNull(caseDetails);
    }

    @Test
    public void givenABenefitTypeAndNinoAndMrnDate_thenReturnNullIfTheyDoNotExistInCcd() {

        SscsCaseData caseData = SscsCaseData.builder().generatedNino("JT123456B")
                .appeal(Appeal.builder()
                        .benefitType(BenefitType.builder().code("JSA").build())
                        .mrnDetails(MrnDetails.builder().mrnDate("2018-01-01").build()).build()).build();

        searchCriteria = new HashMap<String, String>() {{
                put("case.generatedNino", "JT123456B");
                put("case.appeal.benefitType.code", "JSA");
                put("case.appeal.mrnDetails.mrnDate", "2018-01-01");
            }
        };

        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria))
                .thenReturn(emptyList());

        SscsCaseDetails caseDetails = ccdService.findCcdCaseByNinoAndBenefitTypeAndMrnDate(caseData, idamTokens);

        assertNull(caseDetails);
    }

    @Test
    public void givenABenefitTypeAndNinoAndNoMrnDate_thenReturnNull() {

        SscsCaseData caseData = SscsCaseData.builder().generatedNino("JT123456B")
                .appeal(Appeal.builder()
                        .benefitType(BenefitType.builder().code("JSA").build())
                        .mrnDetails(MrnDetails.builder().mrnDate(null).build()).build()).build();

        SscsCaseDetails caseDetails = ccdService.findCcdCaseByNinoAndBenefitTypeAndMrnDate(caseData, idamTokens);

        assertNull(caseDetails);
    }

}