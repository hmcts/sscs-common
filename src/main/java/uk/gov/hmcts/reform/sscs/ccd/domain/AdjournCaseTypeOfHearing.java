package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.reform.sscs.reference.data.model.HearingChannel;

@Getter
@AllArgsConstructor
public enum AdjournCaseTypeOfHearing {
    PAPER("paper", HearingChannel.PAPER, "Paper"),
    VIDEO("video", HearingChannel.VIDEO, "Video"),
    TELEPHONE("telephone", HearingChannel.TELEPHONE,"Telephone"),
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
