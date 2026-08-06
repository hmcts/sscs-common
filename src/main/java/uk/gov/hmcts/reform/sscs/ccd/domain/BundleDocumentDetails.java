package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "BundleDocument", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class BundleDocumentDetails {

    @CCD(label = "Document Name")
    private String name;
    @CCD(label = "Short Description", typeOverride = FieldType.TextArea)
    private String description;
    @CCD(label = "Sort Index", showCondition = "name=\"AnyValueToFailThisCondition\"")
    private int sortIndex;
    @CCD(label = "Source Document", typeOverride = FieldType.Document)
    private DocumentLink sourceDocument;


    @JsonCreator
    public BundleDocumentDetails(@JsonProperty("name") String name,
                                @JsonProperty("description") String description,
                                @JsonProperty("sortIndex") int sortIndex,
                                @JsonProperty("sourceDocument") DocumentLink sourceDocument) {
        this.name = name;
        this.description = description;
        this.sortIndex = sortIndex;
        this.sourceDocument = sourceDocument;
    }
}
