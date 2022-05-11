package uk.gov.hmcts.reform.sscs.exception;

public class SessionCategoryMapImportException extends RuntimeException {
    private static final long serialVersionUID = 6398697603853647363L;

    public SessionCategoryMapImportException(String message, Throwable cause) {
        super(message,cause);
    }
}
