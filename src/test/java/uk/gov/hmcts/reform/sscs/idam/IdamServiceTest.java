package uk.gov.hmcts.reform.sscs.idam;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;

@ExtendWith(MockitoExtension.class)
public class IdamServiceTest {

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @Mock
    private IdamClient idamClient;

    @Mock
    private Appender mockAppender;

    @Captor
    private ArgumentCaptor captorLoggingEvent;

    private Authorize authToken;
    private IdamService idamService;

    @BeforeEach
    public void setUp() {
        authToken = new Authorize("redirect/", "authCode", "access");
        idamService = new IdamService(authTokenGenerator, idamClient);

        ReflectionTestUtils.setField(idamService, "idamOauth2UserEmail", "email");
        ReflectionTestUtils.setField(idamService, "idamOauth2UserPassword", "pass");

        ReflectionTestUtils.setField(idamService, "idamOauth2WaUserEmail", "email");
        ReflectionTestUtils.setField(idamService, "idamOauth2WaUserPassword", "pass");

        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);
    }

    @AfterEach
    public void teardown() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAppender(mockAppender);
    }

    @Test
    public void shouldReturnAuthTokenGivenNewRequestWithAppropriateLogMessages() {
        String auth = "auth";
        when(authTokenGenerator.generate()).thenReturn(auth);

        when(idamClient.getAccessToken("email", "pass")).thenReturn("Bearer " + authToken.getAccessToken());

        UserInfo expectedUserDetails =
                new UserInfo("16", "16", "dummy@email.com", "Peter", "Pan", new ArrayList<>());

        given(idamClient.getUserInfo(eq("Bearer " + authToken.getAccessToken()))).willReturn(expectedUserDetails);

        IdamTokens idamTokens = idamService.getIdamTokens();
        assertThat(idamTokens.getServiceAuthorization(), is(auth));
        assertThat(idamTokens.getUserId(), is(expectedUserDetails.getUid()));
        assertThat(idamTokens.getEmail(), is(expectedUserDetails.getSub()));
        assertThat(idamTokens.getIdamOauth2Token(), containsString("Bearer access"));

        IdamTokens idamWaTokens = idamService.getIdamWaTokens();
        assertThat(idamWaTokens.getServiceAuthorization(), is(auth));
        assertThat(idamWaTokens.getUserId(), is(expectedUserDetails.getUid()));
        assertThat(idamWaTokens.getEmail(), is(expectedUserDetails.getSub()));
        assertThat(idamWaTokens.getIdamOauth2Token(), containsString("Bearer access"));

        verify(mockAppender, times(10)).doAppend(captorLoggingEvent.capture());
        final List<LoggingEvent> loggingEvent = (List<LoggingEvent>) captorLoggingEvent.getAllValues();

        //Check the message being logged is correct
        assertThat(loggingEvent.get(0).getFormattedMessage(), is("No cached IDAM token found, requesting from IDAM service."));
        assertThat(loggingEvent.get(1).getFormattedMessage(), containsString("Attempting to obtain token, retry attempt"));
        assertThat(loggingEvent.get(2).getFormattedMessage(), is("Requesting idam access token from Open End Point"));
        assertThat(loggingEvent.get(3).getFormattedMessage(), is("Requesting idam access token successful"));
        assertThat(loggingEvent.get(4).getFormattedMessage(), is("requesting user details"));

        assertThat(loggingEvent.get(5).getFormattedMessage(), is("No cached Wa IDAM token found, requesting from IDAM service."));
        assertThat(loggingEvent.get(6).getFormattedMessage(), containsString("Attempting to obtain Wa token, retry attempt"));
        assertThat(loggingEvent.get(7).getFormattedMessage(), is("Requesting Wa idam access token from Open End Point"));
        assertThat(loggingEvent.get(8).getFormattedMessage(), is("Requesting Wa idam access token successful"));
        assertThat(loggingEvent.get(9).getFormattedMessage(), is("requesting user details"));
    }

    @Test
    public void shouldExceptionGivenErrorWithAppropriateLogMessages() {

        when(idamClient.getAccessToken("email", "pass")
        ).thenThrow(new RuntimeException());

        try {
            IdamTokens idamTokens = idamService.getIdamTokens();
        } catch (RuntimeException rte) {
            // Ignore for the purposes of this test
        }

        try {
            IdamTokens idamWaTokens = idamService.getIdamWaTokens();
        } catch (RuntimeException rte) {
            // Ignore for the purposes of this test
        }

        verify(mockAppender, times(8)).doAppend(captorLoggingEvent.capture());
        final List<LoggingEvent> loggingEvent = (List<LoggingEvent>) captorLoggingEvent.getAllValues();

        //Check the message being logged is correct
        assertThat(loggingEvent.get(0).getFormattedMessage(), is("No cached IDAM token found, requesting from IDAM service."));
        assertThat(loggingEvent.get(1).getFormattedMessage(), containsString("Attempting to obtain token, retry attempt"));
        assertThat(loggingEvent.get(2).getFormattedMessage(), is("Requesting idam access token from Open End Point"));
        assertThat(loggingEvent.get(3).getFormattedMessage(), containsString("Requesting idam token failed:"));

        assertThat(loggingEvent.get(4).getFormattedMessage(), is("No cached Wa IDAM token found, requesting from IDAM service."));
        assertThat(loggingEvent.get(5).getFormattedMessage(), containsString("Attempting to obtain Wa token, retry attempt"));
        assertThat(loggingEvent.get(6).getFormattedMessage(), is("Requesting Wa idam access token from Open End Point"));
        assertThat(loggingEvent.get(7).getFormattedMessage(), containsString("Requesting Wa idam token failed:"));
    }

    @Test
    public void shouldReturnCacheToken() {
        String auth = "auth";
        when(authTokenGenerator.generate()).thenReturn(auth);

        when(idamClient.getAccessToken("email", "pass")).thenReturn("Bearer " + authToken.getAccessToken());

        UserInfo expectedUserDetails =
                new UserInfo("16", "16", "dummy@email.com", "Peter", "Pan", new ArrayList<>());

        given(idamClient.getUserInfo(eq("Bearer " + authToken.getAccessToken()))).willReturn(expectedUserDetails);

        // first time
        IdamTokens idamTokens = idamService.getIdamTokens();

        assertThat(idamTokens.getServiceAuthorization(), is(auth));
        assertThat(idamTokens.getUserId(), is(expectedUserDetails.getUid()));
        assertThat(idamTokens.getEmail(), is(expectedUserDetails.getSub()));
        assertThat(idamTokens.getIdamOauth2Token(), containsString("Bearer access"));

        // second time
        idamTokens = idamService.getIdamTokens();

        assertThat(idamTokens.getServiceAuthorization(), is(auth));
        assertThat(idamTokens.getUserId(), is(expectedUserDetails.getUid()));
        assertThat(idamTokens.getEmail(), is(expectedUserDetails.getSub()));
        assertThat(idamTokens.getIdamOauth2Token(), containsString("Bearer access"));

        verify(idamClient, atMostOnce()).getAccessToken("email", "pass");

        // first Wa time
        IdamTokens idamWaTokens = idamService.getIdamWaTokens();

        assertThat(idamWaTokens.getServiceAuthorization(), is(auth));
        assertThat(idamWaTokens.getUserId(), is(expectedUserDetails.getUid()));
        assertThat(idamWaTokens.getEmail(), is(expectedUserDetails.getSub()));
        assertThat(idamWaTokens.getIdamOauth2Token(), containsString("Bearer access"));

        // second time
        idamWaTokens = idamService.getIdamWaTokens();

        assertThat(idamWaTokens.getServiceAuthorization(), is(auth));
        assertThat(idamWaTokens.getUserId(), is(expectedUserDetails.getUid()));
        assertThat(idamWaTokens.getEmail(), is(expectedUserDetails.getSub()));
        assertThat(idamWaTokens.getIdamOauth2Token(), containsString("Bearer access"));

        verify(idamClient, atMost(2)).getAccessToken("email", "pass");

    }
}
