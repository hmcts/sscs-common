package uk.gov.hmcts.reform.sscs.ccd.exception;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class CcdException extends RuntimeException {
    public CcdException(String message) {
        super(message);
    }

    public CcdException(String message, Throwable ex) {
        super(message, ex);
    }
}