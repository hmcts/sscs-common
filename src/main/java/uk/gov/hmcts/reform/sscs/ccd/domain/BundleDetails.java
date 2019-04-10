package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.config.SscsConstants.ZONE_ID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
    private DocumentLink stitchedDocument;
    private String stitchStatus;
    private String dateGenerated;

    @JsonCreator
    public BundleDetails(@JsonProperty("id") Long id,
                         @JsonProperty("documents") List<BundleDocument> documents,
                         @JsonProperty("title") String title,
                         @JsonProperty("description") String description,
                         @JsonProperty("eligibleForStitching") String eligibleForStitching,
                         @JsonProperty("stitchedDocument") DocumentLink stitchedDocument,
                         @JsonProperty("stitchStatus") String stitchStatus,
                         @JsonProperty("dateGenerated") String dateGenerated) {
        this.id = id;
        this.documents = documents;
        this.title = title;
        this.description = description;
        this.eligibleForStitching = eligibleForStitching;
        this.stitchedDocument = stitchedDocument;
        this.stitchStatus = stitchStatus;
        this.dateGenerated = dateGenerated;
    }

    @JsonIgnore
    public ZonedDateTime getDateTime() {
        return ZonedDateTime.parse(dateGenerated + "Z").toInstant().atZone(ZoneId.of(ZONE_ID));
    }
}