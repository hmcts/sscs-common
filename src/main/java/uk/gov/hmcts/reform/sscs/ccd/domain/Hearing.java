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
    private HearingDetails hearingDetails;
    private HearingRequest requestDetails;

    @JsonCreator
    public Hearing(@JsonProperty("hearingDetails") HearingDetails hearingDetails,
                   @JsonProperty("requestDetails") HearingRequest requestDetails) {
        this.hearingDetails = hearingDetails;
        this.requestDetails = requestDetails;
    }

    @Override
    public int compareTo(Hearing o) {
        return new CompareToBuilder()
                .append(NumberUtils.createInteger(this.hearingDetails.getHearingId()),
                        NumberUtils.createInteger(o.getHearingDetails().getHearingId()))
                .append(this.hearingDetails.getHearingDateTime(), o.getHearingDetails().getHearingDateTime())
                .toComparison();
    }
}
