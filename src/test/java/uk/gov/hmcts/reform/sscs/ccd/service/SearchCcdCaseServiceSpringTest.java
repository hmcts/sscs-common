package uk.gov.hmcts.reform.sscs.ccd.service;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.sscs.ccd.service.SscsQueryBuilder.findCaseBySingleField;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.SearchResult;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.config.CcdRequestDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

/**
 * This test was added because we had found the Spring Retry was not working.
 * In reality, it is working, but we were not using the Retry framework correctly.
 * To enable retry you need to annotate Retryable on the class method the client calls.
 * If a client calls a method without the annotation which then calls another method in
 * it's own class with the annotation, Spring will not retry if the method fails.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SearchCcdCaseServiceSpringTest.TestContextConfiguration.class)
@Slf4j
public class SearchCcdCaseServiceSpringTest {

    public static final String CASE_REF = "SC068/17/00013";
    public static final String USER_ID = "userId";

    private IdamTokens idamTokens;
    private CaseDetails caseDetails;

    @Autowired
    private SearchCcdCaseService searchCcdCaseService;

    @Autowired
    private CcdClient ccdClient;

    @Configuration
    @EnableRetry
    static class TestContextConfiguration {

        @Bean
        public RetryTemplate retryTemplate() {
            RetryTemplate retryTemplate = new RetryTemplate();

            FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
            fixedBackOffPolicy.setBackOffPeriod(5000L);
            retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

            return retryTemplate;
        }

        @Bean
        public SscsCcdConvertService sscsCcdConvertService() {
            return new SscsCcdConvertService();
        }

        @MockitoBean
        public CcdRequestDetails ccdRequestDetails;

        @MockitoBean
        public CoreCaseDataApi coreCaseDataApi;

        @MockitoBean
        public CcdClient ccdClient;

        @MockitoBean
        public ReadCcdCaseService readCcdCaseService;

        @Bean
        public SearchCcdCaseService searchCcdCaseService(
                SscsCcdConvertService sscsCcdConvertService,
                CcdClient ccdClient,
                ReadCcdCaseService readCcdCaseService
        ) {
            return new SearchCcdCaseService(sscsCcdConvertService, ccdClient, readCcdCaseService);
        }
    }

    @BeforeEach
    public void setUp() throws Exception {

        idamTokens = IdamTokens.builder()
                .idamOauth2Token("oauthToken")
                .serviceAuthorization("serviceAuthToken")
                .userId(USER_ID)
                .build();
        caseDetails = CaseDataUtils.buildCaseDetails();
    }

    @Test
    public void shouldRetryFindCaseForGivenCaseReferenceNumberUntilItSucceeds() {

        SearchSourceBuilder query = findCaseBySingleField("data.caseReference", CASE_REF);
        when(ccdClient.searchCases(idamTokens, query.toString()))
                .thenThrow(new RuntimeException("connect exception"))
                .thenThrow(new RuntimeException("connect exception"))
                .thenReturn(SearchResult.builder().cases(singletonList(caseDetails)).build());

        SscsCaseDetails caseByCaseRef = searchCcdCaseService.findCaseByCaseRef(CASE_REF, idamTokens);

        verify(ccdClient, times(3)).searchCases(idamTokens, query.toString());
        assertNotNull(caseByCaseRef);
    }

}