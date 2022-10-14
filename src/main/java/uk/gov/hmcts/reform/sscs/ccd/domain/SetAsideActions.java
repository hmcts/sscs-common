package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SET_ASIDE_GRANTED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SET_ASIDE_ISSUE_DIRECTIONS;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SET_ASIDE_REFUSED;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SetAsideActions {
    GRANT("grant","Grant Set Aside Application", SET_ASIDE_GRANTED),
    REFUSE("refuse","Refuse Set Aside Application", SET_ASIDE_REFUSED),
    ISSUE_DIRECTIONS("issueDirections","IssueDirections", SET_ASIDE_ISSUE_DIRECTIONS);

    private final String ccdDefinition;
    private final String descriptionEn;
    private final EventType callbackEvent;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}
