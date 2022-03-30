package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingSubChannel {
    TELEPHONE_BT_MEET_ME("telephone-btMeetMe", "Telephone - BTMeetme"),
    TELEPHONE_CVP("telephone-other", "Telephone - Other"),
    TELEPHONE_OTHER("telephone-other", "Telephone - Other"),
    TELEPHONE_SKYPE("telephone-skype", "Telephone - Skype"),
    VIDEO_CVP("video-cvp", "Video - CVP"),
    VIDEO_CONFERENCE("video-conference", "Video Conference"),
    VIDEO_OTHER("video-other", "Video - Other"),
    VIDEO_SKYPE("video-skype", "Video - Skype"),
    VIDEO_TEAMS("video-teams", "Video - Teams");

    private final String key;
    private final String value;

    public static HearingSubChannel getHearingSubChannel(String value) {
        for (HearingSubChannel hsc : HearingSubChannel.values()) {
            if (hsc.getValue().equals(value)) {
                return hsc;
            }
        }
        return null;
    }
}




