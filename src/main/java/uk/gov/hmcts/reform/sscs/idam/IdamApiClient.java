package uk.gov.hmcts.reform.sscs.idam;

import org.apache.http.HttpHeaders;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
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
    Authorize authorizeCodeType(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String authorisation,
            @RequestParam("response_type") final String responseType,
            @RequestParam("client_id") final String clientId,
            @RequestParam("redirect_uri") final String redirectUri,
            @RequestBody final String body
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
            @RequestParam("client_secret") final String clientSecret,
            @RequestBody final String body
    );

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/details"
    )
    UserDetails getUserDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) final String oauth2Token);

    @RequestMapping(method = RequestMethod.PATCH,
            value = "/users/{userId}/roles/{roleId}",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    UserDetails assignNewRole(@RequestHeader(HttpHeaders.AUTHORIZATION) final String oauth2Token,
            @RequestParam("userId") final String userId,
            @RequestParam("roleId") final String roleId
    );

}
