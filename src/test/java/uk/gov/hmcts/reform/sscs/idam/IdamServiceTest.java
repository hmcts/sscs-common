package uk.gov.hmcts.reform.sscs.idam;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

import java.util.Base64;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;


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

        verify(mockAppender, times(4)).doAppend(captorLoggingEvent.capture());
        final List<LoggingEvent> loggingEvent = (List<LoggingEvent>) captorLoggingEvent.getAllValues();

        //Check the message being logged is correct
        assertThat(loggingEvent.get(0).getFormattedMessage(), is("No cached IDAM token found, requesting from IDAM service."));
        assertThat(loggingEvent.get(1).getFormattedMessage(), is("Requesting idam token"));
        assertThat(loggingEvent.get(2).getFormattedMessage(), is("Passing authorization code to IDAM to get a token"));
        assertThat(loggingEvent.get(3).getFormattedMessage(), is("Requesting idam token successful"));
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

        verify(mockAppender, times(3)).doAppend(captorLoggingEvent.capture());
        final List<LoggingEvent> loggingEvent = (List<LoggingEvent>) captorLoggingEvent.getAllValues();

        //Check the message being logged is correct
        assertThat(loggingEvent.get(0).getFormattedMessage(), is("No cached IDAM token found, requesting from IDAM service."));
        assertThat(loggingEvent.get(1).getFormattedMessage(), is("Requesting idam token"));
        assertThat(loggingEvent.get(2).getFormattedMessage(), containsString("Requesting idam token failed:"));
    }

    @Test
    public void shouldVerifyTokenSignature() {
        String auth = "auth";
        authToken = new Authorize("redirect/", "authCode", "eyJ0eXAiOiJKV1QiLCJ6aXAiOiJOT05FIiwia2lkIjoiYi9PNk92VnYxK3krV2dySDVVaTlXVGlvTHQwPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJzc2NzLWNpdGl6ZW40QGhtY3RzLm5ldCIsImF1dGhfbGV2ZWwiOjAsImF1ZGl0VHJhY2tpbmdJZCI6Ijc1YzEyMTk3LWFjYmYtNDg2Zi1iNDI5LTJlYWEwZjMyNWVkMCIsImlzcyI6Imh0dHA6Ly9mci1hbTo4MDgwL29wZW5hbS9vYXV0aDIvaG1jdHMiLCJ0b2tlbk5hbWUiOiJhY2Nlc3NfdG9rZW4iLCJ0b2tlbl90eXBlIjoiQmVhcmVyIiwiYXV0aEdyYW50SWQiOiIwMGZhYThiNy03OWY5LTRiZWQtODI1OS0zZDE0MDEzOGYzZjIiLCJhdWQiOiJzc2NzIiwibmJmIjoxNTc4NTAwNDU0LCJncmFudF90eXBlIjoiYXV0aG9yaXphdGlvbl9jb2RlIiwic2NvcGUiOlsib3BlbmlkIiwicHJvZmlsZSIsInJvbGVzIl0sImF1dGhfdGltZSI6MTU3ODUwMDQ1MTAwMCwicmVhbG0iOiIvaG1jdHMiLCJleHAiOjE1Nzg1MjkyNTQsImlhdCI6MTU3ODUwMDQ1NCwiZXhwaXJlc19pbiI6Mjg4MDAsImp0aSI6ImNkMTgxODM3LTdlMmUtNDY1Ny05ZTgwLTk4NWE3ZjVmZDMzYiJ9.SZOd981fC1bdMWehXKsUl0B9vEXRr7-NBKl6IaFIoS573rNjKgcIzChMaxcmc-anOxJqgF8Lan7RdMCIb4Y-zGG3TzfGAG7elpmXJVsogPKCWJlGFCJm_wU-h_cqAcL2llgqnNkkms43lgvyfIdiXv3J-00qBHzMy3jG5mLOE5YZet1LKf3IiRNZxI5Vx6L2Afdox1jiKGQGGt2bNx7-rcYS8VVVZI-ovo7lbbWU6Mi5lWI19q2AS9jGcK5U4hcIU06JzoWGsh-Ob1xkq7VtJKyrOSiUth-SjY5PqQzjvpuEO8MrLWTI0sCaWRHbmbF0bHICGO17bQ42_PfTHgza4A");
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
        try {
            boolean verified = idamService.verifyTokenSignature(idamTokens.idamOauth2Token);
        } catch (RuntimeException rte) {
            // Ignore for the purposes of this test
        }

        verify(mockAppender, times(6)).doAppend(captorLoggingEvent.capture());
        final List<LoggingEvent> loggingEvent = (List<LoggingEvent>) captorLoggingEvent.getAllValues();
        assertThat(loggingEvent.get(0).getFormattedMessage(), is("No cached IDAM token found, requesting from IDAM service."));
        assertThat(loggingEvent.get(1).getFormattedMessage(), is("Requesting idam token"));
        assertThat(loggingEvent.get(2).getFormattedMessage(), is("Passing authorization code to IDAM to get a token"));
        assertThat(loggingEvent.get(3).getFormattedMessage(), is("Requesting idam token successful"));
        assertThat(loggingEvent.get(4).getFormattedMessage(), is("JWKS key loading error"));
        assertThat(loggingEvent.get(5).getFormattedMessage(), is("Token validation error {}"));
    }
}
