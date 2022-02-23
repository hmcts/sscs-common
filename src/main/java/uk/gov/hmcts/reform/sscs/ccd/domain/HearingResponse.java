package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class HearingResponse {
    private String hearingRequestId;
    private String status;
    private String versionNumber;

    private String partiesNotifiedDateTime;
    private String listAssistTransactionId;
    private String receivedDateTime;
    private String responseVersion;
    private String laCaseStatus;
    private String listingStatus;
    private String hearingCancellationReason;

    private HearingDaySchedule hearingDaySchedule;
}
