package uk.gov.hmcts.reform.sscs.reference.data.model;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CancellationReason {

    EXCLUSION("BBA3-exclusion", "Exclusion"),
    INCOMPLETE_TRIBUNAL("BBA3-incompleteTribunal", "Incomplete Tribunal"),
    LAPSED("BBA3-lapsed", "Lapsed"),
    LISTED_IN_ERROR("BBA3-listedInError", "Listed In Error"),
    OTHER("BBA3-other", "Other"),
    PARTY_DID_NOT_ATTEND("BBA3-partyDidNotAttend", "Party Did Not Attend"),
    PARTY_UNABLE_TO_ATTEND("BBA3-partyUnableToAttend", "Party unable to attend"),
    STRUCK_OUT("BBA3-struckOut", "Struck Out"),
    WITHDRAWN("BBA3-withdrawn", "Withdrawn");

    private final String hmcReference;
    private final String label;

    public static CancellationReason getCancellationReasonByHmcReference(String value) {
        return Arrays.stream(CancellationReason.values())
                .filter(sl -> sl.getHmcReference().equals(value))
                .findFirst()
                .orElse(null);
    }

    public static CancellationReason getCancellationReasonByLabel(String value) {
        return Arrays.stream(CancellationReason.values())
                .filter(sl -> sl.getLabel().equals(value))
                .findFirst()
                .orElse(null);
    }

}
