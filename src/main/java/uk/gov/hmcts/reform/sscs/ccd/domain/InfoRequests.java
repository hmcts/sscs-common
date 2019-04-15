package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class InfoRequests {
    private List<AppellantInfoRequest> appellantInfoRequest;

    @JsonCreator
    public InfoRequests(@JsonProperty(
        value = "appellantInfoRequestCollection") List<AppellantInfoRequest> appellantInfoRequest
    ) {
        this.appellantInfoRequest = appellantInfoRequest;
    }
}
