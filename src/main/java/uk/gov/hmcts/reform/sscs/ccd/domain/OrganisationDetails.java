package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class OrganisationDetails implements PartyDetails {
    private String name;
    private String organisationType;
    private String cftOrganisationId;

    public String getName() {
        return name;
    }
}
