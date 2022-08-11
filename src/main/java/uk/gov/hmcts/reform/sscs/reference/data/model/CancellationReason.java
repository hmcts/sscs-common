package uk.gov.hmcts.reform.sscs.reference.data.model;

import static uk.gov.hmcts.reform.sscs.ccd.domain.State.DORMANT_APPEAL_STATE;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.reform.sscs.ccd.domain.State;

@Getter
@RequiredArgsConstructor
public enum CancellationReason {

    WITHDRAWN("withdraw", "Withdrawn", DORMANT_APPEAL_STATE),
    STRUCK_OUT("struck", "Struck Out", DORMANT_APPEAL_STATE),
    PARTY_UNABLE_TO_ATTEND("unable", "Party Unable To Attend", null),
    EXCLUSION("exclusio", "Exclusion", null),
    INCOMPLETE_TRIBUNAL("incompl", "Incomplete Tribunal", null),
    LISTED_IN_ERROR("listerr", "Listed In Error", null),
    OTHER("other", "Other", null),
    NOT_READY("notready", "No longer ready for hearing", null),
    SETTLED("settled", "Settled", null),
    JUDICIAL_DIRECTION("jodir", "Judicial direction", null),
    FEE_NOT_PAID("notpaid", "Fee not paid", null),
    PARTY_DID_NOT_ATTEND("notatt", "Party Did Not Attend", null),
    LAPSED("lapsed", "Lapsed", DORMANT_APPEAL_STATE);

    private final String hmcReference;
    private final String label;
    private final State caseStateUpdate;

    @Override
    @JsonValue
    public String toString() {
        return hmcReference;
    }
}
