package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

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

    public static AdjournCasePanelMembersExcluded getPanelMembersExcludedByCcdDefinition(String ccdDefinition) {
        return Arrays.stream(AdjournCasePanelMembersExcluded.values())
            .filter(adjournCasePanelMembersExcluded -> Objects.equals(adjournCasePanelMembersExcluded.ccdDefinition, ccdDefinition))
            .findFirst()
            .orElse(null);
    }
}