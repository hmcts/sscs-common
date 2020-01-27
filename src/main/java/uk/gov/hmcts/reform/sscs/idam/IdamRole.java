package uk.gov.hmcts.reform.sscs.idam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdamRole {
    String name;

    public IdamRole(@JsonProperty("name") String name) {
        this.name = name;
    }
}
