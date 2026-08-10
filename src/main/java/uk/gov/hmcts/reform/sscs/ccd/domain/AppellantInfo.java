package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "appellantInfoRequestDetails", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class AppellantInfo {
    @CCD(label = "Request details", typeOverride = FieldType.TextArea)
    @JsonProperty("appellantInfoParagraph")
    private String paragraph;
    @CCD(label = "Date of request", typeOverride = FieldType.Date)
    @JsonProperty("appellantInfoRequestDate")
    private String requestDate;

    @JsonCreator
    public AppellantInfo(@JsonProperty(value = "appellantInfoParagraph") String paragraph,
                         @JsonProperty(value = "appellantInfoRequestDate") String requestDate) {
        this.paragraph = paragraph;
        this.requestDate = requestDate;
    }
}
