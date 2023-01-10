package uk.gov.hmcts.reform.sscs.reference.data.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingChannel {

    TELEPHONE("TEL", "Telephone", "Ffôn", "telephone"),
    VIDEO("VID", "Video", "Fideo", "video"),
    FACE_TO_FACE("INTER", "Face To Face", "Wyneb yn wyneb", "faceToFace"),
    NOT_ATTENDING("NA", "Not Attending", null, "notAttending"),
    PAPER("ONPPRS", "Paper", "Papur", "paper");

    private final String hmcReference;
    private final String valueEn;
    private final String valueCy;
    private final String valueTribunals;

    @Override
    @JsonValue
    public String toString() {
        return hmcReference;
    }
}
