package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

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
        return value.getHearingDateTime().compareTo(o.getValue().getHearingDateTime());
    }
}
