package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingChannel {

    TEL("telephone", "Telephone", "Ffôn"),
    VID("video", "Video", "Fideo"),
    INTER("faceToFace", "Face To Face", "Wyneb yn wyneb"),
    NA("notAttending", "Not Attending", null),
    ONPPRS("paper", "Paper", "Papur"),;

    private final String hmcReference;
    private final String valueEn;
    private final String valueCy;

}