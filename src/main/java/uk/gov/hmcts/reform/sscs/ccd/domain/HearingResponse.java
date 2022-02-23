package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
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
    private String hmcStatus;
    private Number version;

    private LocalDateTime partiesNotifiedDateTime;
    private String listAssistTransactionId;
    private LocalDateTime receivedDateTime;
    private String hearingListingStatus;
    private String listAssistCaseStatus;
    private String hearingCancellationReason;

    private HearingDaySchedule hearingDaySchedule;
}
