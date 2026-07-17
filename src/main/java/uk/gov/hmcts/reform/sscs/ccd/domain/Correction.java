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
public class Correction {
    @CCD(label = "Correction type")
    private AdminCorrectionType adminCorrectionType;
    @CCD(
            label = "Action Correction Application",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_actionCorrection"
    )
    private CorrectionActions action;
    @CCD(label = "Correction in progress?", typeOverride = FieldType.YesOrNo)
    private YesNo isCorrectionFinalDecisionInProgress;
    @CCD(label = "Upload or enter details of your post hearing request")
    private RequestFormat requestFormat;
}
