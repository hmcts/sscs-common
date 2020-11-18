package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DwpDocument {

    private DocumentLink documentLink;
    private String documentType;
    private String documentDateAdded;


    @JsonCreator
    public DwpDocument(@JsonProperty("documentLink") DocumentLink documentLink,
                       @JsonProperty("documentType") String documentType,
                       @JsonProperty("documentDateAdded") String documentDateAdded) {
        this.documentLink = documentLink;
        this.documentType = documentType;
        this.documentDateAdded = documentDateAdded;
    }
}
