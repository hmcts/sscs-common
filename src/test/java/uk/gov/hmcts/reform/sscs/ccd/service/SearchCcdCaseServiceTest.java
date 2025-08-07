package uk.gov.hmcts.reform.sscs.ccd.service;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.sscs.ccd.domain.State.DRAFT;
import static uk.gov.hmcts.reform.sscs.ccd.domain.State.DRAFT_ARCHIVED;
import static uk.gov.hmcts.reform.sscs.ccd.service.SscsCcdConvertService.normaliseNino;
import static uk.gov.hmcts.reform.sscs.ccd.service.SscsQueryBuilder.findCaseBySingleField;

import java.util.List;
import org.assertj.core.util.Lists;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.SearchResult;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

@ExtendWith(MockitoExtension.class)
public class SearchCcdCaseServiceTest {

    public static final String CASE_REF = "SC068/17/00013";
    public static final String USER_ID = "userId";

    private IdamTokens idamTokens;
    private CaseDetails caseDetails;
    private SscsCaseDetails sscsCaseDetails;

    @Mock
    private SscsCcdConvertService sscsCcdConvertService;
    @Mock
    private CcdClient ccdClient;
    @Mock
    private ReadCcdCaseService readCcdCaseService;

    @InjectMocks
    private SearchCcdCaseService searchCcdCaseService;

    @BeforeEach
    public void setUp() throws Exception {
        idamTokens = IdamTokens.builder()
                .idamOauth2Token("oauthToken")
                .serviceAuthorization("serviceAuthToken")
                .userId(USER_ID)
                .build();
        caseDetails = CaseDataUtils.buildCaseDetails();
        sscsCaseDetails = CaseDataUtils.convertCaseDetailsToSscsCaseDetails(caseDetails);
        searchCcdCaseService = new SearchCcdCaseService(sscsCcdConvertService, ccdClient, readCcdCaseService);
    }

    @Test
    public void shouldReturnCaseForGivenCaseReferenceNumber() {
        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);
        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(SearchResult.builder().cases(singletonList(caseDetails)).build());
        when(sscsCcdConvertService.getCaseDetails(caseDetails)).thenReturn(sscsCaseDetails);

        SscsCaseDetails caseByCaseRef = searchCcdCaseService.findCaseByCaseRef(CASE_REF, idamTokens);

        assertNotNull(caseByCaseRef);
    }

    @Test
    public void shouldReturnListOfCasesForGivenCaseReferenceNumber() {
        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);
        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(SearchResult.builder().cases(singletonList(caseDetails)).build());

        List<SscsCaseDetails> casesByCaseRef = searchCcdCaseService.findListOfCasesByCaseRef(CASE_REF, idamTokens);

        assertNotNull(casesByCaseRef);
        assertEquals(1, casesByCaseRef.size());
    }

    @Test
    public void shouldReturnNullIfNoCaseExistsForGiveCaseRef() {
        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);

        when(ccdClient.searchCases(idamTokens, query.toString()))
                .thenReturn(SearchResult.builder().cases(emptyList()).build());

        SscsCaseDetails caseByCaseRef = searchCcdCaseService.findCaseByCaseRef(CASE_REF, idamTokens);

        assertNull(caseByCaseRef);
    }

    @Test
    public void shouldReturnCaseForGivenCaseReferenceNumberAndCcdIdByCaseRef() {
        SscsCaseData sscsCaseData = CaseDataUtils.buildCaseData();
        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);
        when(ccdClient.searchCases(idamTokens, query.toString()))
                .thenReturn(SearchResult.builder().cases(singletonList(caseDetails)).build());
        when(readCcdCaseService.getByCaseId(1L,idamTokens)).thenReturn(sscsCaseDetails);
        when(sscsCcdConvertService.getCaseDetails(caseDetails)).thenReturn(sscsCaseDetails);

        SscsCaseDetails caseByCaseRef = searchCcdCaseService.findCaseByCaseRefOrCaseId(sscsCaseData, idamTokens);

        assertNotNull(caseByCaseRef);
        assertEquals(CASE_REF, caseByCaseRef.getData().getCaseReference());
    }

    @Test
    public void shouldReturnListOfCasesForGivenCaseReferenceNumberAndCcdIdIsNullByCaseRef() {
        SscsCaseData sscsCaseData = CaseDataUtils.buildCaseData();
        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);

        when(ccdClient.searchCases(idamTokens, query.toString()))
                .thenReturn(SearchResult.builder().cases(Lists.list(caseDetails, caseDetails)).build());
        when(sscsCcdConvertService.getCaseDetails(caseDetails)).thenReturn(sscsCaseDetails);

        List<SscsCaseDetails> casesByCaseRef = searchCcdCaseService.findListOfCasesByCaseRefOrCaseId(sscsCaseData, idamTokens);

        assertNotNull(casesByCaseRef);
        assertEquals(2, casesByCaseRef.size());
        assertEquals(CASE_REF, casesByCaseRef.getFirst().getData().getCaseReference());
    }

    @Test
    public void shouldReturnListOfCaseOnlyOneForGivenCaseReferenceNumberAndCcdIdIsNotNullByCaseRef() {
        String caseId = "1";
        SscsCaseData sscsCaseData = CaseDataUtils.buildCaseData();
        sscsCaseData.setCcdCaseId(caseId);
        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);

        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(SearchResult.builder().cases(Lists.list(caseDetails, caseDetails)).build());
        when(readCcdCaseService.getByCaseId(Long.parseLong(caseId), idamTokens)).thenReturn(sscsCaseDetails);

        List<SscsCaseDetails> casesByCaseRef = searchCcdCaseService.findListOfCasesByCaseRefOrCaseId(sscsCaseData, idamTokens);

        assertNotNull(casesByCaseRef);
        assertEquals(1, casesByCaseRef.size());
        assertEquals(CASE_REF, casesByCaseRef.getFirst().getData().getCaseReference());
        assertEquals(caseId, casesByCaseRef.getFirst().getData().getCcdCaseId());
    }

    @Test
    public void shouldReturnAllCases() {
        var draftCase = CaseDetails.builder().state(DRAFT.getId()).build();
        var draftSscsCase = SscsCaseDetails.builder().state(DRAFT.getId()).build();
        var archivedCase = CaseDetails.builder().state(DRAFT_ARCHIVED.getId()).build();
        var archivedSscsCase = SscsCaseDetails.builder().state(DRAFT_ARCHIVED.getId()).build();
        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);
        SearchResult searchResult = SearchResult.builder().cases(List.of(caseDetails, draftCase, archivedCase)).build();
        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(searchResult);
        when(sscsCcdConvertService.getCaseDetails(caseDetails)).thenReturn(sscsCaseDetails);
        when(sscsCcdConvertService.getCaseDetails(draftCase)).thenReturn(draftSscsCase);
        when(sscsCcdConvertService.getCaseDetails(archivedCase)).thenReturn(archivedSscsCase);

        List<SscsCaseDetails> result =
                searchCcdCaseService.findAllCasesBySearchCriteria(query.toString(), idamTokens);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertThat(result, containsInAnyOrder(sscsCaseDetails, draftSscsCase, archivedSscsCase));
    }

    @Test
    public void shouldReturnListOfSubmittedCases() {
        var draftCase = CaseDetails.builder().state(DRAFT.getId()).build();
        var draftSscsCase = SscsCaseDetails.builder().state(DRAFT.getId()).build();
        var archivedCase = CaseDetails.builder().state(DRAFT_ARCHIVED.getId()).build();
        var archivedSscsCase = SscsCaseDetails.builder().state(DRAFT_ARCHIVED.getId()).build();
        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);
        SearchResult searchResult = SearchResult.builder().cases(List.of(caseDetails, draftCase, archivedCase)).build();
        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(searchResult);
        when(sscsCcdConvertService.getCaseDetails(caseDetails)).thenReturn(sscsCaseDetails);
        when(sscsCcdConvertService.getCaseDetails(draftCase)).thenReturn(draftSscsCase);
        when(sscsCcdConvertService.getCaseDetails(archivedCase)).thenReturn(archivedSscsCase);

        List<SscsCaseDetails> result =
                searchCcdCaseService.findSubmittedCasesBySearchCriteria(query.toString(), idamTokens);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sscsCaseDetails, result.getFirst());
    }

    @Test
    public void shouldReturnEmptyListWhenNoCasesFound() {
        SearchResult searchResult = SearchResult.builder().cases(List.of()).build();
        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);
        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(searchResult);

        List<SscsCaseDetails> result =
                searchCcdCaseService.findSubmittedCasesBySearchCriteria(query.toString(), idamTokens);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListWhenSearchResultIsNull() {
        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);
        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(null);

        List<SscsCaseDetails> result =
                searchCcdCaseService.findSubmittedCasesBySearchCriteria(query.toString(), idamTokens);

        assertNull(result);
    }

    @Test
    public void shouldReturnEmptyListWhenSearchResultCasesAreNull() {
        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);
        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(SearchResult.builder().build());

        List<SscsCaseDetails> result =
                searchCcdCaseService.findSubmittedCasesBySearchCriteria(query.toString(), idamTokens);

        assertNull(result);
    }

    @Test
    public void shouldReturnCaseForGivenCaseReferenceNumberAndCcdIdByCaseId() {
        SscsCaseData sscsCaseData = CaseDataUtils.buildCaseData();
        sscsCaseData.setCcdCaseId("1");

        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);

        when(ccdClient.searchCases(idamTokens, query.toString()))
                .thenReturn(SearchResult.builder().cases(emptyList()).build());

        when(readCcdCaseService.getByCaseId(1L, idamTokens)).thenReturn(sscsCaseDetails);

        SscsCaseDetails caseByCaseRef = searchCcdCaseService.findCaseByCaseRefOrCaseId(sscsCaseData, idamTokens);

        assertNotNull(caseByCaseRef);
        assertEquals(1L, caseByCaseRef.getId().longValue());
    }

    @Test
    public void shouldReturnListOfCasesForGivenCaseReferenceNumberAndCcdIdByCaseId() {
        SscsCaseData sscsCaseData = CaseDataUtils.buildCaseData();
        sscsCaseData.setCcdCaseId("1");

        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);

        when(ccdClient.searchCases(idamTokens, query.toString()))
                .thenReturn(SearchResult.builder().cases(emptyList()).build());

        when(readCcdCaseService.getByCaseId(1L, idamTokens)).thenReturn(sscsCaseDetails);

        List<SscsCaseDetails> casesByCaseId = searchCcdCaseService.findListOfCasesByCaseRefOrCaseId(sscsCaseData, idamTokens);

        assertNotNull(casesByCaseId);
        assertEquals(1, casesByCaseId.size());
        assertEquals(1L, casesByCaseId.getFirst().getId().longValue());

    }

    @Test
    public void shouldReturnNullIfCaseNotFoundForGivenCaseIdAndCaseRef() {
        SscsCaseData sscsCaseData = CaseDataUtils.buildCaseData();
        sscsCaseData.setCcdCaseId("1");

        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);

        when(ccdClient.searchCases(idamTokens, query.toString()))
                .thenReturn(SearchResult.builder().cases(emptyList()).build());

        when(readCcdCaseService.getByCaseId(1L, idamTokens)).thenReturn(null);

        SscsCaseDetails caseByCaseRef = searchCcdCaseService.findCaseByCaseRefOrCaseId(sscsCaseData, idamTokens);

        assertNull(caseByCaseRef);
    }

    @ParameterizedTest
    @ValueSource(strings = {"AB123456C", "AB 12 34 56 C", " AB12 34 56C ", "ab123456C", " ab 1 2 3 4 5 6 C"})
    public void shouldNormaliseNino(String uncleanNino) {
        assertEquals("AB123456C", normaliseNino(uncleanNino));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    public void shouldReturnOriginalNinoIfEmpty(String emptyNino) {
        assertEquals("", normaliseNino(emptyNino));
    }

    @Test
    public void shouldReturnOriginalNinoIfNull() {
        assertNull(normaliseNino(null));
    }
}
