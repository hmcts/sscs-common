package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "sscsWelshDocuments", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class SscsWelshDocumentDetails extends AbstractDocumentDetails {

    @CCD(label = "Original Document file name")
    private String originalDocumentFileName;
    @CCD(label = "Comment")
    private String documentComment;
    @CCD(
            label = "Language",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "documentLanguage",
            typeParameterClass = DocumentLanguage.class
    )
    private String documentLanguage;

    @JsonCreator
    public SscsWelshDocumentDetails(@JsonProperty("documentType") String documentType,
                                    @JsonProperty("documentFileName") String documentFileName,
                                    @JsonProperty("documentDateAdded") String documentDateAdded,
                                    @JsonProperty("documentLink") DocumentLink documentLink,
                                    @JsonProperty("originalDocumentFileName") String originalDocumentFileName,
                                    @JsonProperty("documentComment") String documentComment,
                                    @JsonProperty("documentLanguage") String documentLanguage,
                                    @JsonProperty("evidenceIssued") String evidenceIssued,
                                    @JsonProperty("bundleAddition") String bundleAddition,
                                    @JsonProperty("controlNumber") String controlNumber) {
        super(documentType, documentFileName, documentDateAdded, documentLink, null, documentComment, evidenceIssued, bundleAddition, null, null, null, null, null, null, null, null, controlNumber);
        this.originalDocumentFileName = originalDocumentFileName;
        this.documentComment = documentComment;
        this.documentLanguage = documentLanguage;
    }
}
