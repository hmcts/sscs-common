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
public class SscsFurtherEvidenceDocDetails {

    private String documentType;
    private String documentFileName;
    private DocumentLink documentLink;

    @JsonCreator
    public SscsFurtherEvidenceDocDetails(@JsonProperty("documentType") String documentType,
                                         @JsonProperty("documentFileName") String documentFileName,
                                         @JsonProperty("documentLink") DocumentLink documentLink) {
        this.documentType = documentType;
        this.documentFileName = documentFileName;
        this.documentLink = documentLink;
    }
}
