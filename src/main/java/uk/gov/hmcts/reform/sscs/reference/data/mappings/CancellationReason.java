package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CancellationReason {
    
    WITHDRAWN("BBA3-withdrawn", "Withdrawn", null, "BBA3"),
    STRUCK_OUT("BBA3-struckOut", "Struck Out", null, "BBA3"),
    PARTY_UNABLE_TO_ATTEND("BBA3-partyUnableToAttend", "Party unable to attend", null, "BBA3"),
    EXCLUSION("BBA3-exclusion", "Exclusion", null, "BBA3"),
    LAPSED("BBA3-lapsed", "Lapsed", null, "BBA3"),
    OTHER("BBA3-other", "Other", null, "BBA3"),
    NO_LONGER_READY_FOR_HEARING("noLongerReadyForHearing", "No longer ready for hearing", null, null),
    SETTLED("settled", "Settled", null, null),
    JUDICIAL_DIRECTION("judicialDirection", "Judicial direction", null, null),
    FEE_NOT_PAID("feeNotPaid", "Fee not paid", null, null),
    PARTY_DID_NOT_ATTEND("partyDidNotAttend", "Party did not attend", null, null);

    private final String hmcReference;
    private final String value;
    private final String description;
    private final String serviceCode;

}