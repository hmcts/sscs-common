package uk.gov.hmcts.reform.sscs.exception;

public class SessionDetailsLookUpException extends RuntimeException {

    public SessionDetailsLookUpException(Throwable cause) {
        super(cause);
    }

    public SessionDetailsLookUpException(String error) {
        super(error);
    }
}
