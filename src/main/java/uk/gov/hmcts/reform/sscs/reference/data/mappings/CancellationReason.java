package uk.gov.hmcts.reform.sscs.reference.data.mappings;

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
    OTHER("BBA3-other", "Other");

    private final String hmcReference;
    private final String value;

}