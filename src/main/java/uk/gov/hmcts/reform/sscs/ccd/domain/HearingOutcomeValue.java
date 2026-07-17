package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HearingOutcomeValue {
    @CCD(label = "Hearing Outcome", typeOverride = FieldType.FixedList, typeParameterOverride = "FL_caseOutcome")
    private String hearingOutcomeId;
    @CCD(label = "Did PO Attend?", typeOverride = FieldType.YesOrNo)
    private YesNo didPoAttendHearing;
    @CCD(label = "Hearing", typeOverride = FieldType.DynamicList)
    private DynamicList completedHearings;
}
