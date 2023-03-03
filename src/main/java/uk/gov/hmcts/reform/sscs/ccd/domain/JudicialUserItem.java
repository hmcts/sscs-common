package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.builder.CompareToBuilder;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUserBase;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JudicialUserItem implements Comparable<JudicialUserItem> {

    JudicialUserBase value;

    @JsonCreator
    public JudicialUserItem(@JsonProperty("value") JudicialUserBase value) {
        this.value = value;
    }

    @Override
    public int compareTo(JudicialUserItem o) {
        return new CompareToBuilder()
            .append(this.value.getPersonalCode(), o.getValue().getPersonalCode())
            .toComparison();
    }
}
