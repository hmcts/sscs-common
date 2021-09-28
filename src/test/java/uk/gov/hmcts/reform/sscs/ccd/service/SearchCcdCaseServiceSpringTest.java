package uk.gov.hmcts.reform.sscs.ccd.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static uk.gov.hmcts.reform.sscs.ccd.service.SscsQueryBuilder.findCaseBySingleField;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.SearchResult;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.config.CcdRequestDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SearchCcdCaseServiceSpringTest.TestContextConfiguration.class)
@Slf4j
public class SearchCcdCaseServiceSpringTest {

    public static final String CASE_REF = "SC068/17/00013";
    public static final String USER_ID = "userId";

    private IdamTokens idamTokens;
    private CaseDetails caseDetails;
    private SscsCaseDetails sscsCaseDetails;

    @Autowired
    private SearchCcdCaseService searchCcdCaseService;

    @Autowired
    private CcdClient ccdClient;

    @Configuration
    @EnableRetry(proxyTargetClass = false)
    @Slf4j
    static class TestContextConfiguration {

        @Bean
        public RetryListener retryListener1() {
            return new RetryListener() {
                @Override
                public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
                    System.out.println("open");
                    return false;
                }

                @Override
                public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                    System.out.println("close");
                }

                @Override
                public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                    System.out.println("in the retry listener - onError");
                }
            };
        }

        @Bean
        public RetryTemplate retryTemplate() {
            RetryTemplate retryTemplate = new RetryTemplate();

            FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
            fixedBackOffPolicy.setBackOffPeriod(5000L);
            retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

            return retryTemplate;
        }

        @MockBean
        private IdamService idamService;

        @Bean
        public SscsCcdConvertService sscsCcdConvertService() {
            return new SscsCcdConvertService();
        }

        @MockBean
        public CcdRequestDetails ccdRequestDetails;

        @MockBean
        public CoreCaseDataApi coreCaseDataApi;

        @Bean
        public CcdClient ccdClient(CcdRequestDetails ccdRequestDetails,
                                    CoreCaseDataApi coreCaseDataApi) {
            return new CcdClient(ccdRequestDetails, coreCaseDataApi) {
                @Override
                public SearchResult searchCases(IdamTokens idamTokens, String query) {
                    throw new RuntimeException("connect error");
                }

            };
        }

        @MockBean
        public ReadCcdCaseService readCcdCaseService;

        @Bean
        public SearchCcdCaseService searchCcdCaseService(
                IdamService idamService,
                SscsCcdConvertService sscsCcdConvertService,
                CcdClient ccdClient,
                ReadCcdCaseService readCcdCaseService
        ) {
            return new SearchCcdCaseService(idamService, sscsCcdConvertService, ccdClient, readCcdCaseService);
        }
    }

    @Before
    public void setUp() throws Exception {

        idamTokens = IdamTokens.builder()
                .idamOauth2Token("oauthToken")
                .serviceAuthorization("serviceAuthToken")
                .userId(USER_ID)
                .build();
        caseDetails = CaseDataUtils.buildCaseDetails();
        sscsCaseDetails = CaseDataUtils.convertCaseDetailsToSscsCaseDetails(caseDetails);
    }

    @Test
    public void shouldRetryFindCaseForGivenCaseReferenceNumber() {

        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);
        /*when(ccdClient.searchCases(idamTokens, query.toString()))
                .thenThrow(new RuntimeException("connect exception"))
                .thenThrow(new RuntimeException("connect exception"))
                .thenThrow(new RuntimeException("connect exception"))
                .thenReturn(SearchResult.builder().cases(singletonList(caseDetails)).build());*/


        SscsCaseDetails caseByCaseRef = searchCcdCaseService.findCaseByCaseRef(CASE_REF, idamTokens);

        verify(ccdClient.searchCases(idamTokens, query.toString()), times(4));
        assertNotNull(caseByCaseRef);
    }

}