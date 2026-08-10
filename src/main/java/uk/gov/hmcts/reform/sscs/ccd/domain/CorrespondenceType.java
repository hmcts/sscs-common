package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "correspondenceType", generate = true)
@Getter
public enum CorrespondenceType {
    Email,
    Letter,
    @CCD(label = "SMS")
    Sms
}
