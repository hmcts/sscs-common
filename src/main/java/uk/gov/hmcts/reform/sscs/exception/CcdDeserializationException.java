package uk.gov.hmcts.reform.sscs.exception;

import uk.gov.hmcts.reform.logging.exception.AlertLevel;
import uk.gov.hmcts.reform.logging.exception.UnknownErrorCodeException;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class CcdDeserializationException extends UnknownErrorCodeException {
    public CcdDeserializationException(Throwable cause) {
        super(AlertLevel.P3, cause);
    }
}
