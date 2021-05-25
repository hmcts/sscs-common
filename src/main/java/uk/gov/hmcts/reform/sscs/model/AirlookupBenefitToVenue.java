package uk.gov.hmcts.reform.sscs.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AirlookupBenefitToVenue {

    private String pipVenue;
    private String esaOrUcVenue;
    private String jsaVenue;

    public String getPipVenue() {
        return pipVenue.trim();
    }

    public String getEsaOrUcVenue() {
        return esaOrUcVenue.trim();
    }

    public String getJsaVenue() {
        return jsaVenue.trim();
    }

}
