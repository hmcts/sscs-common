package uk.gov.hmcts.reform.sscs.ccd.exception;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class CreateCcdCaseException extends RuntimeException {

    public CreateCcdCaseException(String message, Throwable cause) {

        super(cause.getMessage());
    }
}
