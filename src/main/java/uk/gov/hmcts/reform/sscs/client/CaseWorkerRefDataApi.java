package uk.gov.hmcts.reform.sscs.client;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.sscs.model.CaseWorkersProfileCreationRequest;
import uk.gov.hmcts.reform.sscs.model.ServiceRoleMapping;
import uk.gov.hmcts.reform.sscs.model.UserRequest;

import javax.validation.constraints.Size;


@FeignClient(
    name = "caseworker-ref-data-api",
    url = "${caseworker-ref.api.url}"
)
public interface CaseWorkerRefDataApi {
    String SERVICE_AUTHORIZATION = "serviceAuthorization";

    @RequestMapping(
        method = RequestMethod.POST,
        value = "refdata/case-worker/upload-file",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ResponseEntity<Object> caseWorkerFileUpload(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestParam("file") @Size(max = 1) MultipartFile file
    );


    @RequestMapping(
        method = RequestMethod.POST,
        value = "refdata/case-worker/users",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ResponseEntity<Object> createCaseWorkerProfiles(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestBody List<CaseWorkersProfileCreationRequest>
            caseWorkersProfileCreationRequest
    );


    @RequestMapping(
        method = RequestMethod.POST,
        value = "refdata/case-worker/idam-roles-mapping",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ResponseEntity<Object> buildIdamRoleMappings(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestBody List<ServiceRoleMapping> serviceRoleMappings
    );


    @RequestMapping(
        method = RequestMethod.POST,
        value = "refdata/case-worker/users/fetchUsersById",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    ResponseEntity<Object> fetchCaseworkersById(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestBody UserRequest userRequest
    );
}
