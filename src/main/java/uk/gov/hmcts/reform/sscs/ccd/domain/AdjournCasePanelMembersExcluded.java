package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_panelMembersExcluded", generate = true)
@Getter
@AllArgsConstructor
public enum AdjournCasePanelMembersExcluded {
    RESERVED("Reserved", "Reserved"),
    NO("No", "No"),
    YES("Yes", "Yes");

    private final String ccdDefinition;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }

}