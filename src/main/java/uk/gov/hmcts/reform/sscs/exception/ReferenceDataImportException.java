package uk.gov.hmcts.reform.sscs.exception;

public class ReferenceDataImportException extends RuntimeException {
    private static final long serialVersionUID = 3597563538346171249L;

    public ReferenceDataImportException(String message, Throwable cause) {
        super(message,cause);
    }
}
