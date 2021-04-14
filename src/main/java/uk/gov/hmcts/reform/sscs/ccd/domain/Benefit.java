package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.slf4j.LoggerFactory.getLogger;

import uk.gov.hmcts.reform.sscs.exception.BenefitMappingException;

public enum Benefit {

    ESA("Employment and Support Allowance", "Lwfans Cyflogaeth a Chymorth", "051", "ESA", true),
    JSA("Job Seekers Allowance", "", "", "JSA", true),
    PIP("Personal Independence Payment", "Taliad Annibyniaeth Personol", "002", "PIP", true),
    DLA("Disability Living Allowance", "","037", "DLA", true),
    UC("Universal Credit", "Credyd Cynhwysol", "001", "UC", true),
    CARERS_ALLOWANCE("Carer's Allowance", "Lwfans Gofalwr", "070", "carersAllowance", false);

    private String description;
    private String welshDescription;
    private String benefitCode;
    private String shortName;
    private boolean hasAcronym;

    private static final org.slf4j.Logger LOG = getLogger(Benefit.class);

    Benefit(String description, String welshDescription, String benefitCode, String shortName, boolean hasAcronym) {
        this.description = description;
        this.welshDescription = welshDescription;
        this.benefitCode = benefitCode;
        this.shortName = shortName;
        this.hasAcronym = hasAcronym;
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

    public static Benefit findBenefitByDescription(String code) {
        for (Benefit type : Benefit.values()) {
            if (type.getDescription().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    public String getShortName() {
        return shortName;
    }

    public String getDescription() {
        return description;
    }

    public String getWelshDescription() {
        return welshDescription;
    }

    public String getBenefitCode() {
        return benefitCode;
    }

    public boolean isHasAcronym() {
        return hasAcronym;
    }

    public static Boolean isBenefitTypeValid(String field) {
        for (Benefit benefit : Benefit.values()) {
            if (benefit.toString().toLowerCase().equals(field.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static String getLongBenefitNameDescriptionWithOptionalAcronym(String code, boolean isEnglish) {
        Benefit benefit = getBenefitByCode(code);

        String description = isEnglish ? benefit.getDescription() : benefit.getWelshDescription();
        String shortName = benefit.isHasAcronym() ? " (" + benefit.getShortName() + ")" : "";

        return description + shortName;
    }

}
