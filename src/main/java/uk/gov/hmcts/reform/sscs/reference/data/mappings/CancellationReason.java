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
    OTHER("BBA3-other", "Other", null, "BBA3");

    private final String hmcReference;
    private final String value;
    private final String description;
    private final String serviceCode;

}