package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.math.NumberUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class HearingOutcome implements Comparable<HearingOutcome> {
    private HearingOutcomeDetails value;

    @JsonCreator
    public HearingOutcome(@JsonProperty("value") HearingOutcomeDetails value) {this.value = value; }

    @Override
    public int compareTo(HearingOutcome o) {
        return new CompareToBuilder()
            .append(NumberUtils.createInteger(this.value.getCompletedHearingId()),
                NumberUtils.createInteger(o.getValue().getCompletedHearingId()))
            .toComparison();
    }
}
