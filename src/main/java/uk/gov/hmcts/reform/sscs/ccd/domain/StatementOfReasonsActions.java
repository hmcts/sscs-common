package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SOR_EXTEND_TIME;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SOR_ISSUE_DIRECTIONS;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SOR_REFUSED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SOR_WRITE;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatementOfReasonsActions {
    GRANT("extendTime","Grant Statement of Reasons Application", SOR_EXTEND_TIME),
    REFUSE("refuse","Refuse Statement of Reasons Application", SOR_REFUSED),
    ISSUE_DIRECTIONS("issueDirections","Issue directions", SOR_ISSUE_DIRECTIONS),
    WRITE("write","Write Statement of Reasons", SOR_WRITE);

    private final String ccdDefinition;
    private final String descriptionEn;
    private final EventType callbackEvent;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}
