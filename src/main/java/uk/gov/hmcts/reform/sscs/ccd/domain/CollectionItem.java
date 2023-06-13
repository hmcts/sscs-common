package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Setter
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Slf4j
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
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CollectionItem) {
            return value.equals(((CollectionItem<?>) object).getValue());
        }

        return value.equals(object);
    }
}
