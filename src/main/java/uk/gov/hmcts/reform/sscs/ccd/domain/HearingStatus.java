package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_hearingStatus", generate = true)
@RequiredArgsConstructor
@Getter
public enum HearingStatus {
    @CCD(label = "Hearing is Awaiting a listing")
    AWAITING_LISTING,
    @CCD(label = "Hearing has been Listed")
    LISTED,
    @CCD(label = "Hearing has an Exception")
    EXCEPTION,
    @CCD(label = "Hearing has been Cancelled")
    CANCELLED,
    @CCD(label = "Hearing is Awaiting Actuals")
    AWAITING_ACTUALS,
    @CCD(label = "Hearing is Completed")
    COMPLETED,
    @CCD(label = "Hearing is Adjourned")
    ADJOURNED;
}
