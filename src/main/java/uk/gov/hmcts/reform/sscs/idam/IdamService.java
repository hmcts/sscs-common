package uk.gov.hmcts.reform.sscs.idam;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.factories.DefaultJWSVerifierFactory;
import com.nimbusds.jose.jwk.AsymmetricJWK;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.SecretJWK;
import com.nimbusds.jose.proc.JWSVerifierFactory;
import com.nimbusds.jwt.SignedJWT;
import java.net.URL;
import java.security.Key;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

@Service
@Slf4j
public class IdamService {
    public static final int ONE_HOUR = 1000 * 60 * 60;

    private final AuthTokenGenerator authTokenGenerator;
    private final IdamApiClient idamApiClient;
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

    @Value("${idam.oauth2.jwkUrl}")
    private String idamJwkUrl;

    // Tactical idam token caching solution implemented
    // SSCS-5895 - will deliver the strategic caching solution
    private String cachedToken;

    @Autowired
    IdamService(AuthTokenGenerator authTokenGenerator, IdamApiClient idamApiClient) {
        this.authTokenGenerator = authTokenGenerator;
        this.idamApiClient = idamApiClient;
        this.jwsVerifierFactory = new DefaultJWSVerifierFactory();
    }

    public String generateServiceAuthorization() {
        return authTokenGenerator.generate();
    }

    @Retryable
    public String getUserId(String oauth2Token) {
        return idamApiClient.getUserDetails(oauth2Token).getId();
    }

    public UserDetails getUserDetails(String oauth2Token)  {
        return idamApiClient.getUserDetails(oauth2Token);
    }

    public String getIdamOauth2Token() {

        try {
            log.info("Requesting idam token");
            String authorisation = idamOauth2UserEmail + ":" + idamOauth2UserPassword;
            String base64Authorisation = Base64.getEncoder().encodeToString(authorisation.getBytes());

            Authorize authorize = idamApiClient.authorizeCodeType(
                "Basic " + base64Authorisation,
                "code",
                idamOauth2ClientId,
                idamOauth2RedirectUrl,
                " "
            );

            log.info("Passing authorization code to IDAM to get a token");

            Authorize authorizeToken = idamApiClient.authorizeToken(
                authorize.getCode(),
                "authorization_code",
                idamOauth2RedirectUrl,
                idamOauth2ClientId,
                idamOauth2ClientSecret,
                " "
            );

            cachedToken = "Bearer " + authorizeToken.getAccessToken();

            log.info("Requesting idam token successful");

            return cachedToken;
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

        return IdamTokens.builder()
                .idamOauth2Token(idamOauth2Token)
                .serviceAuthorization(generateServiceAuthorization())
                .userId(getUserId(idamOauth2Token))
                .build();
    }

    public boolean verifyTokenSignature(String token) {
        try {
            String tokenTocheck = StringUtils.replace(token, "Bearer ", "");
            SignedJWT signedJwt = SignedJWT.parse(tokenTocheck);

            JWKSet jsonWebKeySet = loadJsonWebKeySet(idamJwkUrl);

            JWSHeader jwsHeader = signedJwt.getHeader();
            Key key = findKeyById(jsonWebKeySet, jwsHeader.getKeyID());

            JWSVerifier jwsVerifier = jwsVerifierFactory.createJWSVerifier(jwsHeader, key);

            return signedJwt.verify(jwsVerifier);
        } catch (Exception e) {
            log.error("Token validation error {}", e);
            return false;
        }
    }

    private JWKSet loadJsonWebKeySet(String jwksUrl) {
        try {
            return JWKSet.load(new URL(jwksUrl));
        } catch (Exception e) {
            log.error("JWKS key loading error");
            throw new RuntimeException("JWKS error", e);
        }
    }

    private Key findKeyById(JWKSet jsonWebKeySet, String keyId) {
        try {
            JWK jsonWebKey = jsonWebKeySet.getKeyByKeyId(keyId);
            if (jsonWebKey == null) {
                throw new RuntimeException("JWK does not exist in the key set");
            }
            if (jsonWebKey instanceof SecretJWK) {
                return ((SecretJWK) jsonWebKey).toSecretKey();
            }
            if (jsonWebKey instanceof AsymmetricJWK) {
                return ((AsymmetricJWK) jsonWebKey).toPublicKey();
            }
            throw new RuntimeException("Unsupported JWK " + jsonWebKey.getClass().getName());
        } catch (JOSEException e) {
            log.error("Invalid JWK key");
            throw new RuntimeException("Invalid JWK", e);
        }
    }
}
