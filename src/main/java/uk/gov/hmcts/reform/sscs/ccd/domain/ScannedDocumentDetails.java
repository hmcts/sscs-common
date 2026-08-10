package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.hmcts.reform.sscs.ccd.callback.ScannedDocumentType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "ScannedDocument", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScannedDocumentDetails {
    @CCD(
            label = "Document Type",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "ScannedDocumentType",
            typeParameterClass = ScannedDocumentType.class
    )
    private String type;
    @CCD(label = "Original document URL", regex = ".pdf", typeOverride = FieldType.Document)
    private DocumentLink url;
    @CCD(label = "Edited document URL", regex = ".pdf", typeOverride = FieldType.Document)
    private DocumentLink editedUrl;
    @CCD(label = "Document Control Number")
    private String controlNumber;
    @CCD(label = "File Name")
    private String fileName;
    @CCD(label = "Scanned Date", typeOverride = FieldType.DateTime)
    private String scannedDate;
    @CCD(label = "Exception Record Reference")
    private String exceptionRecordReference;
    @CCD(label = "Document Subtype")
    private String subtype;
    @CCD(label = "Include in bundle?", typeOverride = FieldType.YesOrNo)
    private String includeInBundle;
    @CCD(label = "Original sender other party ID", showCondition = "type=\"AnyValueToFailThisCondition\"")
    private String originalSenderOtherPartyId;
    @CCD(label = "Original sender other party name")
    private String originalSenderOtherPartyName;
    @CCD(
            label = "Should this document be stored in the Documents tab or the Tribunal internal documents tab?",
            showCondition = "documentTabChoice=\"DONOTSHOW\"",
            typeParameterOverride = "FL_documentTabChoice",
            typeParameterClass = DocumentTabChoice2.class
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DocumentTabChoice documentTabChoice;

    @JsonCreator
    public ScannedDocumentDetails(@JsonProperty("type") String type,
                                  @JsonProperty("url") DocumentLink url,
                                  @JsonProperty("editedUrl") DocumentLink editedUrl,
                                  @JsonProperty("controlNumber") String controlNumber,
                                  @JsonProperty("fileName") String fileName,
                                  @JsonProperty("scannedDate") String scannedDate,
                                  @JsonProperty("exceptionRecordReference") String exceptionRecordReference,
                                  @JsonProperty("subtype") String subtype,
                                  @JsonProperty("includeInBundle") String includeInBundle,
                                  @JsonProperty("originalSenderOtherPartyId") String originalSenderOtherPartyId,
                                  @JsonProperty("originalSenderOtherPartyName") String originalSenderOtherPartyName,
                                  @JsonProperty("documentTabChoice") DocumentTabChoice documentTabChoice,
                                  @JsonProperty("deliveryDate") java.time.LocalDateTime deliveryDate) {
        this.type = type;
        this.url = url;
        this.editedUrl = editedUrl;
        this.controlNumber = controlNumber;
        this.fileName = fileName;
        this.scannedDate = scannedDate;
        this.exceptionRecordReference = exceptionRecordReference;
        this.subtype = subtype;
        this.includeInBundle = includeInBundle;
        this.originalSenderOtherPartyId = originalSenderOtherPartyId;
        this.originalSenderOtherPartyName = originalSenderOtherPartyName;
        this.documentTabChoice = documentTabChoice;
        this.deliveryDate = deliveryDate;
    }

    /** Retained so existing positional call sites still compile. */
    public ScannedDocumentDetails(String type,
                                  DocumentLink url,
                                  DocumentLink editedUrl,
                                  String controlNumber,
                                  String fileName,
                                  String scannedDate,
                                  String exceptionRecordReference,
                                  String subtype,
                                  String includeInBundle,
                                  String originalSenderOtherPartyId,
                                  String originalSenderOtherPartyName,
                                  DocumentTabChoice documentTabChoice) {
        this(type, url, editedUrl, controlNumber, fileName, scannedDate, exceptionRecordReference, subtype, includeInBundle, originalSenderOtherPartyId, originalSenderOtherPartyName, documentTabChoice, null);
    }

    @JsonIgnore
    public LocalDateTime getScanDateTimeFormatted() {
        if (StringUtils.isEmpty(scannedDate)) {
            return null;
        }
        try {
            return LocalDateTime.of(LocalDate.parse(scannedDate), LocalTime.MIN);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    @JsonIgnore
    public Long getLongControlNumber() {
        return (StringUtils.isNotEmpty(controlNumber) && NumberUtils.isCreatable(controlNumber)) ? Long.parseLong(controlNumber) : null;
    }

  // ==== ccd-definition-converter: synthesised definition-only fields (retrofit) ====
  @CCD(label = "Delivery Date")
  private java.time.LocalDateTime deliveryDate;
  // ==== end synthesised definition-only fields ====
}
