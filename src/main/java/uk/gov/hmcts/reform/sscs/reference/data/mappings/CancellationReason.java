package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CancellationReason {

    WITHDRAWN("BBA3-withdrawn", "Withdrawn"),
    STRUCK_OUT("BBA3-struckOut", "Struck Out"),
    PARTY_UNABLE_TO_ATTEND("BBA3-partyUnableToAttend", "Party unable to attend"),
    EXCLUSION("BBA3-exclusion", "Exclusion"),
    LAPSED("BBA3-lapsed", "Lapsed"),
    OTHER("BBA3-other", "Other"),
    INCOMPLETE_TRIBUNAL("BBA3-incompleteTribunal", "Incomplete Tribunal"),
    LISTED_IN_ERROR("BBA3-listedInError", "Listed In Error"),
    PARTY_DID_NOT_ATTEND("BBA3-partyDidNotAttend", "Party Did Not Attend");

    private final String hmcReference;
    private final String value;

    public static CancellationReason getCancellationReasonByValue(String value) {
        return Arrays.stream(CancellationReason.values())
                .filter(sl -> sl.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }
}