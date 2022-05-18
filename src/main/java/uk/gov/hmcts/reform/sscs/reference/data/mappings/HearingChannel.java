package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingChannel {

    TELEPHONE("telephone", "Telephone", "Ffôn"),
    VIDEO("video", "Video", "Fideo"),
    FACE_TO_FACE("faceToFace", "Face To Face", "Wyneb yn wyneb"),
    NOT_ATTENDING("notAttending", "Not Attending", null);

    private final String hmcReference;
    private final String valueEn;
    private final String valueCy;

}