package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingChannel {
    TELEPHONE("TEL", "Telephone", "Ffôn", "BBA3"),
    VIDEO("VID", "Video", "Fideo", "BBA3"),
    FACE_TO_FACE("INTER", "Face To Face", "Wyneb yn wyneb", "BBA3"),
    NOT_ATTENDING("ONPPRS", "Not Attending", "", "BBA3");

    private final String key;
    private final String valueEN;
    private final String valueCY;
    private final String serviceCode;

    public static HearingChannel getHearingChannel(String value) {
        for (HearingChannel hc : HearingChannel.values()) {
            if (hc.getValueEN().equals(value)) {
                return hc;
            }
        }
        return null;
    }
}
