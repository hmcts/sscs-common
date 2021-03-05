package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.slf4j.LoggerFactory.getLogger;

import uk.gov.hmcts.reform.sscs.exception.BenefitMappingException;

public enum Benefit {

    ESA("Employment and Support Allowance"),
    JSA("Job Seekers Allowance"),
    PIP("Personal Independence Payment"),
    DLA("Personal Independence Payment"),
    UC("Universal Credit");

    private String description;

    private static final org.slf4j.Logger LOG = getLogger(Benefit.class);

    Benefit(String description) {
        this.description = description;
    }

    public static Benefit getBenefitByCode(String code) {
        Benefit b = null;
        for (Benefit type : Benefit.values()) {
            if (type.name().equalsIgnoreCase(code)) {
                b = type;
            }
        }
        if (b == null) {
            BenefitMappingException benefitMappingException =
                    new BenefitMappingException(new Exception(code + " is not a recognised benefit type"));
            LOG.error("Benefit type mapping error", benefitMappingException);
            throw benefitMappingException;
        }
        return b;
    }

    public String getDescription() {
        return description;
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
