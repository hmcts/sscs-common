package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import static uk.gov.hmcts.reform.sscs.reference.data.mappings.HearingChannel.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingSubChannel {

    TELEPHONE_BT_MEET_ME(TELEPHONE,"telephone-btMeetMe", "Telephone - BTMeetme", "", "BBA3"),
    TELEPHONE_CVP(TELEPHONE,"telephone-other", "Telephone - Other", "", "BBA3"),
    TELEPHONE_OTHER(TELEPHONE,"telephone-other", "Telephone - Other", "", "BBA3"),
    TELEPHONE_SKYPE(TELEPHONE,"telephone-skype", "Telephone - Skype", "", "BBA3"),
    VIDEO_CVP(VIDEO,"video-cvp", "Video - CVP", "", "BBA3"),
    VIDEO_CONFERENCE(VIDEO,"video-conference", "Video Conference", "", "BBA3"),
    VIDEO_OTHER(VIDEO,"video-other", "Video - Other", "", "BBA3"),
    VIDEO_SKYPE(VIDEO,"video-skype", "Video - Skype", "", "BBA3"),
    VIDEO_TEAMS(VIDEO,"video-teams", "Video - Teams", "", "BBA3");

    private final HearingChannel hearingChannel;
    private final String subTypeKey;
    private final String valueEn;
    private final String valueCy;
    private final String serviceCode;

    public static HearingSubChannel getHearingSubChannel(String value) {
        for (HearingSubChannel hsc : HearingSubChannel.values()) {
            if (hsc.getValueEn().equals(value)) {
                return hsc;
            }
        }
        return null;
    }
}