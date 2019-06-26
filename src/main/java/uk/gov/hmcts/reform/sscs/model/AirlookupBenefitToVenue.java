package uk.gov.hmcts.reform.sscs.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AirlookupBenefitToVenue {

    private String pipVenue;
    private String esaVenue;

    public String getPipVenue() {
        return pipVenue.trim();
    }

    public String getEsaVenue() {
        return esaVenue.trim();
    }

}
