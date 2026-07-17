package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class ReasonableAdjustments {
    @CCD(label = "Appellant")
    private ReasonableAdjustmentDetails appellant;
    @CCD(label = "Appointee")
    private ReasonableAdjustmentDetails appointee;
    @CCD(label = "Representative")
    private ReasonableAdjustmentDetails representative;
    @CCD(label = "Joint Party")
    private ReasonableAdjustmentDetails jointParty;

    public ReasonableAdjustments(@JsonProperty("appellant") ReasonableAdjustmentDetails appellant,
                                 @JsonProperty("appointee") ReasonableAdjustmentDetails appointee,
                                 @JsonProperty("representative") ReasonableAdjustmentDetails representative,
                                 @JsonProperty("jointParty") ReasonableAdjustmentDetails jointParty) {
        this.appellant = appellant;
        this.appointee = appointee;
        this.representative = representative;
        this.jointParty = jointParty;
    }
}
