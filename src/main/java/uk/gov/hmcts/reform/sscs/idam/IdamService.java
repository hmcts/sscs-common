package uk.gov.hmcts.reform.sscs.idam;

import com.nimbusds.jose.crypto.factories.DefaultJWSVerifierFactory;
import com.nimbusds.jose.proc.JWSVerifierFactory;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.idam.client.IdamClient;

@Service
@Slf4j
public class IdamService {

    public static final int ONE_HOUR = 1000 * 60 * 60;

    private final AuthTokenGenerator authTokenGenerator;

    private final IdamClient sharedIdamClient;

    private final JWSVerifierFactory jwsVerifierFactory;

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

    // Tactical idam token caching solution implemented
    // SSCS-5895 - will deliver the strategic caching solution
    private String cachedToken;

    @Autowired
    IdamService(AuthTokenGenerator authTokenGenerator, IdamClient sharedIdamClient) {
        this.authTokenGenerator = authTokenGenerator;
        this.sharedIdamClient = sharedIdamClient;
        this.jwsVerifierFactory = new DefaultJWSVerifierFactory();
    }

    public String generateServiceAuthorization() {
        return authTokenGenerator.generate();
    }

    @Retryable
    public String getUserId(String oauth2Token) {

        return sharedIdamClient.getUserDetails(oauth2Token).getId();
    }

    public UserDetails getUserDetails(String oauth2Token)  {
        uk.gov.hmcts.reform.idam.client.models.UserDetails userDetails = sharedIdamClient.getUserDetails(oauth2Token);
        return new UserDetailsTransformer(userDetails).asLocalUserDetails();
    }

    public String getIdamOauth2Token() {
        return getOpenAccessToken();
    }

    public String getOpenAccessToken() {
        try {
            log.info("Requesting idam access token from Open End Point");
            String accessToken = sharedIdamClient.getAccessToken(idamOauth2UserEmail, idamOauth2UserPassword);
            log.info("Requesting idam access token successful");
            return accessToken;
        } catch (Exception e) {
            log.error("Requesting idam token failed: " + e.getMessage());
            throw e;
        }
    }

    @Scheduled(fixedRate = ONE_HOUR)
    public void evictCacheAtIntervals() {
        log.info("Evicting idam token cache");
        cachedToken = null;
    }

    public IdamTokens getIdamTokens() {

        String idamOauth2Token;

        if (StringUtils.isEmpty(cachedToken)) {
            log.info("No cached IDAM token found, requesting from IDAM service.");
            idamOauth2Token =  getIdamOauth2Token();
        } else {
            log.info("Using cached IDAM token.");
            idamOauth2Token =  cachedToken;
        }

        UserDetails userDetails = getUserDetails(idamOauth2Token);

        return IdamTokens.builder()
                .idamOauth2Token(idamOauth2Token)
                .serviceAuthorization(generateServiceAuthorization())
                .userId(userDetails.getId())
                .email(userDetails.getEmail())
                .roles(userDetails.getRoles())
                .build();
    }
}
