package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AbstractDocumentDetails {

    private String documentType;
    private String documentFileName;
    private String documentDateAdded;
    private String documentComment;
    private DocumentLink documentLink;
    private DocumentLink editedDocumentLink;
    private String evidenceIssued;
    private String bundleAddition;

    @JsonCreator
    public AbstractDocumentDetails(@JsonProperty("documentType") String documentType,
                                   @JsonProperty("documentFileName") String documentFileName,
                                   @JsonProperty("documentDateAdded") String documentDateAdded,
                                   @JsonProperty("documentLink") DocumentLink documentLink,
                                   @JsonProperty("editedDocumentLink") DocumentLink editedDocumentLink,
                                   @JsonProperty("documentComment") String documentComment,
                                   @JsonProperty("evidenceIssued") String evidenceIssued,
                                   @JsonProperty("bundleAddition") String bundleAddition) {
        this.documentType = documentType;
        this.documentFileName = documentFileName;
        this.documentDateAdded = documentDateAdded;
        this.documentLink = documentLink;
        this.editedDocumentLink = editedDocumentLink;
        this.documentComment = documentComment;
        this.evidenceIssued = evidenceIssued;
        this.bundleAddition = bundleAddition;
    }

}
