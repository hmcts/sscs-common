package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class AppellantInfo {
    @JsonProperty(value = "appellantInfoParagraph")
    private String paragraph;

    @JsonProperty(value = "appellantInfoRequestDate")
    private String requestDate;
}
