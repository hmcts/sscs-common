package uk.gov.hmcts.reform.sscs.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AirlookupBenefitToVenue {

    private String pipOrUcVenue;
    private String esaVenue;

    public String getPipOrUcVenue() {
        return pipOrUcVenue.trim();
    }

    public String getEsaVenue() {
        return esaVenue.trim();
    }

}
