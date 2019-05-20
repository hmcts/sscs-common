package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class AppellantInfo {
    @JsonProperty("appellantInfoParagraph")
    private String paragraph;
    @JsonProperty("appellantInfoRequestDate")
    private String requestDate;

    @JsonCreator
    public AppellantInfo(@JsonProperty(value = "appellantInfoParagraph") String paragraph,
                         @JsonProperty(value = "appellantInfoRequestDate") String requestDate) {
        this.paragraph = paragraph;
        this.requestDate = requestDate;
    }
}
