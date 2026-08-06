package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "AudioVideoEvidence", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AudioVideoEvidenceDetails {
    @CCD(label = "Document Type")
    private String documentType;
    @CCD(label = "Audio/video document url", regex = ".mp3,.mp4,.MP3,.MP4", typeOverride = FieldType.Document)
    private DocumentLink documentLink;
    @CCD(
            label = "RIP 1 document",
            hint = "Document must be PDF formatted",
            regex = ".pdf",
            typeOverride = FieldType.Document
    )
    private DocumentLink rip1Document;
    @CCD(label = "File name")
    private String fileName;
    @CCD(label = "Date added")
    private LocalDate dateAdded;
    @CCD(label = "Date approved")
    private LocalDate dateApproved;
    @CCD(
            label = "Audio/video party uploaded",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_audioVideoPartyUploaded"
    )
    private UploadParty partyUploaded;
    @CCD(
            label = "Audio/video processed action",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_audioVideoProcessedAction"
    )
    private ProcessedAction processedAction;
    @CCD(label = "Statement of evidence pdf", regex = ".pdf", typeOverride = FieldType.Document)
    private DocumentLink statementOfEvidencePdf;
    @CCD(label = "Original Sender")
    private String originalPartySender;
    @CCD(label = "Original sender other party ID", showCondition = "documentType=\"AnyValueToFailThisCondition\"")
    private String originalSenderOtherPartyId;
    @CCD(label = "Original sender other party name")
    private String originalSenderOtherPartyName;

    @JsonCreator
    public AudioVideoEvidenceDetails(@JsonProperty("documentType") String documentType,
                                     @JsonProperty("documentLink") DocumentLink documentLink,
                                     @JsonProperty("rip1Document") DocumentLink rip1Document,
                                     @JsonProperty("fileName") String fileName,
                                     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                                @JsonSerialize(using = LocalDateSerializer.class)
                                                @JsonProperty("dateAdded") LocalDate dateAdded,
                                     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                         @JsonSerialize(using = LocalDateSerializer.class)
                                         @JsonProperty("dateApproved") LocalDate dateApproved,
                                     @JsonProperty("partyUploaded") UploadParty partyUploaded,
                                     @JsonProperty("processedAction") ProcessedAction processedAction,
                                     @JsonProperty("statementOfEvidencePdf") DocumentLink statementOfEvidencePdf,
                                     @JsonProperty("originalPartySender") String originalPartySender,
                                     @JsonProperty("originalSenderOtherPartyId") String originalSenderOtherPartyId,
                                     @JsonProperty("originalSenderOtherPartyName") String originalSenderOtherPartyName) {
        this.documentType = documentType;
        this.documentLink = documentLink;
        this.rip1Document = rip1Document;
        this.fileName = fileName;
        this.dateAdded = dateAdded;
        this.dateApproved = dateApproved;
        this.partyUploaded = partyUploaded;
        this.processedAction = processedAction;
        this.statementOfEvidencePdf = statementOfEvidencePdf;
        this.originalPartySender = originalPartySender;
        this.originalSenderOtherPartyId = originalSenderOtherPartyId;
        this.originalSenderOtherPartyName = originalSenderOtherPartyName;
    }

}
