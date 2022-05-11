package uk.gov.hmcts.reform.sscs.exception;

public class HearingDurationImportException extends RuntimeException {
    private static final long serialVersionUID = 3597563538346171249L;

    public HearingDurationImportException(String message, Throwable cause) {
        super(message,cause);
    }
}
