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
public class DirectionResponses {

    private String id;
    private DirectionResponsesValue value;

    @JsonCreator
    public DirectionResponses(@JsonProperty("id") String id,
                              @JsonProperty("value") DirectionResponsesValue value) {
        this.id = id;
        this.value = value;
    }
}
