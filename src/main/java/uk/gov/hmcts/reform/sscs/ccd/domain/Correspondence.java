package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Delegate;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Correspondence implements Comparable<Correspondence> {
    @CCD(ignore = true)
    @Delegate
    private CorrespondenceDetails value;

    @JsonCreator
    public Correspondence(@JsonProperty("value") CorrespondenceDetails value) {
        this.value = value;
    }

    @Override
    public int compareTo(Correspondence o) {
        return value.getSentOnDateTime().compareTo(o.getValue().getSentOnDateTime());
    }

}
