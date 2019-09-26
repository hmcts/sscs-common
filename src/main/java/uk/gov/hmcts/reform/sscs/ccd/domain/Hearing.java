package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang.StringUtils;

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

        int idCompare = 0;

        if (!StringUtils.isEmpty(o.getValue().getHearingId()) && !StringUtils.isEmpty(value.getHearingId())) {
            idCompare = o.getValue().getHearingId().compareTo(value.getHearingId());
        }

        if (idCompare != 0) {
            return -1 * idCompare;
        }
        return -1 * o.getValue().getHearingDateTime().compareTo(value.getHearingDateTime());
    }
}
