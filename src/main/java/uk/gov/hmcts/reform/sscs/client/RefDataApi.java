package uk.gov.hmcts.reform.sscs.client;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;


@FeignClient(
        name = "location-ref-data-api",
        url = "${location_ref.api.url}"
)
public interface RefDataApi {
    String SERVICE_AUTHORIZATION = "serviceAuthorization";

    @GetMapping(
        value = "refdata/location/court-venues",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    List<CourtVenue> courtVenues(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestParam("court_type_id") String courtTypeId
    );

    @GetMapping(
        value = "refdata/location/court-venues",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    List<CourtVenue> courtVenueByEpimsId(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestParam("epimms_id") String epimsId
    );

}
