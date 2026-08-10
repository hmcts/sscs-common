package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class CaseManagementLocation {
    @CCD(label = "Region")
    private String region;
    @CCD(label = "Base Location")
    private String baseLocation;

    @JsonCreator
    public CaseManagementLocation(@JsonProperty("region")String region,
                                  @JsonProperty("baseLocation")String baseLocation) {
        this.region = region;
        this.baseLocation = baseLocation;
    }
}
