package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class ReasonableAdjustments {
    private ReasonableAdjustmentDetails appellant;
    private ReasonableAdjustmentDetails appointee;
    private ReasonableAdjustmentDetails representative;
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
