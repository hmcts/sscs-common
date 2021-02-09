package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class DwpDocumentDetails extends AbstractDocumentDetails {

    private String dwpEditedEvidenceReason;

    @JsonCreator
    public DwpDocumentDetails(@JsonProperty("documentType") String documentType,
                              @JsonProperty("documentFileName") String documentFileName,
                              @JsonProperty("documentDateAdded") String documentDateAdded,
                              @JsonProperty("documentLink") DocumentLink documentLink,
                              @JsonProperty("editedDocumentLink") DocumentLink editedDocumentLink,
                              @JsonProperty("dwpEditedEvidenceReason") String dwpEditedEvidenceReason,
                              @JsonProperty("documentComment") String documentComment,
                              @JsonProperty("evidenceIssued") String evidenceIssued,
                              @JsonProperty("bundleAddition") String bundleAddition) {

        super(documentType, documentFileName, documentDateAdded, documentLink, editedDocumentLink, documentComment, evidenceIssued, bundleAddition);
        this.dwpEditedEvidenceReason = dwpEditedEvidenceReason;
    }
}
