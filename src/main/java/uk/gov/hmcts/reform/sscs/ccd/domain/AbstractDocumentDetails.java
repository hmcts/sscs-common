package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

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
    private UploadParty partyUploaded;
    private String dateApproved;
    private SscsDocumentTranslationStatus documentTranslationStatus;
    private DocumentLink resizedDocumentLink;
    private DocumentLink avDocumentLink;
    private String originalPartySender;
    private String originalSenderOtherPartyId;
    private String originalSenderOtherPartyName;

    @JsonCreator
    public AbstractDocumentDetails(@JsonProperty("documentType") String documentType,
                                   @JsonProperty("documentFileName") String documentFileName,
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
                                   @JsonProperty("originalSenderOtherPartyName") String originalSenderOtherPartyName) {

        this.documentType = documentType;
        this.documentFileName = documentFileName;
        this.documentDateAdded = documentDateAdded;
        this.documentLink = documentLink;
        this.editedDocumentLink = editedDocumentLink;
        this.documentComment = documentComment;
        this.evidenceIssued = evidenceIssued;
        this.bundleAddition = bundleAddition;
        this.documentTranslationStatus = documentTranslationStatus;
        this.partyUploaded = partyUploaded;
        this.dateApproved = dateApproved;
        this.resizedDocumentLink = resizedDocumentLink;
        this.avDocumentLink = avDocumentLink;
        this.originalPartySender = originalPartySender;
        this.originalSenderOtherPartyId = originalSenderOtherPartyId;
        this.originalSenderOtherPartyName = originalSenderOtherPartyName;
    }

    @JsonIgnore
    public LocalDateTime getDateTimeFormatted() {
        try {
            if (StringUtils.isEmpty(documentDateAdded)) {
                return null;
            }
            return LocalDateTime.of(LocalDate.parse(documentDateAdded), LocalTime.MIN);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    @JsonIgnore
    public String getFirstHalfOfBundleAddition() {
        if (StringUtils.isEmpty(bundleAddition)) {
            return null;
        }
        return bundleAddition.substring(0,1);
    }

    @JsonIgnore
    public Integer getSecondHalfOfBundleAddition() {
        if (StringUtils.isEmpty(bundleAddition)) {
            return null;
        }
        return NumberUtils.isCreatable(bundleAddition.substring(1)) ? Integer.parseInt(bundleAddition.substring(1)) : 0;
    }
}
