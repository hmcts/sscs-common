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

@ComplexType(name = "sscsFurtherEvidenceDoc", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscsFurtherEvidenceDocDetails {

    @CCD(label = "Type", typeOverride = FieldType.FixedList, typeParameterOverride = "documentTypeFE")
    private String documentType;
    @CCD(label = "File name")
    private String documentFileName;
    @CCD(label = "Document Url", regex = ".pdf,.mp3,.mp4,.PDF,.MP3,.MP4", typeOverride = FieldType.Document)
    private DocumentLink documentLink;

    @JsonCreator
    public SscsFurtherEvidenceDocDetails(@JsonProperty("documentType") String documentType,
                                         @JsonProperty("documentFileName") String documentFileName,
                                         @JsonProperty("documentLink") DocumentLink documentLink) {
        this.documentType = documentType;
        this.documentFileName = documentFileName;
        this.documentLink = documentLink;
    }
}
