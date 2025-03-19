package uk.gov.hmcts.reform.sscs.client;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.sscs.model.CaseFlag;
import uk.gov.hmcts.reform.sscs.model.Categories;
import uk.gov.hmcts.reform.sscs.model.CategoryRequest;
import uk.gov.hmcts.reform.sscs.model.PanelResponse;

@FeignClient(
    name = "common-ref-data-api",
    url = "${common-ref.api.url:dummy}"
)
public interface CommonReferenceDataApi {
    String SERVICE_AUTHORIZATION = "serviceAuthorization";

    @GetMapping(
            value = "refdata/commondata/lov/categories/defaultPanelCategory",
            headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    PanelResponse getDefaultPanelCategory(
            @RequestHeader(AUTHORIZATION) String authorisation,
            @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
            @RequestParam("serviceId") String serviceId,
            @RequestParam("key") String key
    );

    @GetMapping(
            value = "refdata/commondata/lov/categories/panelCategoryMember",
            headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    PanelResponse getPanelCategoryMember(
            @RequestHeader(AUTHORIZATION) String authorisation,
            @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
            @RequestParam("serviceId") String serviceId,
            @RequestParam("parentCategory") String parentCategory,
            @RequestParam("parentKey") String parentKey
    );
}
