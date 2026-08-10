package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "directionResponsedDocumentCT", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DirectionResponsesValue {

    @CCD(
            label = "Select document type",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "documentType",
            typeParameterClass = DocumentType2.class
    )
    private String documentType;
    @CCD(label = "Select document for upload", typeOverride = FieldType.Document)
    private DocumentLink documentLink;

    @JsonCreator
    public DirectionResponsesValue(@JsonProperty("documentType") String documentType,
                                   @JsonProperty("documentLink") DocumentLink documentLink) {
        this.documentType = documentType;
        this.documentLink = documentLink;
    }
}
