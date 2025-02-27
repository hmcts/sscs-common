package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InternalCaseDocumentData {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SscsDocument> sscsInternalDocument;
    @Getter
    private DocumentTabChoice moveDocumentTo;
    private String uploadRemoveDocumentType;
    private String uploadRemoveOrMoveDocument;
    private DynamicMixedChoiceList moveDocumentToInternalDocumentsTabDL;
    private DynamicMixedChoiceList moveDocumentToDocumentsTabDL;
    private YesNo shouldBeIssued;

    public void setDynamicList(boolean moveToInternal, DynamicMixedChoiceList dynamicList) {
        if (moveToInternal) {
            this.moveDocumentToInternalDocumentsTabDL = dynamicList;
        } else {
            this.moveDocumentToDocumentsTabDL = dynamicList;
        }
    }

    public DynamicMixedChoiceList getDynamicList(boolean moveToInternal) {
        return moveToInternal ? this.moveDocumentToInternalDocumentsTabDL : this.moveDocumentToDocumentsTabDL;
    }
}
