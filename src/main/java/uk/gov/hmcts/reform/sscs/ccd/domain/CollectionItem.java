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
public class CollectionItem<V> {

    String id;
    V value;

    @JsonCreator
    public CollectionItem(@JsonProperty("id") String id,
        @JsonProperty("value") V value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof JudicialUserItem) {
            return value.equals(((JudicialUserItem) object).getValue());
        } else {
            return object == this;
        }
    }
}
