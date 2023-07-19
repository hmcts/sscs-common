package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostHearingReviewType {
    SET_ASIDE("setAside","Set Aside", "Set Aside"),
    CORRECTION("correction","Correction", "Correction"),
    STATEMENT_OF_REASONS("statementOfReasons","Statement of Reasons", "SOR"),
    PERMISSION_TO_APPEAL("permissionToAppeal","Permission to Appeal", "PTA"),
    LIBERTY_TO_APPLY("libertyToApply","Liberty to Apply", "LTA");

    private final String ccdDefinition;
    private final String descriptionEn;
    private final String shortenedDescriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}
