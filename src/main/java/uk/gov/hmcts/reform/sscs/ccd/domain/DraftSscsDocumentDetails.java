package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class DraftSscsDocumentDetails {

    private String documentType;
    private String documentFileName;
    private DocumentLink documentLink;

    @JsonCreator
    public DraftSscsDocumentDetails(@JsonProperty("documentType") String documentType,
                                    @JsonProperty("documentFileName") String documentFileName,
                                    @JsonProperty("documentLink") DocumentLink documentLink) {
        this.documentType = documentType;
        this.documentFileName = documentFileName;
        this.documentLink = documentLink;
    }
}
