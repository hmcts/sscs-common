package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_adjournCaseDaysOffset", generate = true)
@Getter
@AllArgsConstructor
public enum AdjournCaseDaysOffset {
    OTHER(0, "Other"),
    TWENTY_EIGHT_DAYS(28, "28 days"),
    TWENTY_ONE_DAYS(21, "21 days"),
    FOURTEEN_DAYS(14, "14 days");

    private final Integer ccdDefinition;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(ccdDefinition);
    }

}
