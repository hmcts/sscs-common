package uk.gov.hmcts.reform.sscs.idam;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.Base64;
import java.util.List;

import ch.qos.logback.core.Appender;
import org.apache.logging.log4j.LogManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

import ch.qos.logback.classic.Level;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.spi.LoggingEvent;

@RunWith(MockitoJUnitRunner.class)
public class IdamServiceTest {

    @Mock
    private AuthTokenGenerator authTokenGenerator;
    @Mock
    private IdamApiClient idamApiClient;

    @Mock
    private Appender mockAppender;

    @Captor
    private ArgumentCaptor captorLoggingEvent;

    private Authorize authToken;
    private IdamService idamService;

    @Before
    public void setUp() {
        authToken = new Authorize("redirect/", "authCode", "access");
        idamService = new IdamService(authTokenGenerator, idamApiClient
        );

        ReflectionTestUtils.setField(idamService, "idamOauth2UserEmail", "email");
        ReflectionTestUtils.setField(idamService, "idamOauth2UserPassword", "pass");
        ReflectionTestUtils.setField(idamService, "idamOauth2ClientId", "id");
        ReflectionTestUtils.setField(idamService, "idamOauth2ClientSecret", "secret");
        ReflectionTestUtils.setField(idamService, "idamOauth2RedirectUrl", "redirect/");

        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);
    }

    @After
    public void teardown() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAppender(mockAppender);
    }

    @Test
    public void shouldReturnAuthTokenGivenNewRequestWithAppropriateLogMessages() {
        String auth = "auth";
        when(authTokenGenerator.generate()).thenReturn(auth);

        String base64Authorisation = Base64.getEncoder().encodeToString("email:pass".getBytes());
        when(idamApiClient.authorizeCodeType("Basic " + base64Authorisation,
                "code",
                "id",
                "redirect/",
                " ")
        ).thenReturn(authToken);
        when(idamApiClient.authorizeToken(authToken.getCode(),
                "authorization_code",
                "redirect/",
                "id",
                "secret",
                " ")
        ).thenReturn(authToken);

        UserDetails expectedUserDetails = new UserDetails("16");
        given(idamApiClient.getUserDetails(eq("Bearer " + authToken.getAccessToken()))).willReturn(expectedUserDetails);

        IdamTokens idamTokens = idamService.getIdamTokens();
        assertThat(idamTokens.getServiceAuthorization(), is(auth));
        assertThat(idamTokens.getUserId(), is(expectedUserDetails.getId()));
        assertThat(idamTokens.getIdamOauth2Token(), containsString("Bearer access"));

        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        final List<LoggingEvent> loggingEvent = (List<LoggingEvent>) captorLoggingEvent.getAllValues();

        //Check the message being logged is correct
        assertThat(loggingEvent.get(0).getFormattedMessage(), is("Requesting idam token"));
        assertThat(loggingEvent.get(1).getFormattedMessage(), is("Requesting idam token successful"));
    }

    @Test
    public void shouldExceptionGivenErrorWithAppropriateLogMessages() {
        String base64Authorisation = Base64.getEncoder().encodeToString("email:pass".getBytes());
        when(idamApiClient.authorizeCodeType("Basic " + base64Authorisation,
            "code",
            "id",
            "redirect/",
            " ")
        ).thenThrow(new RuntimeException());

        try {
            IdamTokens idamTokens = idamService.getIdamTokens();
        } catch (RuntimeException rte) {
            // Ignore for the purposes of this test
        }

        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        final List<LoggingEvent> loggingEvent = (List<LoggingEvent>) captorLoggingEvent.getAllValues();

        //Check the message being logged is correct
        assertThat(loggingEvent.get(0).getFormattedMessage(), is("Requesting idam token"));
        assertThat(loggingEvent.get(1).getFormattedMessage(), containsString("Requesting idam token failed:"));
    }
}