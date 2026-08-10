package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.reform.sscs.reference.data.model.HearingChannel;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_adjournCaseTypeOfHearing", generate = true)
@Getter
@AllArgsConstructor
public enum AdjournCaseTypeOfHearing {
    @CCD(label = "Paper")
    PAPER("paper", HearingChannel.PAPER, "Paper"),
    @CCD(label = "Video")
    VIDEO("video", HearingChannel.VIDEO, "Video"),
    @CCD(label = "Telephone")
    TELEPHONE("telephone", HearingChannel.TELEPHONE,"Telephone"),
    @CCD(label = "Face to face")
    FACE_TO_FACE("faceToFace", HearingChannel.FACE_TO_FACE, "Face to face");

    private final String ccdDefinition;
    private final HearingChannel hearingChannel;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }

}
