package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdjournCaseNextHearingDurationType {
    NON_STANDARD("nonStandardTimeSlot", "Non standard time slot"),
    STANDARD("standardTimeSlot", "Standard time slot");

    private final String ccdDefinition;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }

}