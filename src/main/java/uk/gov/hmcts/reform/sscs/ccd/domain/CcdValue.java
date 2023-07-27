package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.requireNonNull;

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
public class CcdValue<T> {
    T value;

    @JsonCreator
    public CcdValue(@JsonProperty("value") T value) {
        requireNonNull(value);
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CcdValue) {
            return value.equals(((CcdValue<?>) object).getValue());
        }

        return value.equals(object);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
