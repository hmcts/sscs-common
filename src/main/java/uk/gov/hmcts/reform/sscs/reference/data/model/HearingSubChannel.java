package uk.gov.hmcts.reform.sscs.reference.data.model;

import static uk.gov.hmcts.reform.sscs.reference.data.model.HearingChannel.TELEPHONE;
import static uk.gov.hmcts.reform.sscs.reference.data.model.HearingChannel.VIDEO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingSubChannel {

    TELEPHONE_BT_MEET_ME(TELEPHONE, "TELBTM", "Telephone - BTMeetme", null),
    TELEPHONE_CVP(TELEPHONE, "TELCVP", "Telephone - CVP", null),
    TELEPHONE_OTHER(TELEPHONE, "TELOTHER", "Telephone - Other", null),
    TELEPHONE_SKYPE(TELEPHONE, "TELSKYP", "Telephone - Skype", null),
    VIDEO_CVP(VIDEO, "VIDCVP", "Video - CVP", null),
    VIDEO_OTHER(VIDEO, "VIDOTHER", "Video - Other", null),
    VIDEO_SKYPE(VIDEO, "VIDSKYPE", "Video - Skype", null),
    VIDEO_TEAMS(VIDEO, "VIDTEAMS", "Video - Teams", null),
    VIDEO_VHS(VIDEO, "VIDBHS", "Video - Video Hearing Service", null);

    private final HearingChannel hearingChannel;
    private final String hmcReference;
    private final String valueEn;
    private final String valueCy;

}
