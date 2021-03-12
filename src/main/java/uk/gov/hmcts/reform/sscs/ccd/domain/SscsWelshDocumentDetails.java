package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class SscsWelshDocumentDetails extends AbstractDocumentDetails {

    private String originalDocumentFileName;
    private String documentComment;
    private String documentLanguage;

    @JsonCreator
    public SscsWelshDocumentDetails(@JsonProperty("documentType") String documentType,
                                    @JsonProperty("documentFileName") String documentFileName,
                                    @JsonProperty("documentDateAdded") String documentDateAdded,
                                    @JsonProperty("documentLink") DocumentLink documentLink,
                                    @JsonProperty("originalDocumentFileName") String originalDocumentFileName,
                                    @JsonProperty("documentComment") String documentComment,
                                    @JsonProperty("documentLanguage") String documentLanguage,
                                    @JsonProperty("evidenceIssued") String evidenceIssued,
                                    @JsonProperty("bundleAddition") String bundleAddition) {
        super(documentType, documentFileName, documentDateAdded, documentLink, null, documentComment, evidenceIssued, bundleAddition, null, null, null);
        this.originalDocumentFileName = originalDocumentFileName;
        this.documentComment = documentComment;
        this.documentLanguage = documentLanguage;
    }

    @JsonIgnore
    @Override
    public String getDocumentDetailsSubType() {
        return "SscsWelshDocumentDetails";
    }
}
