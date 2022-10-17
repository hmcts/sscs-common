package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaseLink {
    private CaseLinkDetails value;

    @JsonCreator
    public CaseLink(@JsonProperty("value") CaseLinkDetails value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        CaseLink c = (CaseLink) o;

        return value.getCaseReference().equals(c.value.getCaseReference());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
