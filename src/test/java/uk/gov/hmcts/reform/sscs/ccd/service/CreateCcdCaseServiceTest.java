package uk.gov.hmcts.reform.sscs.ccd.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static uk.gov.hmcts.reform.sscs.utility.StringUtils.getMaskedNino;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.exception.CreateCcdCaseException;
import uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

public class CreateCcdCaseServiceTest {


    private IdamTokens idamTokens;
    private CaseDetails caseDetails;
    private SscsCaseData sscsCaseData;
    private ListAppender<ch.qos.logback.classic.spi.ILoggingEvent> appender;
    private Logger logger;

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

        appender = new ListAppender<>();
        appender.start();
        logger = (Logger) LoggerFactory.getLogger(CreateCcdCaseService.class);
        logger.addAppender(appender);

        createCcdCaseService = new CreateCcdCaseService(idamService, new SscsCcdConvertService(), ccdClient);
    }

    private boolean containsLog(String message) {
        return appender.list.stream()
                .anyMatch(event -> event.getFormattedMessage().contains(message));
    }

    @AfterEach
    void tearDown() {
        logger.detachAppender(appender);
    }

    @Test
    public void shouldCreateTheCaseInCcd() {
        StartEventResponse startEventResponse = StartEventResponse.builder().build();
        when(ccdClient.startCaseForCaseworker(idamTokens, "appealCreated")).thenReturn(startEventResponse);

        when(ccdClient.submitForCaseworker(eq(idamTokens), any())).thenReturn(caseDetails);
        when(searchCcdCaseService.findCaseByCaseRefOrCaseId(any(), any())).thenReturn(null);

        SscsCaseDetails sscsCaseDetails = createCcdCaseService.createCase(sscsCaseData, "appealCreated", "Summary", "Description", idamTokens);

        assertThat(sscsCaseDetails).isNotNull();
        assertThat(containsLog("Creating CCD case for Nino " + getMaskedNino("AB 22 55 66 B"))).isTrue();
        assertThat(containsLog("Case created with case id 1 for nino " + getMaskedNino("AB 22 55 66 B"))).isTrue();
    }

    @Test
    void shouldThrowExceptionAndLogMaskedNinoWhenCaseCreationFails() {
        when(ccdClient.startCaseForCaseworker(idamTokens, "appealCreated")).thenThrow(new RuntimeException("CCD service is down"));
        CreateCcdCaseException exception = assertThrows(CreateCcdCaseException.class, () -> createCcdCaseService.createCase(sscsCaseData, "appealCreated", "Summary", "Description", idamTokens));
        assertThat(exception).hasMessage("Error found in the case creation or callback process for the ccd case with SC" +
                " (SC068/17/00013) and ccdID (null) and Nino (" + getMaskedNino("AB 22 55 66 B") + ") and " +
                "Benefit Type (PIP) and exception: (CCD service is down) ");
    }
}