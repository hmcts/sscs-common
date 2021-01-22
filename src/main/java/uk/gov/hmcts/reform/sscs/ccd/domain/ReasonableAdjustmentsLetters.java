package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class ReasonableAdjustmentsLetters {

    private List<Correspondence> appellantReasonableAdjustmentsLetters;
    private List<Correspondence> appointeeReasonableAdjustmentsLetters;
    private List<Correspondence> representativeReasonableAdjustmentsLetters;
    private List<Correspondence> jointPartyReasonableAdjustmentsLetters;

    public ReasonableAdjustmentsLetters(@JsonProperty("appellantReasonableAdjustmentsLetters") List<Correspondence> appellantReasonableAdjustmentsLetters,
                                 @JsonProperty("appointeeReasonableAdjustmentsLetters") List<Correspondence> appointeeReasonableAdjustmentsLetters,
                                 @JsonProperty("representativeReasonableAdjustmentsLetters") List<Correspondence> representativeReasonableAdjustmentsLetters,
                                 @JsonProperty("jointPartyReasonableAdjustmentsLetters") List<Correspondence> jointPartyReasonableAdjustmentsLetters) {
        this.appellantReasonableAdjustmentsLetters = appellantReasonableAdjustmentsLetters;
        this.appointeeReasonableAdjustmentsLetters = appointeeReasonableAdjustmentsLetters;
        this.representativeReasonableAdjustmentsLetters = representativeReasonableAdjustmentsLetters;
        this.jointPartyReasonableAdjustmentsLetters = jointPartyReasonableAdjustmentsLetters;
    }
}
