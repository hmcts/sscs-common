package uk.gov.hmcts.reform.sscs.client;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.hmcts.reform.sscs.model.client.JudicialRefDataSearchRequest;
import uk.gov.hmcts.reform.sscs.model.client.JudicialRefDataUsersRequest;
import uk.gov.hmcts.reform.sscs.model.client.JudicialRefDataUsersResponse;

@FeignClient(
        name = "judicial-ref-data-api",
        url = "${judicial-ref.api.url}"
)
public interface JudicialRefDataApi {

    String SERVICE_AUTHORIZATION = "serviceAuthorization";

    @RequestMapping(
            method = RequestMethod.POST,
            value = "refdata/judicial/users/search",
            headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ResponseEntity<Object> searchUsersBySearchString(
            @RequestHeader(AUTHORIZATION) String authorisation,
            @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
            @RequestBody JudicialRefDataSearchRequest judicialRefDataSearchRequest
    );

    @RequestMapping(
            method = RequestMethod.POST,
            value = "refdata/judicial/users",
            headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    JudicialRefDataUsersResponse getJudicialUsers(
            @RequestHeader(AUTHORIZATION) String authorisation,
            @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
            @RequestBody JudicialRefDataUsersRequest judicialRefDataUsersRequest
    );

}
