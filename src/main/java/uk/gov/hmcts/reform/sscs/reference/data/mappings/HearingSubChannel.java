package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import static uk.gov.hmcts.reform.sscs.reference.data.mappings.HearingChannel.TEL;
import static uk.gov.hmcts.reform.sscs.reference.data.mappings.HearingChannel.VID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingSubChannel {

    TELEPHONE_BT_MEET_ME(TEL, "TELBTM", "Telephone - BTMeetme", null),
    TELEPHONE_CVP(TEL, "TELCVP", "Telephone - CVP", null),
    TELEPHONE_OTHER(TEL, "TELOTHER", "Telephone - Other", null),
    TELEPHONE_SKYPE(TEL, "TELSKYP", "Telephone - Skype", null),
    VIDEO_CVP(VID, "VIDCVP", "Video - CVP", null),
    VIDEO_OTHER(VID, "VIDOTHER", "Video - Other", null),
    VIDEO_SKYPE(VID, "VIDSKYPE", "Video - Skype", null),
    VIDEO_TEAMS(VID, "VIDTEAMS", "Video - Teams", null),
    VIDEO_VHS(VID, "VIDBHS", "Video - Video Hearing Service", null);

    private final HearingChannel hearingChannel;
    private final String hmcReference;
    private final String valueEn;
    private final String valueCy;

}
