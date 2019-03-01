package uk.gov.hmcts.reform.sscs.ccd.exception;

public class RequiredFieldMissingException extends RuntimeException {
    public RequiredFieldMissingException(String message) {
        super(message);
    }
}
