package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class HearingOutcome {
    private HearingOutcomeDetails value;

    @JsonCreator
    public HearingOutcome(@JsonProperty("value") HearingOutcomeDetails value) {this.value = value; }

}
