package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
public class ElementDisputed {

    private ElementDisputedDetails value;

    @JsonCreator
    public ElementDisputed(@JsonProperty("value") ElementDisputedDetails value) {
        this.value = value;
    }
}
