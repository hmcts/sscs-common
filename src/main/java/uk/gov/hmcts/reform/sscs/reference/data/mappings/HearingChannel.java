package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingChannel {
    TELEPHONE("telephone", "Telephone"),
    VIDEO("video", "Video"),
    FACE_TO_FACE("faceToFace", "Face To Face"),
    NOT_ATTENDING("notAttending", "Not Attending");

    private final String key;
    private final String value;

    public static HearingChannel getHearingChannel(String value) {
        for (HearingChannel hc : HearingChannel.values()) {
            if (hc.getValue().equals(value)) {
                return hc;
            }
        }
        return null;
    }
}
