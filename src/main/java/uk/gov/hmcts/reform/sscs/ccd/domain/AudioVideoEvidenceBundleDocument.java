package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class AudioVideoEvidenceBundleDocument {

    private DocumentLink documentLink;
    private String documentFileName;

    @JsonCreator
    public AudioVideoEvidenceBundleDocument(@JsonProperty("documentLink") DocumentLink documentLink,
                                            @JsonProperty("documentFileName") String documentFileName) {
        this.documentLink = documentLink;
        this.documentFileName = documentFileName;
    }

}