package uk.gov.hmcts.reform.sscscorbackend.service.idam;

import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

@Service
@Slf4j
public class IdamService {

    private final AuthTokenGenerator authTokenGenerator;
    private final IdamApiClient idamApiClient;

    @Value("${idam.oauth2.user.email}")
    private String idamOauth2UserEmail;

    @Value("${idam.oauth2.user.password}")
    private String idamOauth2UserPassword;

    @Value("${idam.oauth2.client.id}")
    private String idamOauth2ClientId;

    @Value("${idam.oauth2.client.secret}")
    private String idamOauth2ClientSecret;

    @Value("${idam.oauth2.redirectUrl}")
    private String idamOauth2RedirectUrl;

    @Autowired
    IdamService(AuthTokenGenerator authTokenGenerator, IdamApiClient idamApiClient) {
        this.authTokenGenerator = authTokenGenerator;
        this.idamApiClient = idamApiClient;
    }

    private String generateServiceAuthorization() {
        return authTokenGenerator.generate();
    }

    @Retryable
    private String getUserId(String oauth2Token) {
        return idamApiClient.getUserDetails(oauth2Token).getId();
    }

    private String getIdamOauth2Token() {
        String authorisation = idamOauth2UserEmail + ":" + idamOauth2UserPassword;
        String base64Authorisation = Base64.getEncoder().encodeToString(authorisation.getBytes());

        Authorize authorize = idamApiClient.authorizeCodeType(
                "Basic " + base64Authorisation,
                "code",
                idamOauth2ClientId,
                idamOauth2RedirectUrl
        );

        Authorize authorizeToken = idamApiClient.authorizeToken(
                authorize.getCode(),
                "authorization_code",
                idamOauth2RedirectUrl,
                idamOauth2ClientId,
                idamOauth2ClientSecret
        );

        return "Bearer " + authorizeToken.getAccessToken();
    }

    public IdamTokens getIdamTokens() {
        return IdamTokens.builder()
                .idamOauth2Token(getIdamOauth2Token())
                .serviceAuthorization(generateServiceAuthorization())
                .userId(getUserId(getIdamOauth2Token()))
                .build();
    }
}
