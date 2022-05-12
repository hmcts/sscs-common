package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import static uk.gov.hmcts.reform.sscs.reference.data.mappings.HearingChannel.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingSubChannel {

    TELEPHONE_BT_MEET_ME(TELEPHONE, "telephone-btMeetMe", "Telephone - BTMeetme", null, "BBA3"),
    TELEPHONE_CVP(TELEPHONE, "telephone-cvp", "Telephone - CVP", null, "BBA3"),
    TELEPHONE_OTHER(TELEPHONE, "telephone-other", "Telephone - Other", null, "BBA3"),
    TELEPHONE_SKYPE(TELEPHONE, "telephone-skype", "Telephone - Skype", null, "BBA3"),
    VIDEO_CVP(VIDEO, "video-cvp", "Video - CVP", null, "BBA3"),
    VIDEO_CONFERENCE(VIDEO, "video-conference", "Video - Conference", null, "BBA3"),
    VIDEO_OTHER(VIDEO, "video-other", "Video - Other", null, "BBA3"),
    VIDEO_SKYPE(VIDEO, "video-skype", "Video - Skype", null, "BBA3"),
    VIDEO_TEAMS(VIDEO, "video-teams", "Video - Teams", null, "BBA3");

    private final HearingChannel hearingChannel;
    private final String hmcReference;
    private final String valueEn;
    private final String valueCy;
    private final String serviceCode;

}