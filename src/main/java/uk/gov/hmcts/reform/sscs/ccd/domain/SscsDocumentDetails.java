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
public class SscsDocumentDetails {

    private String documentType;
    private String documentFileName;
    private String documentEmailContent;
    private String documentDateAdded;
    private DocumentLink documentLink;
    private String documentComment;
    private String controlNumber;
    private String evidenceIssued;

    @JsonCreator
    public SscsDocumentDetails(@JsonProperty("documentType") String documentType,
                               @JsonProperty("documentFileName") String documentFileName,
                               @JsonProperty("documentEmailContent") String documentEmailContent,
                               @JsonProperty("documentDateAdded") String documentDateAdded,
                               @JsonProperty("documentLink") DocumentLink documentLink,
                               @JsonProperty("documentComment") String documentComment,
                               @JsonProperty("controlNumber") String controlNumber,
                               @JsonProperty("evidenceIssued") String evidenceIssued) {
        this.documentType = documentType;
        this.documentFileName = documentFileName;
        this.documentEmailContent = documentEmailContent;
        this.documentDateAdded = documentDateAdded;
        this.documentLink = documentLink;
        this.documentComment = documentComment;
        this.controlNumber = controlNumber;
        this.evidenceIssued = evidenceIssued;
    }
}
