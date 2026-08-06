package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "corDocument", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CorDocumentDetails {
    @CCD(label = "Document")
    private SscsDocumentDetails document;
    @CCD(label = "Question Id")
    private String questionId;

    @JsonCreator
    public CorDocumentDetails(@JsonProperty("document") SscsDocumentDetails document,
                       @JsonProperty("questionId")String questionId) {
        this.document = document;
        this.questionId = questionId;
    }
}
