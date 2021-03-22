package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.slf4j.LoggerFactory.getLogger;

import uk.gov.hmcts.reform.sscs.exception.BenefitMappingException;

public enum Benefit {

    ESA("Employment and Support Allowance", "051"),
    JSA("Job Seekers Allowance", ""),
    PIP("Personal Independence Payment", "002"),
    DLA("Disability Living Allowance", "037"),
    UC("Universal Credit", "001"),
    CARERS_ALLOWANCE("Carer's Allowance", "070");

    private String description;
    private String benefitCode;

    private static final org.slf4j.Logger LOG = getLogger(Benefit.class);

    Benefit(String description, String benefitCode) {
        this.description = description;
        this.benefitCode = benefitCode;
    }

    public static Benefit getBenefitByCode(String code) {
        Benefit benefit = findBenefitByShortName(code);
        if (benefit == null) {
            benefit = findBenefitByDescription(code);
            if (benefit == null) {
                BenefitMappingException benefitMappingException =
                        new BenefitMappingException(new Exception(code + " is not a recognised benefit type"));
                LOG.error("Benefit type mapping error", benefitMappingException);
                throw benefitMappingException;
            }
        }
        return benefit;
    }

    private static Benefit findBenefitByShortName(String code) {
        for (Benefit type : Benefit.values()) {
            if (type.name().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    private static Benefit findBenefitByDescription(String code) {
        for (Benefit type : Benefit.values()) {
            if (type.getDescription().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    public String getBenefitCode() {
        return benefitCode;
    }

    public static Boolean isBenefitTypeValid(String field) {
        for (Benefit benefit : Benefit.values()) {
            if (benefit.toString().toLowerCase().equals(field.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
