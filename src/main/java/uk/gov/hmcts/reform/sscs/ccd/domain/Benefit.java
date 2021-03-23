package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.slf4j.LoggerFactory.getLogger;

import uk.gov.hmcts.reform.sscs.exception.BenefitMappingException;

public enum Benefit {

    ESA("Employment and Support Allowance", "051", "ESA"),
    JSA("Job Seekers Allowance", "", "JSA"),
    PIP("Personal Independence Payment", "002", "PIP"),
    DLA("Disability Living Allowance", "037", "DLA"),
    UC("Universal Credit", "001", "UC"),
    CARERS_ALLOWANCE("Carer's Allowance", "070", "carersAllowance");

    private String description;
    private String benefitCode;
    private String shortName;

    private static final org.slf4j.Logger LOG = getLogger(Benefit.class);

    Benefit(String description, String benefitCode, String shortName) {
        this.description = description;
        this.benefitCode = benefitCode;
        this.shortName = shortName;
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

    public static Benefit findBenefitByShortName(String code) {
        for (Benefit type : Benefit.values()) {
            if (type.getShortName().equalsIgnoreCase(code)) {
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

    private String getShortName() {
        return shortName;
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
