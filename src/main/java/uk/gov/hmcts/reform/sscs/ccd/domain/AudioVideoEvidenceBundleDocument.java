package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "audioVideoEvidenceBundleDocumentCT", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class AudioVideoEvidenceBundleDocument {

    @CCD(label = "Select document", typeOverride = FieldType.Document)
    private DocumentLink documentLink;
    @CCD(
            label = "Audio video bundle document file name",
            showCondition = "documentLink=\"AnyValueToFailThisCondition\""
    )
    private String documentFileName;

    @JsonCreator
    public AudioVideoEvidenceBundleDocument(@JsonProperty("documentLink") DocumentLink documentLink,
                                            @JsonProperty("documentFileName") String documentFileName) {
        this.documentLink = documentLink;
        this.documentFileName = documentFileName;
    }

}