package uk.gov.hmcts.reform.sscs.reference.data.model;

import static uk.gov.hmcts.reform.sscs.ccd.domain.State.DORMANT_APPEAL_STATE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.State.READY_TO_LIST;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.reform.sscs.ccd.domain.State;

@Getter
@RequiredArgsConstructor
public enum CancellationReason {

    EXCLUSION("BBA3-exclusion", "Exclusion", READY_TO_LIST),
    INCOMPLETE_TRIBUNAL("BBA3-incompleteTribunal", "Incomplete Tribunal", READY_TO_LIST),
    LAPSED("BBA3-lapsed", "Lapsed", DORMANT_APPEAL_STATE),
    LISTED_IN_ERROR("BBA3-listedInError", "Listed In Error", READY_TO_LIST),
    OTHER("BBA3-other", "Other", READY_TO_LIST),
    PARTY_DID_NOT_ATTEND("BBA3-partyDidNotAttend", "Party Did Not Attend", READY_TO_LIST),
    PARTY_UNABLE_TO_ATTEND("BBA3-partyUnableToAttend", "Party unable to attend", READY_TO_LIST),
    STRUCK_OUT("BBA3-struckOut", "Struck Out", DORMANT_APPEAL_STATE),
    WITHDRAWN("BBA3-withdrawn", "Withdrawn", DORMANT_APPEAL_STATE);

    private final String hmcReference;
    private final String label;
    private final State caseStateUpdate;

    public static CancellationReason getCancellationReasonByHmcReference(String value) {
        return Arrays.stream(CancellationReason.values())
                .filter(cancellationReason -> cancellationReason.getHmcReference().equals(value))
                .findFirst()
                .orElse(null);
    }

    public static CancellationReason getCancellationReasonByLabel(String value) {
        return Arrays.stream(CancellationReason.values())
                .filter(cancellationReason -> cancellationReason.getLabel().equals(value))
                .findFirst()
                .orElse(null);
    }

}
