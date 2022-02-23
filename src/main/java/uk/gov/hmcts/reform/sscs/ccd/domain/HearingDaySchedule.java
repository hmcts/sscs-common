package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
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
