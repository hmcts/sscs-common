package uk.gov.hmcts.reform.sscs.ccd.service;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
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

    private static final String APPELLANT_APPEAL_NUMBER = "app-appeal-number";
    private static final String REPRESENTATIVE_APPEAL_NUMBER = "rep-appeal-number";
    private static final String UPDATED_TEST_COM = "updated@test.com";
    private static final String YES = "yes";
    private IdamTokens idamTokens;
    private CaseDetails caseDetails;
    private SscsCaseDetails sscsCaseDetails;
    private CcdClient ccdClient;
    private CcdService ccdService;
    private SscsCcdConvertService sscsCcdConvertService;
    private SearchCcdCaseService searchCcdCaseService;
    private UpdateCcdCaseService updateCcdCaseService;
    private CreateCcdCaseService createCcdCaseService;

    @Mock
    private IdamService idamService;

    @Mock
    private ReadCcdCaseService readCcdCaseService;


    @Captor
    private ArgumentCaptor<CaseDataContent> captor;

    private final Map<String, String> representativeSearchCriteria = new HashMap<String, String>() {
        {
            put("case.subscriptions.representativeSubscription.tya", REPRESENTATIVE_APPEAL_NUMBER);
        }
    };
    private final Map<String, String> searchCriteria = new HashMap<String, String>() {
        {
            put("case.subscriptions.appellantSubscription.tya", APPELLANT_APPEAL_NUMBER);
        }
    };

    private final Map<String, String> appellantSearchCriteriaForRep = new HashMap<String, String>() {
        {
            put("case.subscriptions.appellantSubscription.tya", REPRESENTATIVE_APPEAL_NUMBER);
        }
    };

    @Before
    public void setUp() {
        initMocks(this);
        String userId = "userId";
        idamTokens = IdamTokens.builder()
                .idamOauth2Token("oauthToken")
                .serviceAuthorization("serviceAuthToken")
                .userId(userId)
                .build();
        caseDetails = CaseDataUtils.buildCaseDetails();
        sscsCaseDetails = CaseDataUtils.convertCaseDetailsToSscsCaseDetails(caseDetails);
        ccdClient = mock(CcdClient.class);
        sscsCcdConvertService = new SscsCcdConvertService();
        searchCcdCaseService = new SearchCcdCaseService(idamService, sscsCcdConvertService, ccdClient, readCcdCaseService);
        updateCcdCaseService = new UpdateCcdCaseService(idamService, sscsCcdConvertService, ccdClient);
        createCcdCaseService = new CreateCcdCaseService(sscsCcdConvertService, ccdClient);
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
        when(readCcdCaseService.getByCaseId(1L, idamTokens)).thenReturn(sscsCaseDetails);

        SscsCaseDetails result = ccdService.getByCaseId(1L, idamTokens);

        assertThat(result, is(sscsCaseDetails));
    }

    @Test
    public void canCreateACase() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().build();
        StartEventResponse startEventResponse = StartEventResponse.builder().token("1234").build();

        when(ccdClient.startCaseForCaseworker(idamTokens,  "appealCreated")).thenReturn(startEventResponse);

        when(ccdClient.submitForCaseworker(eq(idamTokens), any())).thenReturn(caseDetails);

        SscsCaseDetails result = ccdService.createCase(sscsCaseData, "appealCreated", "Summary", "Description", idamTokens);

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
    public void shouldUpdateAppellantSubscriptionInCcd() {

        StartEventResponse startEventResponse = StartEventResponse.builder().build();

        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria)).thenReturn(singletonList(caseDetails));
        when(ccdClient.startEvent(idamTokens, 1L, "subscriptionUpdated")).thenReturn(startEventResponse);

        when(ccdClient.submitEventForCaseworker(eq(idamTokens), eq(1L), captor.capture())).thenReturn(caseDetails);

        ccdService.updateSubscription(APPELLANT_APPEAL_NUMBER, UPDATED_TEST_COM, idamTokens);

        assertEquals(UPDATED_TEST_COM, ((SscsCaseData) captor.getAllValues().get(0).getData()).getSubscriptions().getAppellantSubscription().getEmail());
        assertEquals(YES, ((SscsCaseData) captor.getAllValues().get(0).getData()).getSubscriptions().getAppellantSubscription().getSubscribeEmail());
    }

    @Test
    public void shouldUnSubscribeAppellantEmailNotification() {
        StartEventResponse startEventResponse = StartEventResponse.builder().build();

        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria)).thenReturn(singletonList(caseDetails));
        when(ccdClient.startEvent(idamTokens, 1L, "subscriptionUpdated")).thenReturn(startEventResponse);

        when(ccdClient.submitEventForCaseworker(eq(idamTokens), eq(1L), captor.capture())).thenReturn(caseDetails);

        ccdService.updateSubscription(APPELLANT_APPEAL_NUMBER, null, idamTokens);

        assertNull(((SscsCaseData) captor.getAllValues().get(0).getData()).getSubscriptions().getAppellantSubscription().getEmail());
        assertEquals("no", ((SscsCaseData) captor.getAllValues().get(0).getData()).getSubscriptions().getAppellantSubscription().getSubscribeEmail());
    }

    @Test
    public void shouldReturnCaseGivenSurnameAndAppealNumber() {
        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria)).thenReturn(singletonList(caseDetails));

        SscsCaseData caseData = ccdService.findCcdCaseByAppealNumberAndSurname(APPELLANT_APPEAL_NUMBER, "Test", idamTokens);

        assertNotNull(caseData);
        assertEquals("Test", caseData.getAppeal().getAppellant().getName().getLastName());
        assertEquals("1", caseData.getCcdCaseId());
    }

    @Test
    public void shouldReturnCaseGivenWhenSurnameHasAccent() {
        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria)).thenReturn(singletonList(caseDetails));

        SscsCaseData caseData = ccdService.findCcdCaseByAppealNumberAndSurname(APPELLANT_APPEAL_NUMBER, "Tést", idamTokens);

        assertNotNull(caseData);
        assertEquals("Test", caseData.getAppeal().getAppellant().getName().getLastName());
    }

    @Test
    public void shouldReturnCaseGivenWhenSurnameInCcdHasAccent() {
        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria))
                .thenReturn(singletonList(buildCaseDetailsWithSurname("Tést")));

        SscsCaseData caseData = ccdService.findCcdCaseByAppealNumberAndSurname(APPELLANT_APPEAL_NUMBER, "Test", idamTokens);

        assertNotNull(caseData);
        assertEquals("Tést", caseData.getAppeal().getAppellant().getName().getLastName());
    }

    @Test
    public void shouldReturnCaseGivenSurnameContainsNotAlphaCharacter() {
        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria)).thenReturn(singletonList(caseDetails));

        SscsCaseData caseData = ccdService.findCcdCaseByAppealNumberAndSurname(APPELLANT_APPEAL_NUMBER, "Te-st", idamTokens);

        assertNotNull(caseData);
        assertEquals("Test", caseData.getAppeal().getAppellant().getName().getLastName());
    }

    @Test
    public void shouldReturnCaseGivenSurnameInCcdContainsNotAlphaCharacter() {
        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria))
                .thenReturn(singletonList(buildCaseDetailsWithSurname("Te-st")));

        SscsCaseData caseData = ccdService.findCcdCaseByAppealNumberAndSurname(APPELLANT_APPEAL_NUMBER, "Test", idamTokens);

        assertNotNull(caseData);
        assertEquals("Te-st", caseData.getAppeal().getAppellant().getName().getLastName());
    }

    @Test
    public void shouldReturnNullIfSurnameInvalid() {
        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria)).thenReturn(singletonList(caseDetails));

        SscsCaseData caseData = ccdService.findCcdCaseByAppealNumberAndSurname(APPELLANT_APPEAL_NUMBER, "XXX", idamTokens);

        verify(ccdClient).searchForCaseworker(eq(idamTokens), any());

        assertNull(caseData);
    }

    @Test
    public void givenABenefitTypeAndNinoAndMrnDate_thenReturnCaseDetailsIfTheyExistInCcd() {

        SscsCaseData caseData = SscsCaseData.builder().generatedNino("JT123456B")
                .appeal(Appeal.builder()
                        .benefitType(BenefitType.builder().code("JSA").build())
                        .mrnDetails(MrnDetails.builder().mrnDate("2018-01-01").build()).build()).build();

        final Map<String, String> searchCriteria = new HashMap<String, String>() {{
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

        final Map<String, String> searchCriteria = new HashMap<String, String>() {{
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

    @Test
    public void shouldRetrieveAppealByRepresentativeAppealNumber() {
        when(ccdClient.searchForCaseworker(idamTokens, appellantSearchCriteriaForRep)).thenReturn(emptyList());
        when(ccdClient.searchForCaseworker(idamTokens, representativeSearchCriteria))
                .thenReturn(singletonList(caseDetails));

        SscsCaseDetails caseByAppealNumber = ccdService.findCaseByAppealNumber(REPRESENTATIVE_APPEAL_NUMBER, idamTokens);

        verify(ccdClient).searchForCaseworker(idamTokens, appellantSearchCriteriaForRep);
        verify(ccdClient).searchForCaseworker(idamTokens,representativeSearchCriteria);
        assertNotNull(caseByAppealNumber);
    }

    @Test
    public void shouldReturnNullIfNoAppealFoundForGivenAppealNumber() {
        when(ccdClient.searchForCaseworker(any(), any())).thenReturn(emptyList());

        SscsCaseDetails caseByAppealNumber = ccdService.findCaseByAppealNumber(REPRESENTATIVE_APPEAL_NUMBER, idamTokens);

        verify(ccdClient, times(3)).searchForCaseworker(any(), any());
        assertNull(caseByAppealNumber);
    }


    @Test
    public void shouldReturnSscsCaseDetailsIfAppealNumberAndLastNameMatchForRepresentative() {
        when(ccdClient.searchForCaseworker(idamTokens, appellantSearchCriteriaForRep)).thenReturn(emptyList());
        when(ccdClient.searchForCaseworker(idamTokens, representativeSearchCriteria))
                .thenReturn(singletonList(caseDetails));

        SscsCaseData sscsCaseData = ccdService
                .findCcdCaseByAppealNumberAndSurname(REPRESENTATIVE_APPEAL_NUMBER, "Giles", idamTokens);

        assertNotNull(sscsCaseData);
    }

    @Test
    public void shouldUpdateRepresentativeSubscriptionInCcd() {
        when(ccdClient.searchForCaseworker(idamTokens, appellantSearchCriteriaForRep)).thenReturn(emptyList());
        when(ccdClient.searchForCaseworker(idamTokens, representativeSearchCriteria))
                .thenReturn(singletonList(caseDetails));

        when(ccdClient.startEvent(idamTokens, 1L, "subscriptionUpdated")).thenReturn(StartEventResponse.builder().build());
        when(ccdClient.submitEventForCaseworker(eq(idamTokens), eq(1L), captor.capture())).thenReturn(caseDetails);

        ccdService.updateSubscription(REPRESENTATIVE_APPEAL_NUMBER, UPDATED_TEST_COM, idamTokens);

        assertEquals("Rep email should be updated", UPDATED_TEST_COM, ((SscsCaseData) captor.getAllValues().get(0).getData()).getSubscriptions().getRepresentativeSubscription().getEmail());
        assertEquals("Rep is subscribed", YES, ((SscsCaseData) captor.getAllValues().get(0).getData()).getSubscriptions().getRepresentativeSubscription().getSubscribeEmail());
    }

    @Test
    public void shouldUpdateRepresentativeSubscriptionInCcdWhenAppellantSubscriptionIsNull() {
        Subscriptions subscriptions = sscsCaseDetails.getData().getSubscriptions();
        Subscriptions newSubscriptions = subscriptions.toBuilder().appellantSubscription(null).build();

        CaseDetails caseDetails = CaseDetails.builder().id(1L).data(buildCaseDataMap(buildCaseData().toBuilder().subscriptions(newSubscriptions).build())).build();
        when(ccdClient.searchForCaseworker(idamTokens, representativeSearchCriteria)).thenReturn(singletonList(caseDetails));
        when(ccdClient.startEvent(idamTokens, 1L, "subscriptionUpdated")).thenReturn(StartEventResponse.builder().build());
        when(ccdClient.submitEventForCaseworker(eq(idamTokens), eq(1L), captor.capture())).thenReturn(caseDetails);

        ccdService.updateSubscription(REPRESENTATIVE_APPEAL_NUMBER, UPDATED_TEST_COM, idamTokens);
        assertEquals("Rep email should be updated", UPDATED_TEST_COM, ((SscsCaseData) captor.getAllValues().get(0).getData()).getSubscriptions().getRepresentativeSubscription().getEmail());
    }

    @Test
    public void shouldUpdateCaseWhenAppellantAndRepresentativeSubscriptionAreNull() {
        Subscriptions subscriptions = sscsCaseDetails.getData().getSubscriptions();
        Subscriptions newSubscriptions = subscriptions.toBuilder().appellantSubscription(null).representativeSubscription(null).build();

        CaseDetails caseDetails = CaseDetails.builder().id(1L).data(buildCaseDataMap(buildCaseData().toBuilder().subscriptions(newSubscriptions).build())).build();
        when(ccdClient.searchForCaseworker(idamTokens, representativeSearchCriteria)).thenReturn(singletonList(caseDetails));
        when(ccdClient.startEvent(idamTokens, 1L, "subscriptionUpdated")).thenReturn(StartEventResponse.builder().build());
        when(ccdClient.submitEventForCaseworker(eq(idamTokens), eq(1L), captor.capture())).thenReturn(caseDetails);

        ccdService.updateSubscription(REPRESENTATIVE_APPEAL_NUMBER, UPDATED_TEST_COM, idamTokens);

        verify(ccdClient).startEvent(idamTokens, 1L, "subscriptionUpdated");
    }


}