package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SetAsideActions {
    GRANT("grant","Grant Set Aside Application"),
    REFUSE("refuse","Refuse Set Aside Application"),
    ISSUE_DIRECTIONS("issueDirections","IssueDirections");

    private final String ccdDefinition;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}
