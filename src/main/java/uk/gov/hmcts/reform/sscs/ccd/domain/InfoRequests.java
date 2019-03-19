package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class InfoRequests {
    @JsonProperty(value = "appellantInfoRequestCollection")
    private List<AppellantInfoRequest> appellantInfoRequest;
}
