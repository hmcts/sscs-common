package uk.gov.hmcts.reform.sscs.exception;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@Slf4j
public class BenefitMappingException extends RuntimeException {

    public BenefitMappingException(Exception ex) {
        super(ex);
    }

    public static BenefitMappingException createException(String code) {
        BenefitMappingException benefitMappingException =
                new BenefitMappingException(new Exception(code + " is not a recognised benefit type"));
        log.error("Benefit type mapping error", benefitMappingException);
        return benefitMappingException;
    }
}
