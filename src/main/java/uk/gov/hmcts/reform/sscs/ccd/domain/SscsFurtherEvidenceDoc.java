package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscsFurtherEvidenceDoc {

    private SscsFurtherEvidenceDocDetails value;

    @JsonCreator
    public SscsFurtherEvidenceDoc(@JsonProperty("value") SscsFurtherEvidenceDocDetails value) {
        this.value = value;
    }

}
