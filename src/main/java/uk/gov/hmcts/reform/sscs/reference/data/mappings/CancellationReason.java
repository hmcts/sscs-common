package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CancellationReason {
    WITHDRAWN("withdrawn", "Withdrawn", "", "BBA3"),
    STRUCK_OUT("struckOut", "Struck Out", "", "BBA3"),
    PARTY_UNABLE_TO_ATTEND("partyUnableToAttend", "Party unable to attend", "", "BBA3"),
    EXCLUSION("exclusion", "Exclusion", "", "BBA3"),
    INCOMPLETE_TRIBUNAL("incompleteTribunal", "Incomplete Tribunal", "", "BBA3"),
    LISTED_IN_ERROR("listedInError", "Listed in error", "", "BBA3"),
    OTHER("other", "Other", "", "BBA3"),
    NO_LONGER_READY_FOR_HEARING("noLongerReadyForHearing", "No longer ready for hearing", "", ""),
    SETTLED("settled", "Settled", "", ""),
    JUDICIAL_DIRECTION("judicialDirection", "Judicial direction", "", ""),
    FEE_NOT_PAID("feeNotPaid", "Fee not paid", "", ""),
    PARTY_DID_NOT_ATTEND("partyDidNotAttend", "Party did not attend", "", ""),
    LAPSED("lapsed", "Lapsed", "", "");

    private final String key;
    private final String value;
    private final String description;
    private final String serviceCode;

    public static CancellationReason getCancellationReason(String value) {
        for (CancellationReason cr : CancellationReason.values()) {
            if (cr.getValue().equals(value)) {
                return cr;
            }
        }
        return null;
    }
}