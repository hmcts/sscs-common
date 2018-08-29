package uk.gov.hmcts.reform.sscs.idam;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

@RunWith(MockitoJUnitRunner.class)
public class IdamServiceTest {

    @Mock
    private AuthTokenGenerator authTokenGenerator;
    @Mock
    private IdamApiClient idamApiClient;

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
    }

    @Test
    public void shouldReturnAuthTokenGivenNewRequest() {
        String auth = "auth";
        when(authTokenGenerator.generate()).thenReturn(auth);

        String base64Authorisation = Base64.getEncoder().encodeToString("email:pass".getBytes());
        when(idamApiClient.authorizeCodeType("Basic " + base64Authorisation,
                "code",
                "id",
                "redirect/")).thenReturn(authToken);
        when(idamApiClient.authorizeToken(authToken.getCode(),
                "authorization_code",
                "redirect/",
                "id",
                "secret")).thenReturn(authToken);

        UserDetails expectedUserDetails = new UserDetails("16");
        given(idamApiClient.getUserDetails(eq("Bearer " + authToken.getAccessToken()))).willReturn(expectedUserDetails);

        IdamTokens idamTokens = idamService.getIdamTokens();
        assertThat(idamTokens.getServiceAuthorization(), is(auth));
        assertThat(idamTokens.getUserId(), is(expectedUserDetails.getId()));
        assertThat(idamTokens.getIdamOauth2Token(), containsString("Bearer access"));
    }
}