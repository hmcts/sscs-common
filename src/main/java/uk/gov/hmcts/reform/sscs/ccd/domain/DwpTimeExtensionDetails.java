package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class DwpTimeExtensionDetails {
    private YesNo requested;
    private YesNo granted;

    @JsonCreator
    public DwpTimeExtensionDetails(@JsonProperty("requested") YesNo requested,
                                   @JsonProperty("granted") YesNo granted) {
        this.requested = requested;
        this.granted = granted;
    }
}
