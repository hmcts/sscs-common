package uk.gov.hmcts.reform.sscs.ccd.exception;

import uk.gov.hmcts.reform.logging.exception.AlertLevel;
import uk.gov.hmcts.reform.logging.exception.UnknownErrorCodeException;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class CcdException extends UnknownErrorCodeException {
    public CcdException(String message) {
        super(AlertLevel.P4, message);
    }

    public CcdException(String message, Throwable ex) {
        super(AlertLevel.P4, message, ex);
    }
}