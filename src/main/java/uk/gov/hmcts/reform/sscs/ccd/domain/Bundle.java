package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class Bundle implements Comparable<Bundle> {

    private BundleDetails value;

    @JsonCreator
    public Bundle(@JsonProperty("value") BundleDetails value) {
        this.value = value;
    }

    @Override
    public int compareTo(Bundle o) {
        return value.getDateGenerated().compareTo(o.getValue().getDateGenerated());
    }

}