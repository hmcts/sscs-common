package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Delegate;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class AppealReason {
    @CCD(ignore = true)
    @Delegate
    private AppealReasonDetails value;

    @JsonCreator
    public AppealReason(@JsonProperty("value") AppealReasonDetails value) {
        this.value = value;
    }

}
