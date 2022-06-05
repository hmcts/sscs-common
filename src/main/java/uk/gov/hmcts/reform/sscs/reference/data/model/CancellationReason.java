package uk.gov.hmcts.reform.sscs.reference.data.model;

import static uk.gov.hmcts.reform.sscs.ccd.domain.State.DORMANT_APPEAL_STATE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.State.NOT_LISTABLE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.State.READY_TO_LIST;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.reform.sscs.ccd.domain.State;

@Getter
@RequiredArgsConstructor
public enum CancellationReason {

    WITHDRAWN("withdraw", "Withdrawn", DORMANT_APPEAL_STATE),
    STRUCK_OUT("struck", "Struck Out", DORMANT_APPEAL_STATE),
    PARTY_UNABLE_TO_ATTEND("unable", "Party Unable To Attend", READY_TO_LIST),
    EXCLUSION("exclusio", "Exclusion", READY_TO_LIST),
    INCOMPLETE_TRIBUNAL("incompl", "Incomplete Tribunal", READY_TO_LIST),
    LISTED_IN_ERROR("listerr", "Listed In Error", READY_TO_LIST),
    OTHER("other", "Other", READY_TO_LIST),
    NOT_READY("notready", "No longer ready for hearing", NOT_LISTABLE),
    SETTLED("settled", "Settled", NOT_LISTABLE),
    JUDICIAL_DIRECTION("jodir", "Judicial direction", NOT_LISTABLE),
    FEE_NOT_PAID("notpaid", "Fee not paid", NOT_LISTABLE),
    PARTY_DID_NOT_ATTEND("notatt", "Party Did Not Attend", READY_TO_LIST),
    LAPSED("lapsed", "Lapsed", DORMANT_APPEAL_STATE);

    @JsonValue
    private final String hmcReference;
    private final String label;
    private final State caseStateUpdate;
}
