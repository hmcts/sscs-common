package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum HearingStatus {
    AWAITING_LISTING,
    LISTED,
    EXCEPTION,
    CANCELLED,
    AWAITING_ACTUALS,
    COMPLETED,
    ADJOURNED;
}
