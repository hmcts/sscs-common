package uk.gov.hmcts.reform.sscs.idam;

import feign.Body;
import feign.Headers;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "idam-api", url = "${idam.url}")
public interface IdamApiClient {

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/oauth2/authorize",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    @Body("")
    Authorize authorizeCodeType(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String authorisation,
            @RequestHeader(HttpHeaders.CONTENT_LENGTH) final String contentLength,
            @RequestParam("response_type") final String responseType,
            @RequestParam("client_id") final String clientId,
            @RequestParam("redirect_uri") final String redirectUri
    );

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/oauth2/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    Authorize authorizeToken(
            @RequestParam("code") final String code,
            @RequestParam("grant_type") final String grantType,
            @RequestParam("redirect_uri") final String redirectUri,
            @RequestParam("client_id") final String clientId,
            @RequestParam("client_secret") final String clientSecret
    );

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/details"
    )
    UserDetails getUserDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) final String oauth2Token);

}
