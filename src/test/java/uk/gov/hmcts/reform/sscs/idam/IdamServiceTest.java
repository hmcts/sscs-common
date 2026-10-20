package uk.gov.hmcts.reform.sscs.idam;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
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
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;

@ExtendWith(MockitoExtension.class)
class IdamServiceTest {

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @Mock
    private IdamClient idamClient;

    @Mock
    private Appender<ILoggingEvent> mockAppender;

    @Captor
    private ArgumentCaptor<ILoggingEvent> captorLoggingEvent;

    private Authorize authToken;
    private IdamService idamService;

    @BeforeEach
    void setUp() {
        authToken = new Authorize("redirect/", "authCode", "access");
        idamService = new IdamService(authTokenGenerator, idamClient);

        setField(idamService, "idamOauth2UserEmail", "email");
        setField(idamService, "idamOauth2UserPassword", "pass");

        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);
    }

    @AfterEach
    void teardown() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAppender(mockAppender);
    }

    @Test
    void shouldReturnAuthTokenGivenNewRequestWithAppropriateLogMessages() {
        final String auth = "auth";
        when(authTokenGenerator.generate()).thenReturn(auth);

        when(idamClient.getAccessToken("email", "pass")).thenReturn("Bearer " + authToken.getAccessToken());

        final UserInfo expectedUserDetails =
                new UserInfo("16", "16", "dummy@email.com", "Peter", "Pan", new ArrayList<>());

        given(idamClient.getUserInfo("Bearer " + authToken.getAccessToken())).willReturn(expectedUserDetails);

        final IdamTokens idamTokens = idamService.getIdamTokens();
        assertIdamTokens(idamTokens, expectedUserDetails);

        verify(mockAppender, times(5)).doAppend(captorLoggingEvent.capture());
        List<ILoggingEvent> loggingEvent = captorLoggingEvent.getAllValues();

        assertSoftly(softly -> {
            softly.assertThat(loggingEvent.get(0).getFormattedMessage()).isEqualTo("No cached IDAM token found, requesting from IDAM service.");
            softly.assertThat(loggingEvent.get(1).getFormattedMessage()).contains("Attempting to obtain token, retry attempt");
            softly.assertThat(loggingEvent.get(2).getFormattedMessage()).isEqualTo("Requesting idam access token from Open End Point");
            softly.assertThat(loggingEvent.get(3).getFormattedMessage()).isEqualTo("Requesting idam access token successful");
            softly.assertThat(loggingEvent.get(4).getFormattedMessage()).isEqualTo("requesting user details");
        });
    }

    @Test
    void shouldExceptionGivenErrorWithAppropriateLogMessages() {
        when(idamClient.getAccessToken("email", "pass")).thenThrow(new RuntimeException());

        assertThatThrownBy(() -> idamService.getIdamTokens()).isInstanceOf(RuntimeException.class);

        verify(mockAppender, times(4)).doAppend(captorLoggingEvent.capture());
        List<ILoggingEvent> loggingEvent = captorLoggingEvent.getAllValues();

        assertSoftly(softly -> {
            softly.assertThat(loggingEvent.get(0).getFormattedMessage()).isEqualTo("No cached IDAM token found, requesting from IDAM service.");
            softly.assertThat(loggingEvent.get(1).getFormattedMessage()).contains("Attempting to obtain token, retry attempt");
            softly.assertThat(loggingEvent.get(2).getFormattedMessage()).isEqualTo("Requesting idam access token from Open End Point");
            softly.assertThat(loggingEvent.get(3).getFormattedMessage()).contains("Requesting idam token failed:");
        });
    }

    @Test
    void shouldReturnCacheToken() {
        final String auth = "auth";
        when(authTokenGenerator.generate()).thenReturn(auth);

        when(idamClient.getAccessToken("email", "pass")).thenReturn("Bearer " + authToken.getAccessToken());

        final UserInfo expectedUserDetails =
                new UserInfo("16", "16", "dummy@email.com", "Peter", "Pan", new ArrayList<>());

        given(idamClient.getUserInfo("Bearer " + authToken.getAccessToken())).willReturn(expectedUserDetails);

        assertIdamTokens(idamService.getIdamTokens(), expectedUserDetails);
        assertIdamTokens(idamService.getIdamTokens(), expectedUserDetails);

        verify(idamClient, atMostOnce()).getAccessToken("email", "pass");
    }

    @Test
    void shouldReturnUserIdFromUserInfoUid() {
        final String oauth2Token = "Bearer token";
        final UserInfo userInfo = UserInfo.builder()
                .sub("dummy@email.com")
                .uid("user-id-123")
                .build();
        given(idamClient.getUserInfo(oauth2Token)).willReturn(userInfo);

        final String userId = idamService.getUserId(oauth2Token);

        assertThat(userId).isEqualTo("user-id-123");
        verify(idamClient).getUserInfo(oauth2Token);
    }

    private void assertIdamTokens(final IdamTokens idamTokens, final UserInfo expectedUserDetails) {
        assertSoftly(softly -> {
            softly.assertThat(idamTokens.getServiceAuthorization()).isEqualTo("auth");
            softly.assertThat(idamTokens.getUserId()).isEqualTo(expectedUserDetails.getUid());
            softly.assertThat(idamTokens.getEmail()).isEqualTo(expectedUserDetails.getSub());
            softly.assertThat(idamTokens.getIdamOauth2Token()).contains("Bearer access");
        });
    }
}
