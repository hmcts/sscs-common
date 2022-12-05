package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostHearingReviewType {
    SET_ASIDE("setAside","Set Aside"),
    CORRECTION("correction","Correction"),
    STATEMENT_OF_REASONS("statementOfReasons","Statement of Reasons"),
    PERMISSION_TO_APPEAL("permissionToAppeal","Permission to Appeal"),
    LIBERTY_TO_APPLY("libertyToApply","Liberty to Apply");

    private final String ccdDefinition;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}
