package uk.gov.hmcts.reform.sscs.model.hearings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import uk.gov.hmcts.reform.sscs.ccd.domain.HearingRoute;
import uk.gov.hmcts.reform.sscs.ccd.domain.HearingState;
import uk.gov.hmcts.reform.sscs.model.servicebus.SessionAwareRequest;
import uk.gov.hmcts.reform.sscs.reference.data.mappings.CancellationReason;

@Data
@Builder(builderMethodName = "internalBuilder")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HearingRequest implements SessionAwareRequest {

    @NonNull
    private String ccdCaseId;

    private HearingRoute hearingRoute;

    private HearingState hearingState;

    private CancellationReason cancellationReason;

    public static HearingRequestBuilder builder(String ccdCaseId) {
        return internalBuilder().ccdCaseId(ccdCaseId);
    }

    @Override
    @JsonIgnore
    public String getSessionId() {
        return ccdCaseId;
    }

    public static class HearingRequestBuilder {}
}
