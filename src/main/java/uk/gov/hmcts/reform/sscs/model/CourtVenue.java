package uk.gov.hmcts.reform.sscs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class CourtVenue {
    @JsonProperty("court_venue_id")
    private String courtVenueId;
    @JsonProperty("site_name")
    private String siteName;
    @JsonProperty("court_name")
    private String courtName;
    @JsonProperty("epimms_id")
    private String epimsId;
    @JsonProperty("open_for_public")
    private String openForPublic;
    @JsonProperty("court_type_id")
    private String courtTypeId;
    @JsonProperty("court_type")
    private String courtType;
    @JsonProperty("region_id")
    private String regionId;
    @JsonProperty("region")
    private String region;
    @JsonProperty("cluster_id")
    private String clusterId;
    @JsonProperty("cluster_name")
    private String clusterName;
    @JsonProperty("court_status")
    private String courtStatus;
    @JsonProperty("court_open_date")
    private String courtOpenDate;
    @JsonProperty("closed_date")
    private String closedDate;
    @JsonProperty("postcode")
    private String postcode;
    @JsonProperty("court_address")
    private String courtAddress;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("court_location_code")
    private String courtLocationCode;
    @JsonProperty("dx_address")
    private String dxAddress;
    @JsonProperty("welsh_site_name")
    private String welshSiteName;
    @JsonProperty("welsh_court_address")
    private String welshCourtAddress;
    @JsonProperty("venue_name")
    private String venueName;
    @JsonProperty("is_case_management_location")
    private String isCaseManagementLocation;
    @JsonProperty("is_hearing_location")
    private String isHearingLocation;
}
