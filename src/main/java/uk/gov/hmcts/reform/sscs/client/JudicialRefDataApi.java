package uk.gov.hmcts.reform.sscs.client;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.hmcts.reform.sscs.model.client.JudicialRefDataSearchRequest;
import uk.gov.hmcts.reform.sscs.model.client.JudicialRefDataUsersRequest;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUser;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUserSearch;

@FeignClient(
        name = "judicial-ref-data-api",
        url = "${judicial-ref.api.url}"
)
public interface JudicialRefDataApi {

    String SERVICE_AUTHORIZATION = "serviceAuthorization";
    String ACCEPT_HEADER_STRING = "application/vnd.jrd.api+json;Version=2.0";

    @RequestMapping(
            method = RequestMethod.POST,
            value = "refdata/judicial/users/search",
            consumes = APPLICATION_JSON_VALUE,
            headers = ACCEPT + "=" + ACCEPT_HEADER_STRING
    )
    List<JudicialUserSearch> searchUsersBySearchString(
            @RequestHeader(AUTHORIZATION) String authorisation,
            @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
            @RequestBody JudicialRefDataSearchRequest judicialRefDataSearchRequest
    );

    @RequestMapping(
            method = RequestMethod.POST,
            value = "refdata/judicial/users",
            consumes = APPLICATION_JSON_VALUE,
            headers = ACCEPT + "=" + ACCEPT_HEADER_STRING
    )
    List<JudicialUser> getJudicialUsers(
            @RequestHeader(AUTHORIZATION) String authorisation,
            @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
            @RequestBody JudicialRefDataUsersRequest judicialRefDataUsersRequest
    );

}
