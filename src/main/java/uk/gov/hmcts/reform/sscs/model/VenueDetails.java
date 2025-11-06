package uk.gov.hmcts.reform.sscs.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class VenueDetails {
    private String venueId;
    private String threeDigitReference;
    private String regionalProcessingCentre;
    private String venName;
    private String venAddressLine1;
    private String venAddressLine2;
    private String venAddressTown;
    private String venAddressCounty;
    private String venAddressPostcode;
    private String venAddressTelNo;
    private String districtId;
    private String url;
    private String active;
    private String gapsVenName;
    private String legacyVenue;
    private String comments;
    private String epimsId;
}
