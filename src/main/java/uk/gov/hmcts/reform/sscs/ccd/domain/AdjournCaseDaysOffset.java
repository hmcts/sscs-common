package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdjournCaseDaysOffset {
    OTHER("0", 0, "Other"),
    TWENTY_EIGHT_DAYS("28", 28, "28 days"),
    TWENTY_ONE_DAYS("21", 21, "21 days"),
    FOURTEEN_DAYS("14", 14, "14 days");

    private final String ccdDefinition;
    private final Integer number;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(ccdDefinition);
    }

    public static AdjournCaseDaysOffset getDaysOffsetByNumber(Integer number) {
        return Arrays.stream(AdjournCaseDaysOffset.values())
            .filter(adjournCaseTypeOfHearing -> Objects.equals(adjournCaseTypeOfHearing.number, number))
            .findFirst()
            .orElse(null);
    }

    public static AdjournCaseDaysOffset getDaysOffsetByCcdDefinition(String ccdDefinition) {
        return Arrays.stream(AdjournCaseDaysOffset.values())
            .filter(adjournCaseTypeOfHearing -> Objects.equals(adjournCaseTypeOfHearing.ccdDefinition, ccdDefinition))
            .findFirst()
            .orElse(null);
    }
}
