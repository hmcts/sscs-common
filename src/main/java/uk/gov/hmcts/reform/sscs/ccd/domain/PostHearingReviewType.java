package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_postHearingReviewType", generate = true)
@Getter
@AllArgsConstructor
public enum PostHearingReviewType {
    @CCD(label = "Set Aside")
    SET_ASIDE("setAside","Set Aside", "Set Aside"),
    @CCD(label = "Correction")
    CORRECTION("correction","Correction", "Correction"),
    @CCD(label = "Statement of Reasons (Out of Time)")
    STATEMENT_OF_REASONS("statementOfReasons","Statement of Reasons", "SOR"),
    @CCD(label = "Permission to Appeal")
    PERMISSION_TO_APPEAL("permissionToAppeal","Permission to Appeal", "PTA"),
    @CCD(label = "Liberty to Apply")
    LIBERTY_TO_APPLY("libertyToApply","Liberty to Apply", "LTA");

    private final String ccdDefinition;
    private final String descriptionEn;
    private final String shortDescriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}
