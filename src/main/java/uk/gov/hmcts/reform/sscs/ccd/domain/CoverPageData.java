package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class CoverPageData {

    private String firstTierAgencyFullName;
    private String firstTierAgencyGroup;

    @JsonCreator
    public CoverPageData(@JsonProperty("firstTierAgencyFullName") String firstTierAgencyFullName,
                         @JsonProperty("firstTierAgencyGroup") String firstTierAgencyGroup) {
        this.firstTierAgencyFullName = firstTierAgencyFullName;
        this.firstTierAgencyGroup = firstTierAgencyGroup;
    }
}
