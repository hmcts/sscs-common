package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

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
                                    @JsonProperty("hearingRoute") HearingRoute hearingRoute,
                                    @JsonProperty("epimsId") String epimsId) {
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
        this.epimsId = epimsId;
    }

    @CCD(label = "Fax Number")
    String faxNumber;

    @CCD(label = "Address Line 4")
    String address4;

    @CCD(label = "Phone Number")
    String phoneNumber;

    @CCD(label = "Regional Processing Center Name")
    String name;

    @CCD(label = "Address Line 1")
    String address1;

    @CCD(label = "Address Line 2")
    String address2;

    @CCD(label = "Address Line 3")
    String address3;

    @CCD(label = "Postcode")
    String postcode;

    @CCD(label = "City")
    String city;

    @CCD(label = "Email")
    String email;

    @CCD(label = "Hearing Route", typeOverride = FieldType.FixedList, typeParameterOverride = "FL_hearingRoute")
    HearingRoute hearingRoute;

    @CCD(label = "Epims ID")
    String epimsId;
}
