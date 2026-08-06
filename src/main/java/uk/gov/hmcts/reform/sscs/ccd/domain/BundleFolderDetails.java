package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "BundleFolder", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class BundleFolderDetails {

    @CCD(label = "Folder Name")
    private String name;
    @CCD(label = "Folder Documents", typeOverride = FieldType.Collection, typeParameterOverride = "BundleDocument")
    private List<BundleDocument> documents;
    @CCD(label = "Subfolders", typeOverride = FieldType.Collection, typeParameterOverride = "BundleSubfolder")
    private List<BundleSubfolder> folders;
    @CCD(label = "Sort Index", showCondition = "name=\"AnyValueToFailThisCondition\"")
    private int sortIndex;

    @JsonCreator
    public BundleFolderDetails(@JsonProperty("name") String name,
                               @JsonProperty("documents") List<BundleDocument> documents,
                               @JsonProperty("folders") List<BundleSubfolder> folders,
                               @JsonProperty("sortIndex") int sortIndex) {
        this.name = name;
        this.documents = documents;
        this.folders = folders;
        this.sortIndex = sortIndex;
    }
}
