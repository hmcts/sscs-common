package uk.gov.hmcts.reform.sscs.ccd.service;

import static java.util.Collections.singletonList;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collections;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

public class SearchCcdCaseServiceTest {

    public static final String CASE_REF = "SC068/17/00013";
    public static final String USER_ID = "userId";

    private IdamTokens idamTokens;
    private HashMap<String, String> searchCriteria;
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
        initMocks(this);

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

        searchCriteria = new HashMap<String, String>() {
            {
                put("case.caseReference", CASE_REF);
            }
        };
        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria)).thenReturn(singletonList(caseDetails));

        SscsCaseDetails caseByCaseRef = searchCcdCaseService.findCaseByCaseRef(CASE_REF, idamTokens);

        assertNotNull(caseByCaseRef);

    }

    @Test
    public void shouldReturnNullIfNoCaseExistsForGiveCaseRef() {
        searchCriteria = new HashMap<String, String>() {
            {
                put("case.caseReference", CASE_REF);
            }
        };

        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria)).thenReturn(Collections.EMPTY_LIST);

        SscsCaseDetails caseByCaseRef = searchCcdCaseService.findCaseByCaseRef(CASE_REF, idamTokens);

        assertNull(caseByCaseRef);

    }

    @Test
    public void shouldReturnCaseForGivenCaseReferenceNumberAndCcdIdByCaseRef() {

        SscsCaseData sscsCaseData = CaseDataUtils.buildCaseData();

        searchCriteria = new HashMap<String, String>() {
            {
                put("case.caseReference", CASE_REF);
            }
        };
        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria)).thenReturn(singletonList(caseDetails));

        SscsCaseDetails caseByCaseRef = searchCcdCaseService.findCaseByCaseRefOrCaseId(sscsCaseData, idamTokens);

        assertNotNull(caseByCaseRef);
        assertEquals(CASE_REF, caseByCaseRef.getData().getCaseReference());

    }


    @Test
    public void shouldReturnCaseForGivenCaseReferenceNumberAndCcdIdByCaseId() {

        SscsCaseData sscsCaseData = CaseDataUtils.buildCaseData();
        sscsCaseData.setCcdCaseId("1");

        searchCriteria = new HashMap<String, String>() {
            {
                put("case.caseReference", CASE_REF);
            }
        };
        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria)).thenReturn(Collections.EMPTY_LIST);

        when(readCcdCaseService.getByCaseId(1L, idamTokens)).thenReturn(sscsCaseDetails);

        SscsCaseDetails caseByCaseRef = searchCcdCaseService.findCaseByCaseRefOrCaseId(sscsCaseData, idamTokens);

        assertNotNull(caseByCaseRef);
        assertEquals(1L, caseByCaseRef.getId().longValue());

    }


    @Test
    public void shouldReturnNullIfCaseNotFoundForGivenCaseIdAndCaseRef() {

        SscsCaseData sscsCaseData = CaseDataUtils.buildCaseData();
        sscsCaseData.setCcdCaseId("1");

        searchCriteria = new HashMap<String, String>() {
            {
                put("case.caseReference", CASE_REF);
            }
        };
        when(ccdClient.searchForCaseworker(idamTokens, searchCriteria)).thenReturn(Collections.EMPTY_LIST);

        when(readCcdCaseService.getByCaseId(1L, idamTokens)).thenReturn(null);

        SscsCaseDetails caseByCaseRef = searchCcdCaseService.findCaseByCaseRefOrCaseId(sscsCaseData, idamTokens);

        assertNull(caseByCaseRef);

    }


}