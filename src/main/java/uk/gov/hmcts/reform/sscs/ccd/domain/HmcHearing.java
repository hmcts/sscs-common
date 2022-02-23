package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ComparisonChain;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HmcHearing implements Comparable<HmcHearing> {

    private HmcHearingDetails value;

    @JsonCreator
    public HmcHearing(@JsonProperty("value") HmcHearingDetails value) {
        this.value = value;
    }

    @Override
    public int compareTo(HmcHearing o) {
        return ComparisonChain.start()
                .compare(this.value.getHearingRequest().getInitialRequestTimestamp(),
                        o.getValue().getHearingRequest().getInitialRequestTimestamp())
                .result();
    }

}
