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
    private String cachedWaToken;

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
        cachedToken = getOpenAccessToken(idamOauth2UserEmail, idamOauth2UserPassword);
        return cachedToken;
    }

    @Retryable
    public String getWaIdamOauth2Token() {
        cachedWaToken = getOpenAccessToken(idamOauth2WaUserEmail, idamOauth2WaUserPassword);
        return cachedWaToken;
    }

    @Retryable
    private String getOpenAccessToken(String userEmail, String userPassword) {
        try {
            log.info("Requesting idam access token from Open End Point");
            String accessToken = idamClient.getAccessToken(userEmail, userPassword);
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
        cachedWaToken = null;
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

    @Retryable(backoff = @Backoff(delay = 15000L, multiplier = 1.0, random = true))
    public IdamTokens getIdamWaTokens() {
        String WaIdamOauth2Token;

        if (StringUtils.isEmpty(cachedWaToken)) {
            log.info("No cached Wa IDAM token found, requesting from IDAM service.");
            log.info("Attempting to obtain Wa token, retry attempt {}", atomicInteger.getAndIncrement());
            WaIdamOauth2Token =  getWaIdamOauth2Token();
        } else {
            atomicInteger.set(1);
            log.info("Using cached Wa IDAM token.");
            WaIdamOauth2Token =  cachedWaToken;
        }

        UserDetails waUserDetails = getUserDetails(WaIdamOauth2Token);

        return IdamTokens.builder()
                .idamOauth2Token(WaIdamOauth2Token)
                .serviceAuthorization(generateServiceAuthorization())
                .userId(waUserDetails.getId())
                .email(waUserDetails.getEmail())
                .roles(waUserDetails.getRoles())
                .build();
    }
}
