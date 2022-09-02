package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdjournCaseTypeOfNextHearing {

    PAPER("Paper"),
    VIDEO("Video"),
    TELEPHONE("Telephone"),
    FACE_TO_FACE("Face to face");

    private final String hearingType;

    @Override
    @JsonValue
    public String toString() {
        return hearingType;
    }

}
