package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "sscsDocument", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class SscsDocumentDetails extends AbstractDocumentDetails {

    @CCD(label = "Email content", typeOverride = FieldType.TextArea)
    private String documentEmailContent;
    @CCD(label = "Edited document URL", typeOverride = FieldType.Document)
    private DocumentLink editedDocumentLink;
    @CCD(label = "Party uploaded", typeOverride = FieldType.Text)
    private UploadParty partyUploaded;
    @CCD(label = "Original Sender")
    private String originalPartySender;
    @CCD(label = "Date approved", typeOverride = FieldType.Date)
    private String dateApproved;

    @JsonCreator
    public SscsDocumentDetails(@JsonProperty("documentType") String documentType,
                               @JsonProperty("documentFileName") String documentFileName,
                               @JsonProperty("documentEmailContent") String documentEmailContent,
                               @JsonProperty("documentDateAdded") String documentDateAdded,
                               @JsonProperty("documentLink") DocumentLink documentLink,
                               @JsonProperty("editedDocumentLink") DocumentLink editedDocumentLink,
                               @JsonProperty("documentComment") String documentComment,
                               @JsonProperty("evidenceIssued") String evidenceIssued,
                               @JsonProperty("bundleAddition") String bundleAddition,
                               @JsonProperty("documentTranslationStatus") SscsDocumentTranslationStatus documentTranslationStatus,
                               @JsonProperty("partyUploaded") UploadParty partyUploaded,
                               @JsonProperty("dateApproved") String dateApproved,
                               @JsonProperty("resizedDocumentLink") DocumentLink resizedDocumentLink,
                               @JsonProperty("avDocumentLink") DocumentLink avDocumentLink,
                               @JsonProperty("originalPartySender") String originalPartySender,
                               @JsonProperty("originalSenderOtherPartyId") String originalSenderOtherPartyId,
                               @JsonProperty("originalSenderOtherPartyName") String originalSenderOtherPartyName,
                               @JsonProperty("controlNumber") String controlNumber) {
        super(documentType, documentFileName, documentDateAdded, documentLink, editedDocumentLink, documentComment, evidenceIssued, bundleAddition, documentTranslationStatus, partyUploaded, dateApproved, resizedDocumentLink, avDocumentLink, originalPartySender, originalSenderOtherPartyId, originalSenderOtherPartyName, controlNumber);
        this.documentEmailContent = documentEmailContent;
        this.editedDocumentLink = editedDocumentLink;
        this.partyUploaded = partyUploaded;
        this.dateApproved = dateApproved;
        this.originalPartySender = originalPartySender;
    }
}
