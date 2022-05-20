package uk.gov.hmcts.reform.sscs.reference.data.model;

import static uk.gov.hmcts.reform.sscs.reference.data.model.HearingChannel.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingSubChannel {

    TELEPHONE_BT_MEET_ME(TELEPHONE, "telephone-btMeetMe", "Telephone - BTMeetme", null),
    TELEPHONE_CVP(TELEPHONE, "telephone-cvp", "Telephone - CVP", null),
    TELEPHONE_OTHER(TELEPHONE, "telephone-other", "Telephone - Other", null),
    TELEPHONE_SKYPE(TELEPHONE, "telephone-skype", "Telephone - Skype", null),
    VIDEO_CVP(VIDEO, "video-cvp", "Video - CVP", null),
    VIDEO_CONFERENCE(VIDEO, "video-conference", "Video - Conference", null),
    VIDEO_OTHER(VIDEO, "video-other", "Video - Other", null),
    VIDEO_SKYPE(VIDEO, "video-skype", "Video - Skype", null),
    VIDEO_TEAMS(VIDEO, "video-teams", "Video - Teams", null);

    private final HearingChannel hearingChannel;
    private final String hmcReference;
    private final String valueEn;
    private final String valueCy;

}
