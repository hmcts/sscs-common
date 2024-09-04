package uk.gov.hmcts.reform.sscs.ccd.service;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils.buildCaseData;
import static uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils.buildCaseDataMap;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.SearchResult;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.Subscriptions;
import uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

@RunWith(MockitoJUnitRunner.class)
public class CcdServiceTest {

    private static final String APPELLANT_SEARCH_FIELD = "case.subscriptions.appellantSubscription.tya";
    private static final String APPELLANT_APPEAL_NUMBER = "app-appeal-number";
    private static final String REPRESENTATIVE_APPEAL_NUMBER = "rep-appeal-number";
    private static final String UPDATED_TEST_COM = "updated@test.com";
    private static final String YES = "yes";

    private IdamTokens idamTokens;
    private CaseDetails caseDetails;
    private SscsCaseDetails sscsCaseDetails;
    private CcdClient ccdClient;
    private CcdService ccdService;

    @Mock
    private IdamService idamService;

    @Mock
    private ReadCcdCaseService readCcdCaseService;

    @Mock
    private SscsCcdConvertService ccdConvertService;


    @Captor
    private ArgumentCaptor<CaseDataContent> captor;

    @Before
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
        ccdClient = mock(CcdClient.class);
        readCcdCaseService = mock(ReadCcdCaseService.class);

        SscsCcdConvertService sscsCcdConvertService = new SscsCcdConvertService();
        SearchCcdCaseService searchCcdCaseService = new SearchCcdCaseService(sscsCcdConvertService, ccdClient, readCcdCaseService);
        UpdateCcdCaseService updateCcdCaseService = new UpdateCcdCaseService(idamService, sscsCcdConvertService, ccdClient, ccdService);
        CreateCcdCaseService createCcdCaseService = new CreateCcdCaseService(idamService, sscsCcdConvertService, ccdClient);
        ccdService = new CcdService(createCcdCaseService, searchCcdCaseService, updateCcdCaseService, readCcdCaseService, ccdClient, ccdConvertService, false);
    }

    @Test
    public void canSearchForACase() {
        SearchSourceBuilder query = SscsQueryBuilder.findCaseBySingleField(APPELLANT_SEARCH_FIELD, APPELLANT_APPEAL_NUMBER);

        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(SearchResult.builder().cases(singletonList(caseDetails)).build());

        List<SscsCaseDetails> caseDetailsList = ccdService.findCaseBy(APPELLANT_SEARCH_FIELD, APPELLANT_APPEAL_NUMBER, idamTokens);

        assertThat(caseDetailsList.size(), is(1));

        Assertions.assertThat(caseDetailsList.get(0))
            .usingRecursiveComparison()
            .ignoringFields(
                "data.appeal.appellant.appointee.id",
                "data.appeal.appellant.id",
                "data.appeal.rep.id",
                "data.jointParty.id")
            .isEqualTo(sscsCaseDetails);
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

        Assertions.assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields(
                "data.appeal.appellant.appointee.id",
                "data.appeal.appellant.id",
                "data.appeal.rep.id",
                "data.jointParty.id")
            .isEqualTo(sscsCaseDetails);
    }

    @Test
    public void givenAnUpdateCaseRequest_thenUpdateTheCase() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().build();
        StartEventResponse startEventResponse = StartEventResponse.builder().build();

        when(ccdClient.startEvent(idamTokens, 1L, "appealReceived")).thenReturn(startEventResponse);

        when(ccdClient.submitEventForCaseworker(eq(idamTokens), eq(1L),  any())).thenReturn(caseDetails);

        SscsCaseDetails result = ccdService.updateCase(sscsCaseData, 1L, "appealReceived", "SSCS - update event", "Updated case", idamTokens);

        Assertions.assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields(
                "data.appeal.appellant.appointee.id",
                "data.appeal.appellant.id",
                "data.appeal.rep.id",
                "data.jointParty.id")
            .isEqualTo(sscsCaseDetails);
    }

    @Test
    public void givenAnUpdateCaseRequest_thenUpdateTheCaseWithoutRetry() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().build();
        StartEventResponse startEventResponse = StartEventResponse.builder().build();

        when(ccdClient.startEvent(idamTokens, 1L, "appealReceived")).thenReturn(startEventResponse);

        when(ccdClient.submitEventForCaseworker(eq(idamTokens), eq(1L),  any())).thenReturn(caseDetails);

        SscsCaseDetails result = ccdService.updateCaseWithoutRetry(sscsCaseData, 1L, "appealReceived", "SSCS - update event", "Updated case", idamTokens);

        Assertions.assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields(
                "data.appeal.appellant.appointee.id",
                "data.appeal.appellant.id",
                "data.appeal.rep.id",
                "data.jointParty.id")
            .isEqualTo(sscsCaseDetails);
    }

    @Test
    public void shouldUpdateAppellantSubscriptionInCcd() {

        StartEventResponse startEventResponse = StartEventResponse.builder().build();

        SearchSourceBuilder query = SscsQueryBuilder.findCaseByTyaNumberQuery(APPELLANT_APPEAL_NUMBER);

        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(SearchResult.builder().cases(singletonList(caseDetails)).build());
        when(ccdClient.startEvent(idamTokens, 1L, "subscriptionUpdated")).thenReturn(startEventResponse);

        when(ccdClient.submitEventForCaseworker(eq(idamTokens), eq(1L), captor.capture())).thenReturn(caseDetails);

        ccdService.updateSubscription(APPELLANT_APPEAL_NUMBER, UPDATED_TEST_COM, idamTokens);

        assertEquals(UPDATED_TEST_COM, ((SscsCaseData) captor.getAllValues().get(0).getData()).getSubscriptions().getAppellantSubscription().getEmail());
        assertEquals(YES, ((SscsCaseData) captor.getAllValues().get(0).getData()).getSubscriptions().getAppellantSubscription().getSubscribeEmail());
    }

    @Test
    public void shouldUnSubscribeAppellantEmailNotification() {
        StartEventResponse startEventResponse = StartEventResponse.builder().build();

        SearchSourceBuilder query = SscsQueryBuilder.findCaseByTyaNumberQuery(APPELLANT_APPEAL_NUMBER);

        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(SearchResult.builder().cases(singletonList(caseDetails)).build());
        when(ccdClient.startEvent(idamTokens, 1L, "subscriptionUpdated")).thenReturn(startEventResponse);

        when(ccdClient.submitEventForCaseworker(eq(idamTokens), eq(1L), captor.capture())).thenReturn(caseDetails);

        ccdService.updateSubscription(APPELLANT_APPEAL_NUMBER, null, idamTokens);

        assertNull(((SscsCaseData) captor.getAllValues().get(0).getData()).getSubscriptions().getAppellantSubscription().getEmail());
        assertEquals("no", ((SscsCaseData) captor.getAllValues().get(0).getData()).getSubscriptions().getAppellantSubscription().getSubscribeEmail());
    }

    @Test
    public void givenABenefitTypeAndNinoAndMrnDate_thenReturnCaseDetailsIfTheyExistInCcd() {

        SearchSourceBuilder query = SscsQueryBuilder.findCcdCaseByNinoAndBenefitTypeAndMrnDateQuery("JT123456B", "JSA", "2018-01-01");

        when(ccdClient.searchCases(idamTokens, query.toString()))
                .thenReturn(SearchResult.builder().cases(singletonList(caseDetails)).build());

        SscsCaseDetails caseDetails = ccdService.findCcdCaseByNinoAndBenefitTypeAndMrnDate("JT123456B", "JSA", "2018-01-01", idamTokens);

        assertNotNull(caseDetails);
    }

    @Test
    public void givenABenefitTypeAndNinoAndMrnDate_thenReturnNullIfTheyDoNotExistInCcd() {

        SearchSourceBuilder query = SscsQueryBuilder.findCcdCaseByNinoAndBenefitTypeAndMrnDateQuery("JT123456B", "JSA", "2018-01-01");

        when(ccdClient.searchCases(idamTokens, query.toString()))
                .thenReturn(SearchResult.builder().cases(emptyList()).build());

        SscsCaseDetails caseDetails = ccdService.findCcdCaseByNinoAndBenefitTypeAndMrnDate("JT123456B", "JSA", "2018-01-01", idamTokens);

        assertNull(caseDetails);
    }

    @Test
    public void givenABenefitTypeAndNinoAndNoMrnDate_thenReturnNull() {

        SscsCaseDetails caseDetails = ccdService.findCcdCaseByNinoAndBenefitTypeAndMrnDate("JT123456B", "JSA", null, idamTokens);

        assertNull(caseDetails);
    }

    @Test
    public void shouldRetrieveAppealByRepresentativeAppealNumber() {
        SearchSourceBuilder query = SscsQueryBuilder.findCaseByTyaNumberQuery(REPRESENTATIVE_APPEAL_NUMBER);

        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(SearchResult.builder().cases(singletonList(caseDetails)).build());

        SscsCaseDetails caseByAppealNumber = ccdService.findCaseByAppealNumber(REPRESENTATIVE_APPEAL_NUMBER, idamTokens);

        verify(ccdClient).searchCases(idamTokens, query.toString());
        assertNotNull(caseByAppealNumber);
    }

    @Test
    public void shouldReturnNullIfNoAppealFoundForGivenAppealNumber() {
        SearchSourceBuilder query = SscsQueryBuilder.findCaseByTyaNumberQuery(REPRESENTATIVE_APPEAL_NUMBER);
        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(SearchResult.builder().cases(emptyList()).build());

        SscsCaseDetails caseByAppealNumber = ccdService.findCaseByAppealNumber(REPRESENTATIVE_APPEAL_NUMBER, idamTokens);

        verify(ccdClient).searchCases(any(), any());
        assertNull(caseByAppealNumber);
    }

    @Test
    public void shouldUpdateRepresentativeSubscriptionInCcd() {
        SearchSourceBuilder query = SscsQueryBuilder.findCaseByTyaNumberQuery(REPRESENTATIVE_APPEAL_NUMBER);
        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(SearchResult.builder().cases(singletonList(caseDetails)).build());

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
        SearchSourceBuilder query = SscsQueryBuilder.findCaseByTyaNumberQuery(REPRESENTATIVE_APPEAL_NUMBER);
        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(SearchResult.builder().cases(singletonList(caseDetails)).build());
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
        SearchSourceBuilder query = SscsQueryBuilder.findCaseByTyaNumberQuery(REPRESENTATIVE_APPEAL_NUMBER);
        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(SearchResult.builder().cases(singletonList(caseDetails)).build());
        when(ccdClient.startEvent(idamTokens, 1L, "subscriptionUpdated")).thenReturn(StartEventResponse.builder().build());
        when(ccdClient.submitEventForCaseworker(eq(idamTokens), eq(1L), captor.capture())).thenReturn(caseDetails);

        ccdService.updateSubscription(REPRESENTATIVE_APPEAL_NUMBER, UPDATED_TEST_COM, idamTokens);

        verify(ccdClient).startEvent(idamTokens, 1L, "subscriptionUpdated");
    }


}
