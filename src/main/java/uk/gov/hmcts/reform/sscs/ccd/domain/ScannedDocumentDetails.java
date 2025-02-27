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

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScannedDocumentDetails {
    private String type;
    private DocumentLink url;
    private DocumentLink editedUrl;
    private String controlNumber;
    private String fileName;
    private String scannedDate;
    private String exceptionRecordReference;
    private String subtype;
    private String includeInBundle;
    private String originalSenderOtherPartyId;
    private String originalSenderOtherPartyName;
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
                                  @JsonProperty("documentTabChoice") DocumentTabChoice documentTabChoice) {
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
}
