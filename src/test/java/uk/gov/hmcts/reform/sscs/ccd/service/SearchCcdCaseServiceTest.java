package uk.gov.hmcts.reform.sscs.ccd.service;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static uk.gov.hmcts.reform.sscs.ccd.service.SscsCcdConvertService.normaliseNino;
import static uk.gov.hmcts.reform.sscs.ccd.service.SscsQueryBuilder.findCaseBySingleField;

import java.util.Collections;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.SearchResult;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

@RunWith(JUnitParamsRunner.class)
public class SearchCcdCaseServiceTest {

    public static final String CASE_REF = "SC068/17/00013";
    public static final String USER_ID = "userId";

    private IdamTokens idamTokens;
    private CaseDetails caseDetails;
    private SscsCaseDetails sscsCaseDetails;

    @Mock
    private IdamService idamService;

    private SscsCcdConvertService sscsCcdConvertService;

    @Mock
    private CcdClient ccdClient;

    @Mock
    private ReadCcdCaseService readCcdCaseService;

    private SearchCcdCaseService searchCcdCaseService;

    @Before
    public void setUp() throws Exception {
        openMocks(this);

        idamTokens = IdamTokens.builder()
                .idamOauth2Token("oauthToken")
                .serviceAuthorization("serviceAuthToken")
                .userId(USER_ID)
                .build();
        caseDetails = CaseDataUtils.buildCaseDetails();
        sscsCaseDetails = CaseDataUtils.convertCaseDetailsToSscsCaseDetails(caseDetails);
        sscsCcdConvertService = new SscsCcdConvertService();

        searchCcdCaseService = new SearchCcdCaseService(idamService,
                sscsCcdConvertService, ccdClient, readCcdCaseService);
    }

    @Test
    public void shouldReturnCaseForGivenCaseReferenceNumber() {

        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);
        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(SearchResult.builder().cases(singletonList(caseDetails)).build());

        SscsCaseDetails caseByCaseRef = searchCcdCaseService.findCaseByCaseRef(CASE_REF, idamTokens);

        assertNotNull(caseByCaseRef);

    }

    @Test
    public void shouldReturnNullIfNoCaseExistsForGiveCaseRef() {
        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);

        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(SearchResult.builder().cases(Collections.EMPTY_LIST).build());

        SscsCaseDetails caseByCaseRef = searchCcdCaseService.findCaseByCaseRef(CASE_REF, idamTokens);

        assertNull(caseByCaseRef);

    }

    @Test
    public void shouldReturnCaseForGivenCaseReferenceNumberAndCcdIdByCaseRef() {

        SscsCaseData sscsCaseData = CaseDataUtils.buildCaseData();
        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);

        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(SearchResult.builder().cases(singletonList(caseDetails)).build());

        SscsCaseDetails caseByCaseRef = searchCcdCaseService.findCaseByCaseRefOrCaseId(sscsCaseData, idamTokens);

        assertNotNull(caseByCaseRef);
        assertEquals(CASE_REF, caseByCaseRef.getData().getCaseReference());

    }


    @Test
    public void shouldReturnCaseForGivenCaseReferenceNumberAndCcdIdByCaseId() {

        SscsCaseData sscsCaseData = CaseDataUtils.buildCaseData();
        sscsCaseData.setCcdCaseId("1");

        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);

        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(SearchResult.builder().cases(Collections.EMPTY_LIST).build());

        when(readCcdCaseService.getByCaseId(1L, idamTokens)).thenReturn(sscsCaseDetails);

        SscsCaseDetails caseByCaseRef = searchCcdCaseService.findCaseByCaseRefOrCaseId(sscsCaseData, idamTokens);

        assertNotNull(caseByCaseRef);
        assertEquals(1L, caseByCaseRef.getId().longValue());

    }


    @Test
    public void shouldReturnNullIfCaseNotFoundForGivenCaseIdAndCaseRef() {

        SscsCaseData sscsCaseData = CaseDataUtils.buildCaseData();
        sscsCaseData.setCcdCaseId("1");

        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);

        when(ccdClient.searchCases(idamTokens, query.toString())).thenReturn(SearchResult.builder().cases(Collections.EMPTY_LIST).build());

        when(readCcdCaseService.getByCaseId(1L, idamTokens)).thenReturn(null);

        SscsCaseDetails caseByCaseRef = searchCcdCaseService.findCaseByCaseRefOrCaseId(sscsCaseData, idamTokens);

        assertNull(caseByCaseRef);

    }

    @Test
    @Parameters({"AB123456C", "AB 12 34 56 C", " AB12 34 56C ", "ab123456C", " ab 1 2 3 4 5 6 C"})
    public void shouldNormaliseNino(String uncleanNino) {
        assertEquals("AB123456C", normaliseNino(uncleanNino));
    }

    @Test
    @Parameters({"", " "})
    public void shouldReturnOriginalNinoIfEmpty(String emptyNino) {
        assertEquals(emptyNino, normaliseNino(emptyNino));
    }

    @Test
    public void shouldReturnOriginalNinoIfNull() {
        assertNull(normaliseNino(null));
    }
}