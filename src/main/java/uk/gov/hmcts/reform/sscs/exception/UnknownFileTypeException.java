package uk.gov.hmcts.reform.sscs.exception;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class UnknownFileTypeException extends RuntimeException {

    public UnknownFileTypeException(String message, Throwable cause) {
        super(cause);
    }
}
