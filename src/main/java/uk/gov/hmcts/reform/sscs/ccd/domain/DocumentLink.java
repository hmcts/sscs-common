package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentLink {

    @JsonProperty("document_url")
    private String documentUrl;
    @JsonProperty("document_binary_url")
    private String documentBinaryUrl;
    @JsonProperty("document_filename")
    private String documentFilename;

    @JsonCreator
    public DocumentLink(@JsonProperty("document_url") String documentUrl,
                        @JsonProperty("document_binary_url") String documentBinaryUrl,
                        @JsonProperty("document_filename") String documentFilename) {
        this.documentUrl = documentUrl;
        this.documentBinaryUrl = documentBinaryUrl;
        this.documentFilename = documentFilename;
    }
}
