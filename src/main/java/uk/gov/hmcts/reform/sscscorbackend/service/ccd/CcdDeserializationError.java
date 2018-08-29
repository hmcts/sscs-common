package uk.gov.hmcts.reform.sscscorbackend.service.ccd;

import uk.gov.hmcts.reform.logging.exception.AlertLevel;
import uk.gov.hmcts.reform.logging.exception.UnknownErrorCodeException;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class CcdDeserializationError extends UnknownErrorCodeException {
    public CcdDeserializationError(Throwable cause) {
        super(AlertLevel.P3, cause);
    }
}
