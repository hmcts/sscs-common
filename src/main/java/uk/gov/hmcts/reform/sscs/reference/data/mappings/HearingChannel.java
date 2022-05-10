package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingChannel {

    TELEPHONE("telephone", "Telephone", "Ffôn", "BBA3"),
    VIDEO("video", "Video", "Fideo", "BBA3"),
    FACE_TO_FACE("faceToFace", "Face To Face", "Wyneb yn wyneb", "BBA3"),
    NOT_ATTENDING("notAttending", "Not Attending", null, "BBA3");

    private final String hmcReference;
    private final String valueEn;
    private final String valueCy;
    private final String serviceCode;

}