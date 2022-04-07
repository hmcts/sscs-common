package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingSubChannel {
    TELEPHONE_BT_MEET_ME("telephone","telephone-btMeetMe", "Telephone - BTMeetme", "", "BBA3"),
    TELEPHONE_CVP("telephone","telephone-other", "Telephone - Other", "", "BBA3"),
    TELEPHONE_OTHER("telephone","telephone-other", "Telephone - Other", "", "BBA3"),
    TELEPHONE_SKYPE("telephone","telephone-skype", "Telephone - Skype", "", "BBA3"),
    VIDEO_CVP("video","video-cvp", "Video - CVP", "", "BBA3"),
    VIDEO_CONFERENCE("video","video-conference", "Video Conference", "", "BBA3"),
    VIDEO_OTHER("video","video-other", "Video - Other", "", "BBA3"),
    VIDEO_SKYPE("video","video-skype", "Video - Skype", "", "BBA3"),
    VIDEO_TEAMS("video","video-teams", "Video - Teams", "", "BBA3");

    private final String key;
    private final String subTypeKey;
    private final String valueEN;
    private final String valueCY;
    private final String serviceCode;

    public static HearingSubChannel getHearingSubChannel(String value) {
        for (HearingSubChannel hsc : HearingSubChannel.values()) {
            if (hsc.getValueEN().equals(value)) {
                return hsc;
            }
        }
        return null;
    }
}




