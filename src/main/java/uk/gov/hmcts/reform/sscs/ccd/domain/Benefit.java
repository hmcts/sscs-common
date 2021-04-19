package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.Getter;
import uk.gov.hmcts.reform.sscs.exception.BenefitMappingException;
import uk.gov.hmcts.reform.sscs.model.dwp.OfficeMapping;
import uk.gov.hmcts.reform.sscs.service.DwpAddressLookupService;

@Getter
public enum Benefit {

    ESA("Employment and Support Allowance", "Lwfans Cyflogaeth a Chymorth", "051", "ESA", true, DwpAddressLookupService::esaOfficeMapping, DwpAddressLookupService::esaDefaultMapping),
    JSA("Job Seekers Allowance", "", "", "JSA", true, null, null),
    PIP("Personal Independence Payment", "Taliad Annibyniaeth Personol", "002", "PIP", true, DwpAddressLookupService::pipOfficeMapping, DwpAddressLookupService::pipDefaultMapping),
    DLA("Disability Living Allowance", "Lwfans Byw i’r Anabl","037", "DLA", true, DwpAddressLookupService::dlaOfficeMapping, DwpAddressLookupService::dlaDefaultMapping),
    UC("Universal Credit", "Credyd Cynhwysol", "001", "UC", true, DwpAddressLookupService::ucOfficeMapping, DwpAddressLookupService::ucDefaultMapping),
    CARERS_ALLOWANCE("Carer's Allowance", "Lwfans Gofalwr", "070", "carersAllowance", false, DwpAddressLookupService::carersAllowanceOfficeMapping, DwpAddressLookupService::carersAllowanceDefaultMapping),
    ATTENDANCE_ALLOWANCE("Attendance Allowance", "Lwfans Gweini", "013", "attendanceAllowance", false, null, null);

    private final String description;
    private final String welshDescription;
    private final String benefitCode;
    private final String shortName;
    private final boolean hasAcronym;
    private final BiFunction<DwpAddressLookupService, String, Optional<OfficeMapping>> officeMappings;

    private static final org.slf4j.Logger LOG = getLogger(Benefit.class);
    private final Function<DwpAddressLookupService, Optional<OfficeMapping>> defaultOfficeMapping;

    Benefit(String description, String welshDescription, String benefitCode, String shortName, boolean hasAcronym,
            BiFunction<DwpAddressLookupService, String, Optional<OfficeMapping>> officeMappings,
            Function<DwpAddressLookupService, Optional<OfficeMapping>> defaultOfficeMapping) {
        this.description = description;
        this.welshDescription = welshDescription;
        this.benefitCode = benefitCode;
        this.shortName = shortName;
        this.hasAcronym = hasAcronym;
        this.officeMappings = officeMappings;
        this.defaultOfficeMapping = defaultOfficeMapping;
    }

    public static Benefit getBenefitByCode(String code) {
        Benefit benefit = findBenefitByShortName(code)
                .orElseGet(() -> findBenefitByDescription(code)
                        .orElse(null));
        if (benefit == null) {
            BenefitMappingException benefitMappingException =
                    new BenefitMappingException(new Exception(code + " is not a recognised benefit type"));
            LOG.error("Benefit type mapping error", benefitMappingException);
            throw benefitMappingException;
        }
        return benefit;
    }

    public static Optional<Benefit> findBenefitByShortName(String code) {
        return stream(values())
                .filter(type -> type.getShortName().equalsIgnoreCase(code))
                .findFirst();
    }

    public static Optional<Benefit> findBenefitByDescription(String code) {
        return stream(values())
                .filter(type -> type.getDescription().equalsIgnoreCase(code))
                .findFirst();
    }

    public static Boolean isBenefitTypeValid(String field) {
        return stream(values()).anyMatch(benefit -> benefit.toString().equalsIgnoreCase(field));
    }

    public String getBenefitNameDescriptionWithAcronym(boolean isEnglish) {
        String description = isEnglish || getWelshDescription().isEmpty() ? getDescription() : getWelshDescription();
        return description + getShortNameOptional().map(value -> format(" (%s)", value)).orElse(EMPTY);
    }

    public static String getLongBenefitNameDescriptionWithOptionalAcronym(String code, boolean isEnglish) {
        return getBenefitByCode(code).getBenefitNameDescriptionWithAcronym(isEnglish);
    }

    private Optional<String> getShortNameOptional() {
        return isHasAcronym() ? of(getShortName()) : empty();
    }

}
