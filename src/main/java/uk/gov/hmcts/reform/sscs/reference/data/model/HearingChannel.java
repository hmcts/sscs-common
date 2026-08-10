package uk.gov.hmcts.reform.sscs.reference.data.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_hearingChannel", generate = true)
@Getter
@RequiredArgsConstructor
public enum HearingChannel {

    @CCD(label = "Telephone")
    TELEPHONE("TEL", "Telephone", "Ffôn", "telephone"),
    @CCD(label = "Video")
    VIDEO("VID", "Video", "Fideo", "video"),
    @CCD(label = "Face To Face")
    FACE_TO_FACE("INTER", "Face To Face", "Wyneb yn wyneb", "faceToFace"),
    @CCD(label = "Not Attending")
    NOT_ATTENDING("NA", "Not Attending", null, "notAttending"),
    @CCD(label = "Paper")
    PAPER("ONPPRS", "Paper", "Papur", "paper"),;

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

