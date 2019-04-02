package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.LinkedList;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class Bundle {

    private Long id;
    private String title;
    private String description;
    private String eligibleForStitching;
    private DocumentLink documentLink;
    private List<BundleDocument> documents = new LinkedList<>();

    @JsonIgnore
    public String generateEligibleForStitchingAsBoolean(boolean eligibleForStitching) {
        return eligibleForStitching ? "yes" : "no";
    }
}
