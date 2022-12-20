package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
public class DocumentSelection {
    private DocumentSelectionDetails value;

    @JsonCreator
    public DocumentSelection(DocumentSelectionDetails value) {
        this.value = value;
    }
}
