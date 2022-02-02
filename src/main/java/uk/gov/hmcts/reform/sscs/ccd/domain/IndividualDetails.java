package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class IndividualDetails implements PartyDetails {
    private Name name;
    private Identity identity;

    private List<String> reasonableAdjustments;

    private YesNo vulnerableFlag;
    private String vulnerabilityDetails;
    private YesNo confidentialityRequired;
    private YesNo unacceptableCustomerBehaviour;

    private ReasonableAdjustmentDetails reasonableAdjustment;

    public String getName() {
        return name.getFullName();
    }
}
