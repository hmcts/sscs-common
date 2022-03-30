package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CancellationReason {
    WITHDRAWN("withdrawn", "Withdrawn"),
    STRUCK_OUT("struckOut", "Struck Out"),
    PARTY_UNABLE_TO_ATTEND("partyUnableToAttend", "Party unable to attend"),
    EXCLUSION("exclusion", "Exclusion"),
    INCOMPLETE_TRIBUNAL("incompleteTribunal", "Incomplete Tribunal"),
    LISTED_IN_ERROR("listedInError", "Listed in error"),
    OTHER("other", "Other"),
    NO_LONGER_READY_FOR_HEARING("noLongerReadyForHearing", "No longer ready for hearing"),
    SETTLED("settled", "Settled"),
    JUDICIAL_DIRECTION("judicialDirection", "Judicial direction"),
    FEE_NOT_PAID("feeNotPaid", "Fee not paid"),
    PARTY_DID_NOT_ATTEND("partyDidNotAttend", "Party did not attend"),
    LAPSED("lapsed", "Lapsed");

    private final String key;
    private final String value;

    public static CancellationReason getCancellationReason(String value) {
        for (CancellationReason cr : CancellationReason.values()) {
            if (cr.getValue().equals(value)) {
                return cr;
            }
        }
        return null;
    }
}
