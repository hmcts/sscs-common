package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class HearingLocation {
    private String locationType;
    private LocationId locationId;

    public enum LocationId {
        court,
        cluster,
        region
    }
}
