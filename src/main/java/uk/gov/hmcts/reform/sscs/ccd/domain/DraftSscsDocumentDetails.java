package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "draftSscsDocuments", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class DraftSscsDocumentDetails {

    @CCD(
            label = "Type",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "documentType",
            typeParameterClass = DocumentType2.class
    )
    private String documentType;
    @CCD(label = "File name")
    private String documentFileName;
    @CCD(label = "Original document Url", regex = ".pdf,.mp4,.mp3,.MP4,.MP3,.PDF", typeOverride = FieldType.Document)
    private DocumentLink documentLink;

    @JsonCreator
    public DraftSscsDocumentDetails(@JsonProperty("documentType") String documentType,
                                    @JsonProperty("documentFileName") String documentFileName,
                                    @JsonProperty("documentLink") DocumentLink documentLink) {
        this.documentType = documentType;
        this.documentFileName = documentFileName;
        this.documentLink = documentLink;
    }
}
