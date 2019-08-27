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
public class DirectionResponsesValue {

    private String documentType;
    private DocumentLink documentLink;

    @JsonCreator
    public DirectionResponsesValue(@JsonProperty("documentType") String documentType,
                                   @JsonProperty("documentLink") DocumentLink documentLink) {
        this.documentType = documentType;
        this.documentLink = documentLink;
    }
}
