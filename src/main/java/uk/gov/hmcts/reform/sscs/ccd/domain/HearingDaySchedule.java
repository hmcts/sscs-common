package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class HearingDaySchedule {
    private String hearingStartDateTime;
    private String hearingEndDateTime;
    private String listAssistSessionId;
    private String hearingVenueId;
    private String hearingRoomId;
    private String hearingJudgeId;
    private String panelMemberId;
    private List<Attendee> attendees;

}
