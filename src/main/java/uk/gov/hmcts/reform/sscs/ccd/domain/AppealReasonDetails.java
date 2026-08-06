package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "appealReason", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class AppealReasonDetails {
    @CCD(label = "Reason")
    private String reason;
    @CCD(label = "Description")
    private String description;

    @JsonCreator
    public AppealReasonDetails(@JsonProperty("reason") String reason,
                               @JsonProperty("description") String description) {
        this.reason = reason;
        this.description = description;
    }
}
