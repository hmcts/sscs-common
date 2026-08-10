package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_panelMembersExcluded", generate = true)
@Getter
@AllArgsConstructor
public enum AdjournCasePanelMembersExcluded {
    @CCD(label = "Reserved")
    RESERVED("Reserved", "Reserved"),
    @CCD(label = "No")
    NO("No", "No"),
    @CCD(label = "Yes")
    YES("Yes", "Yes");

    private final String ccdDefinition;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }

}