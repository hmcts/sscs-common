package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import static uk.gov.hmcts.reform.sscs.reference.data.mappings.HearingChannel.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingSubChannel {

    TELEPHONE_BT_MEET_ME(TEL, "telephone-btMeetMe", "Telephone - BTMeetme", null),
    TELEPHONE_CVP(TEL, "telephone-cvp", "Telephone - CVP", null),
    TELEPHONE_OTHER(TEL, "telephone-other", "Telephone - Other", null),
    TELEPHONE_SKYPE(TEL, "telephone-skype", "Telephone - Skype", null),
    VIDEO_CVP(VID, "video-cvp", "Video - CVP", null),
    VIDEO_CONFERENCE(VID, "video-conference", "Video - Conference", null),
    VIDEO_OTHER(VID, "video-other", "Video - Other", null),
    VIDEO_SKYPE(VID, "video-skype", "Video - Skype", null),
    VIDEO_TEAMS(VID, "video-teams", "Video - Teams", null);

    private final HearingChannel hearingChannel;
    private final String hmcReference;
    private final String valueEn;
    private final String valueCy;

}