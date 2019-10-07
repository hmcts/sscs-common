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
public class Hearing implements Comparable<Hearing> {
    private HearingDetails value;

    @JsonCreator
    public Hearing(@JsonProperty("value") HearingDetails value) {
        this.value = value;
    }

    @Override
    public int compareTo(Hearing o) {
        return new CompareToBuilder()
                .append(NumberUtils.createInteger(this.value.getHearingId()),
                        NumberUtils.createInteger(o.getValue().getHearingId()))
                .append(this.value.getHearingDateTime(), o.getValue().getHearingDateTime())
                .toComparison();
    }
}
