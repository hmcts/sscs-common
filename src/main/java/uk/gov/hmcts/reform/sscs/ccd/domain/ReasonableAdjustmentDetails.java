package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class ReasonableAdjustmentDetails {
    @CCD(label = "Wants Reasonable Adjustment", typeOverride = FieldType.YesOrNo)
    private YesNo wantsReasonableAdjustment;
    @CCD(
            label = "Alternative Format Requirements",
            showCondition = "wantsReasonableAdjustment=\"Yes\"",
            typeOverride = FieldType.TextArea
    )
    private String reasonableAdjustmentRequirements;

    public ReasonableAdjustmentDetails(@JsonProperty("wantsReasonableAdjustment") YesNo wantsReasonableAdjustment,
                                       @JsonProperty("reasonableAdjustmentRequirements") String reasonableAdjustmentRequirements) {
        this.wantsReasonableAdjustment = wantsReasonableAdjustment;
        this.reasonableAdjustmentRequirements = reasonableAdjustmentRequirements;
    }
}
