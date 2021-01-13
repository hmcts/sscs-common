package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class ReasonableAdjustmentDetails {
    private YesNo wantsReasonableAdjustment;
    private String reasonableAdjustmentRequirements;

    public ReasonableAdjustmentDetails(@JsonProperty("wantsReasonableAdjustment") YesNo wantsReasonableAdjustment,
                                       @JsonProperty("reasonableAdjustmentRequirements") String reasonableAdjustmentRequirements) {
        this.wantsReasonableAdjustment = wantsReasonableAdjustment;
        this.reasonableAdjustmentRequirements = reasonableAdjustmentRequirements;
    }
}
