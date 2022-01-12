package uk.gov.hmcts.reform.sscs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourtVenue {
    @JsonProperty("court_venue_id")
    private String courtVenueId;
    @JsonProperty("site_name")
    private String siteName;
    @JsonProperty("court_name")
    private String courtName;
    @JsonProperty("epims_id")
    private String epimsId;
    @JsonProperty("open_for_public")
    private String open_for_public;
    @JsonProperty("court_type_id")
    private String court_type_id;
    @JsonProperty("court_type")
    private String court_type;
    @JsonProperty("region_id")
    private String region_id;
    @JsonProperty("region")
    private String region;
    @JsonProperty("cluster_id")
    private String cluster_id;
    @JsonProperty("cluster_name")
    private String cluster_name;
    @JsonProperty("court_status")
    private String court_status;
    @JsonProperty("court_open_date")
    private String court_open_date;
    @JsonProperty("closed_date")
    private String closed_date;
    @JsonProperty("postcode")
    private String postcode;
    @JsonProperty("court_address")
    private String court_address;
    @JsonProperty("phone_number")
    private String phone_number;
    @JsonProperty("court_location_code")
    private String court_location_code;
    @JsonProperty("dx_address")
    private String dx_address;
    @JsonProperty("welsh_site_name")
    private String welsh_site_name;
    @JsonProperty("welsh_court_address")
    private String welsh_court_address;
    @JsonProperty("venue_name")
    private String venue_name;
    @JsonProperty("is_case_management_location")
    private String is_case_management_location;
    @JsonProperty("is_hearing_location")
    private String is_hearing_location;
}
