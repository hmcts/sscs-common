package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class Venue {
    @CCD(label = "Venue Name")
    private String name;
    @CCD(label = "Venue Address")
    private Address address;
    @CCD(label = "Venue Map Link")
    private String googleMapLink;

    @JsonCreator
    public Venue(@JsonProperty("name") String name,
                   @JsonProperty("address") Address address,
                   @JsonProperty("googleMapLink") String googleMapLink) {
        this.name = name;
        this.address = address;
        this.googleMapLink = googleMapLink;
    }
}
