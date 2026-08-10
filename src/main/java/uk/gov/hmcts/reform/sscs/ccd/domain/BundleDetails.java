package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "Bundle", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class BundleDetails {

    @CCD(label = "Bundle ID", showCondition = "title=\"AnyValueToFailThisCondition\"")
    private String id;
    @CCD(label = "Config used for bundle")
    private String title;
    @CCD(
            label = "Description",
            showCondition = "title=\"AnyValueToFailThisCondition\"",
            typeOverride = FieldType.TextArea
    )
    private String description;
    @CCD(label = "Is this the bundle you want to amend?", typeOverride = FieldType.YesOrNo)
    private String eligibleForStitching;
    @CCD(
            label = "Is this the bundle you want to clone?",
            showCondition = "title=\"AnyValueToFailThisCondition\"",
            typeOverride = FieldType.YesOrNo
    )
    private String eligibleForCloning;
    @CCD(label = "Bundle document", typeOverride = FieldType.Collection, typeParameterOverride = "BundleDocument")
    private List<BundleDocument> documents;
    @CCD(label = "Bundle folder", typeOverride = FieldType.Collection, typeParameterOverride = "BundleFolder")
    private List<BundleFolder> folders;
    @CCD(label = "Stitch status")
    private String stitchStatus;
    @CCD(label = "Stitched document", typeOverride = FieldType.Document)
    private DocumentLink stitchedDocument;
    @CCD(
            label = "Should this bundle have coversheets separating each document?",
            showCondition = "title=\"AnyValueToFailThisCondition\"",
            typeOverride = FieldType.YesOrNo
    )
    private String hasCoversheets;
    @CCD(
            label = "Should this bundle have a title page with a table of contents?",
            showCondition = "title=\"AnyValueToFailThisCondition\"",
            typeOverride = FieldType.YesOrNo
    )
    private String hasTableOfContents;
    @CCD(
            label = "Should this bundle’s folders have a coversheet?",
            showCondition = "title=\"AnyValueToFailThisCondition\"",
            typeOverride = FieldType.YesOrNo
    )
    private String hasFolderCoversheets;
    @CCD(label = "Error from Stiching service")
    private String stitchingFailureMessage;
    @CCD(label = "Name of the PDF", showCondition = "title=\"AnyValueToFailThisCondition\"")
    private String fileName;
    @CCD(
            label = "Pagination Style",
            showCondition = "title=\"AnyValueToFailThisCondition\"",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "paginationStyle",
            typeParameterClass = PaginationStyle.class
    )
    private String paginationStyle;
    @CCD(label = "Cover page template", showCondition = "title=\"AnyValueToFailThisCondition\"")
    private String coverpageTemplate;
    @CCD(
            label = "Page Number Format",
            showCondition = "title=\"AnyValueToFailThisCondition\"",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "pageNumberFormat",
            typeParameterClass = PageNumberFormat.class
    )
    private String pageNumberFormat;


    @JsonCreator
    public BundleDetails(@JsonProperty("id") String id,
                         @JsonProperty("title") String title,
                         @JsonProperty("description") String description,
                         @JsonProperty("eligibleForStitching") String eligibleForStitching,
                         @JsonProperty("eligibleForCloning") String eligibleForCloning,
                         @JsonProperty("documents") List<BundleDocument> documents,
                         @JsonProperty("folders") List<BundleFolder> folders,
                         @JsonProperty("stitchStatus") String stitchStatus,
                         @JsonProperty("stitchedDocument") DocumentLink stitchedDocument,
                         @JsonProperty("hasCoversheets") String hasCoversheets,
                         @JsonProperty("hasTableOfContents") String hasTableOfContents,
                         @JsonProperty("hasFolderCoversheets") String hasFolderCoversheets,
                         @JsonProperty("stitchingFailureMessage") String stitchingFailureMessage,
                         @JsonProperty("fileName") String fileName,
                         @JsonProperty("paginationStyle") String paginationStyle,
                         @JsonProperty("coverpageTemplate") String coverpageTemplate,
                         @JsonProperty("pageNumberFormat") String pageNumberFormat) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.eligibleForStitching = eligibleForStitching;
        this.eligibleForCloning = eligibleForCloning;
        this.documents = documents;
        this.folders = folders;
        this.stitchStatus = stitchStatus;
        this.stitchedDocument = stitchedDocument;
        this.hasCoversheets = hasCoversheets;
        this.hasTableOfContents = hasTableOfContents;
        this.hasFolderCoversheets = hasFolderCoversheets;
        this.stitchingFailureMessage = stitchingFailureMessage;
        this.fileName = fileName;
        this.paginationStyle = paginationStyle;
        this.coverpageTemplate = coverpageTemplate;
        this.pageNumberFormat = pageNumberFormat;
    }
}