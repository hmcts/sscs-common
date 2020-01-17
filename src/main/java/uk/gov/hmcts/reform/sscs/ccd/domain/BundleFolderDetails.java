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
public class BundleFolderDetails {

    private String name;
    private List<BundleDocument> documents;
    private List<BundleSubfolder> folders;
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
