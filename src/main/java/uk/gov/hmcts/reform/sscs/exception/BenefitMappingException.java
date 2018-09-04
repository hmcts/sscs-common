package uk.gov.hmcts.reform.sscs.exception;

import uk.gov.hmcts.reform.logging.exception.AlertLevel;
import uk.gov.hmcts.reform.logging.exception.UnknownErrorCodeException;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class BenefitMappingException extends UnknownErrorCodeException {

    public BenefitMappingException(Exception ex) {
        super(AlertLevel.P3, ex);
    }
}
