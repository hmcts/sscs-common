package uk.gov.hmcts.reform.sscs.reference.data.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CancellationReason {

    WITHDRAWN("BBA3-withdrawn", "Withdrawn", "withdraw"),
    STRUCK_OUT("BBA3-struckOut", "Struck Out", "struck"),
    PARTY_UNABLE_TO_ATTEND("BBA3-partyUnableToAttend", "Party unable to attend", "unable"),
    EXCLUSION("BBA3-exclusion", "Exclusion", "exclusio"),
    LAPSED("BBA3-lapsed", "Lapsed", "lapsed"),
    OTHER("BBA3-other", "Other", "other");

    private final String hmcReference;
    private final String value;
    private final String cancellationCode;

}
