package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionalProcessingCenter {

    @JsonCreator
    public RegionalProcessingCenter(@JsonProperty("faxNumber") String faxNumber,
                                    @JsonProperty("address4") String address4,
                                    @JsonProperty("phoneNumber") String phoneNumber,
                                    @JsonProperty("name") String name,
                                    @JsonProperty("address1") String address1,
                                    @JsonProperty("address2") String address2,
                                    @JsonProperty("address3") String address3,
                                    @JsonProperty("postcode") String postcode,
                                    @JsonProperty("city") String city,
                                    @JsonProperty("email") String email,
                                    @JsonProperty("hearingRoute") HearingRoute hearingRoute) {
        this.faxNumber = faxNumber;
        this.address4 = address4;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.postcode = postcode;
        this.city = city;
        this.email = email;
        this.hearingRoute = hearingRoute;
    }

    String faxNumber;

    String address4;

    String phoneNumber;

    String name;

    String address1;

    String address2;

    String address3;

    String postcode;

    String city;

    String email;

    HearingRoute hearingRoute;
}
