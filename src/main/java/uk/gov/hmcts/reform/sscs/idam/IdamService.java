package uk.gov.hmcts.reform.sscs.idam;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;

@Service
@Slf4j
public class IdamService {

    public static final int ONE_HOUR = 1000 * 60 * 60;

    private final AuthTokenGenerator authTokenGenerator;

    private final IdamClient idamClient;

    private final AtomicInteger atomicInteger = new AtomicInteger(1);

    @Value("${idam.oauth2.user.email}")
    private String idamOauth2UserEmail;

    @Value("${idam.oauth2.user.password}")
    private String idamOauth2UserPassword;

    @Value("${idam.oauth2.waUser.email}")
    private String idamOauth2WaUserEmail;

    @Value("${idam.oauth2.waUser.password}")
    private String idamOauth2WaUserPassword;

    // Tactical idam token caching solution implemented
    // SSCS-5895 - will deliver the strategic caching solution
    private String cachedToken;

    @Autowired
    IdamService(AuthTokenGenerator authTokenGenerator, IdamClient idamClient) {
        this.authTokenGenerator = authTokenGenerator;
        this.idamClient = idamClient;
    }

    @Retryable
    public String generateServiceAuthorization() {
        return authTokenGenerator.generate();
    }

    @Retryable
    public String getUserId(String oauth2Token) {
        log.info("get user Id");
        return idamClient.getUserDetails(oauth2Token).getId();
    }

    @Retryable
    public UserDetails getUserDetails(String oauth2Token)  {
        log.info("requesting user details");
        UserInfo userInfo = idamClient.getUserInfo(oauth2Token);
        return new UserDetailsTransformer(userInfo).asLocalUserDetails();
    }

    @Retryable
    public String getIdamOauth2Token() {
        cachedToken = getOpenAccessToken();
        return cachedToken;
    }

    @Retryable
    public String getOpenAccessToken() {
        try {
            log.info("Requesting idam access token from Open End Point");
            String accessToken = idamClient.getAccessToken(idamOauth2UserEmail, idamOauth2UserPassword);
            log.info("Requesting idam access token successful");
            return accessToken;
        } catch (Exception e) {
            log.error("Requesting idam token failed: " + e.getMessage());
            throw e;
        }
    }

    @Retryable
    public String getOpenAccessTokenForWaUser() {
        try {
            log.info("Requesting idam access token from Open End Point");
            String accessToken = idamClient.getAccessToken(idamOauth2WaUserEmail, idamOauth2WaUserPassword);
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

    @Retryable(backoff = @Backoff(delay = 15000L, multiplier = 1.0, random = true))
    public IdamTokens getIdamTokens() {
        String idamOauth2Token;

        if (StringUtils.isEmpty(cachedToken)) {
            log.info("No cached IDAM token found, requesting from IDAM service.");
            log.info("Attempting to obtain token, retry attempt {}", atomicInteger.getAndIncrement());
            idamOauth2Token =  getIdamOauth2Token();
        } else {
            atomicInteger.set(1);
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
