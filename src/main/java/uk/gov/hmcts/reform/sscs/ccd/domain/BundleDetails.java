package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class BundleDetails {

    private Long id;
    private List<BundleDocument> documents;
    private String title;
    private String description;
    private String eligibleForStitching;
    private String hasTableOfContents;
    private String hasCoversheets;
    private String fileName;
    private DocumentLink stitchedDocument;
    private String stitchStatus;

    @JsonCreator
    public BundleDetails(@JsonProperty("id") Long id,
                         @JsonProperty("documents") List<BundleDocument> documents,
                         @JsonProperty("title") String title,
                         @JsonProperty("description") String description,
                         @JsonProperty("eligibleForStitching") String eligibleForStitching,
                         @JsonProperty("hasTableOfContents") String hasTableOfContents,
                         @JsonProperty("hasCoversheets") String hasCoversheets,
                         @JsonProperty("fileName") String fileName,
                         @JsonProperty("stitchedDocument") DocumentLink stitchedDocument,
                         @JsonProperty("stitchStatus") String stitchStatus) {
        this.id = id;
        this.documents = documents;
        this.title = title;
        this.description = description;
        this.eligibleForStitching = eligibleForStitching;
        this.hasTableOfContents = hasTableOfContents;
        this.hasCoversheets = hasCoversheets;
        this.fileName = fileName;
        this.stitchedDocument = stitchedDocument;
        this.stitchStatus = stitchStatus;
    }
}