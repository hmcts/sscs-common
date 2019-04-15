package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class AppellantInfoRequest {
    private AppellantInfo appellantInfo;
    private String id;

    @JsonCreator
    public AppellantInfoRequest(
        @JsonProperty(value = "value") AppellantInfo appellantInfo,
        @JsonProperty(value = "id") String id
    ) {
        this.appellantInfo = appellantInfo;
        this.id = id;
    }
}
