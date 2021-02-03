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

    @JsonCreator
    public ScannedDocumentDetails(@JsonProperty("type") String type,
                                  @JsonProperty("url") DocumentLink url,
                                  @JsonProperty("editedUrl") DocumentLink editedUrl,
                                  @JsonProperty("controlNumber") String controlNumber,
                                  @JsonProperty("fileName") String fileName,
                                  @JsonProperty("scannedDate") String scannedDate,
                                  @JsonProperty("exceptionRecordReference") String exceptionRecordReference,
                                  @JsonProperty("subtype") String subtype,
                                  @JsonProperty("includeInBundle") String includeInBundle) {
        this.type = type;
        this.url = url;
        this.editedUrl = editedUrl;
        this.controlNumber = controlNumber;
        this.fileName = fileName;
        this.scannedDate = scannedDate;
        this.exceptionRecordReference = exceptionRecordReference;
        this.subtype = subtype;
        this.includeInBundle = includeInBundle;
    }

}
