package uk.gov.hmcts.reform.sscs.exception;


@SuppressWarnings("squid:MaximumInheritanceDepth")
public class BenefitMappingException extends RuntimeException {

    public BenefitMappingException(Exception ex) {

        super(ex.getMessage());
    }
}
