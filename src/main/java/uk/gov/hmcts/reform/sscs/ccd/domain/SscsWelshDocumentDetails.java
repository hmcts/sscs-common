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
public class SscsWelshDocumentDetails {

    private String documentType;
    private String documentFileName;
    private String documentDateAdded;
    private DocumentLink documentLink;
    private String originalDocumentFileName;
    private String documentComment;
    private String documentLanguage;

    @JsonCreator
    public SscsWelshDocumentDetails(@JsonProperty("documentType") String documentType,
                                    @JsonProperty("documentFileName") String documentFileName,
                                    @JsonProperty("documentDateAdded") String documentDateAdded,
                                    @JsonProperty("documentLink") DocumentLink documentLink,
                                    @JsonProperty("originalDocumentFileName") String originalDocumentFileName,
                                    @JsonProperty("documentComment") String documentComment,
                                    @JsonProperty("documentLanguage") String documentLanguage) {
        this.documentType = documentType;
        this.documentFileName = documentFileName;
        this.documentDateAdded = documentDateAdded;
        this.documentLink = documentLink;
        this.originalDocumentFileName = originalDocumentFileName;
        this.documentComment = documentComment;
        this.documentLanguage = documentLanguage;
    }
}