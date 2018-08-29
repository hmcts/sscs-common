package uk.gov.hmcts.reform.sscscorbackend.service.idam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Authorize {
    private String defaultUrl;
    private String code;
    private String accessToken;

    public Authorize(@JsonProperty("default-url") String defaultUrl,
                     @JsonProperty("code") String code,
                     @JsonProperty("access_token") String accessToken) {
        this.defaultUrl = defaultUrl;
        this.code = code;
        this.accessToken = accessToken;
    }
}
