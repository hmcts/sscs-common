package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscsStrikeOutDocument {

    @CCD(label = "Type", typeOverride = FieldType.FixedList, typeParameterOverride = "FL_InterlocDecision")
    private String documentType;
    @CCD(label = "File name")
    private String documentFileName;
    @CCD(label = "Date added", typeOverride = FieldType.Date)
    private String documentDateAdded;
    @CCD(label = "Document Url", typeOverride = FieldType.Document)
    private DocumentLink documentLink;

    @JsonCreator
    public SscsStrikeOutDocument(@JsonProperty("documentType") String documentType,
                                 @JsonProperty("documentFileName") String documentFileName,
                                 @JsonProperty("documentDateAdded") String documentDateAdded,
                                 @JsonProperty("documentLink") DocumentLink documentLink) {
        this.documentType = documentType;
        this.documentFileName = documentFileName;
        this.documentDateAdded = documentDateAdded;
        this.documentLink = documentLink;
    }
}
