package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.Arrays;
import java.util.Objects;

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

    public static AdjournCaseTypeOfHearing getTypeOfHearingByCcdDefinition(String ccdDefinition) {
        return Arrays.stream(AdjournCaseTypeOfHearing.values())
            .filter(adjournCaseTypeOfHearing -> Objects.equals(adjournCaseTypeOfHearing.ccdDefinition, ccdDefinition))
            .findFirst()
            .orElse(null);
    }

    public static AdjournCaseTypeOfHearing getTypeOfHearingByHearingChannel(HearingChannel hearingChannel) {
        return Arrays.stream(AdjournCaseTypeOfHearing.values())
            .filter(adjournCaseTypeOfHearing -> Objects.equals(adjournCaseTypeOfHearing.hearingChannel, hearingChannel))
            .findFirst()
            .orElse(null);
    }
}
