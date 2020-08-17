package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
public class ElementDisputedDetails {

    private String issueCode;
    private String outcome;

    @JsonCreator
    public ElementDisputedDetails(@JsonProperty("issueCode") String issueCode,
                                  @JsonProperty("outcome") String outcome) {
        this.issueCode = issueCode;
        this.outcome = outcome;
    }
}
