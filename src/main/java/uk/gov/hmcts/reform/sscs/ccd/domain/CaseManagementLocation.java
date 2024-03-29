package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class CaseManagementLocation {
    private String region;
    private String baseLocation;

    @JsonCreator
    public CaseManagementLocation(@JsonProperty("region")String region,
                                  @JsonProperty("baseLocation")String baseLocation) {
        this.region = region;
        this.baseLocation = baseLocation;
    }
}
