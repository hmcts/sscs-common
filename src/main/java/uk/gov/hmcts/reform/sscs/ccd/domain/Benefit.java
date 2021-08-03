package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelComposition.JUDGE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelComposition.JUDGE_AND_A_DOCTOR;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelComposition.JUDGE_AND_ONE_OR_TWO_DOCTORS;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelComposition.JUDGE_DOCTOR_AND_DISABILITY_EXPERT;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelComposition.JUDGE_DOCTOR_AND_DISABILITY_EXPERT_IF_APPLICABLE;
import static uk.gov.hmcts.reform.sscs.exception.BenefitMappingException.createException;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.Getter;
import uk.gov.hmcts.reform.sscs.model.AirlookupBenefitToVenue;
import uk.gov.hmcts.reform.sscs.model.dwp.OfficeMapping;
import uk.gov.hmcts.reform.sscs.service.AirLookupService;
import uk.gov.hmcts.reform.sscs.service.DwpAddressLookupService;

@Getter
public enum Benefit {

    ESA("Employment and Support Allowance", "Lwfans Cyflogaeth a Chymorth", "051", "ESA", List.of("051"), true, DwpAddressLookupService::esaOfficeMappings, AirLookupService::getEsaOrUcVenue),
    JSA("Job Seekers Allowance", "Lwfans Ceisio Gwaith", "073", "JSA", List.of("073"), true, DwpAddressLookupService::jsaOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue),
    PIP("Personal Independence Payment", "Taliad Annibyniaeth Personol", "002", "PIP", List.of("002", "003"), true, DwpAddressLookupService::pipOfficeMappings, AirLookupService::getPipDlaCarersOrAttendanceAllowanceVenue),
    DLA("Disability Living Allowance", "Lwfans Byw i’r Anabl","037", "DLA", List.of("037"), true, DwpAddressLookupService::dlaOfficeMappings, AirLookupService::getPipDlaCarersOrAttendanceAllowanceVenue),
    UC("Universal Credit", "Credyd Cynhwysol", "001", "UC", List.of("001"), true, DwpAddressLookupService::ucOfficeMappings, AirLookupService::getEsaOrUcVenue),
    CARERS_ALLOWANCE("Carer's Allowance", "Lwfans Gofalwr", "070", "carersAllowance", List.of("070"), false, DwpAddressLookupService::carersAllowanceOfficeMappings, AirLookupService::getPipDlaCarersOrAttendanceAllowanceVenue),
    ATTENDANCE_ALLOWANCE("Attendance Allowance", "Lwfans Gweini", "013", "attendanceAllowance", List.of("013"), false, DwpAddressLookupService::attendanceAllowanceOfficeMappings, AirLookupService::getPipDlaCarersOrAttendanceAllowanceVenue),
    BEREAVEMENT_BENEFIT("Bereavement Benefit", "Budd-dal Profedigaeth", "094", "bereavementBenefit", List.of("094"), false, DwpAddressLookupService::bereavementBenefitOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue),
    IIDB("Industrial Injuries Disablement Benefit", "Budd-dal Anabledd Anafiadau Diwydiannol", "067", "industrialInjuriesDisablement", List.of("067"), false, DwpAddressLookupService::iidbOfficeMappings, AirLookupService::getIidbVenue),
    MATERNITY_ALLOWANCE("Maternity Allowance", "Lwfans Mamolaeth", "079", "maternityAllowance", List.of("079"), false, DwpAddressLookupService::maternityAllowanceOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue),
    SOCIAL_FUND("Social Fund", "Cronfa Gymdeithasol", "088", "socialFund", List.of("088", "089", "061"), false, DwpAddressLookupService::socialFundOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue),
    INCOME_SUPPORT("Income Support", "Cymhorthdal Incwm", "061", "incomeSupport", List.of("061"), false, DwpAddressLookupService::incomeSupportOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue),
    BEREAVEMENT_SUPPORT_PAYMENT_SCHEME("Bereavement Support Payment Scheme", "Cynllun Taliad Cymorth Profedigaeth", "095", "bereavementSupportPaymentScheme", List.of("095"), false, DwpAddressLookupService::bereavementSupportPaymentSchemeOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue),
    INDUSTRIAL_DEATH_BENEFIT("Industrial Death Benefit", "Budd Marwolaeth Ddiwydiannol", "064", "industrialDeathBenefit", List.of("064"), false, DwpAddressLookupService::industrialDeathBenefitOfficeMappings, AirLookupService::getIidbVenue),
    PENSION_CREDITS("Pension Credits", "Credydau Pensiwn", "045", "pensionCredits", List.of("045"), false, DwpAddressLookupService::pensionCreditsOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue),
    RETIREMENT_PENSION("Retirement Pension", "Pensiwn Ymddeol", "082", "retirementPension", List.of("082"), false, DwpAddressLookupService::retirementPensionOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue);


    private final String description;
    private final String welshDescription;
    private final String benefitCode;
    private final String shortName;
    private final List<String> caseLoaderKeyId;
    private final boolean hasAcronym;
    private final Function<DwpAddressLookupService, OfficeMapping[]> officeMappings;
    private final BiFunction<AirLookupService, AirlookupBenefitToVenue, String> airLookupVenue;

    private static final org.slf4j.Logger LOG = getLogger(Benefit.class);

    Benefit(String description, String welshDescription, String benefitCode, String shortName, List<String> caseLoaderKeyId, boolean hasAcronym,
            Function<DwpAddressLookupService, OfficeMapping[]> officeMappings,
            BiFunction<AirLookupService, AirlookupBenefitToVenue, String> airLookupVenue) {
        this.description = description;
        this.welshDescription = welshDescription;
        this.benefitCode = benefitCode;
        this.shortName = shortName;
        this.caseLoaderKeyId = caseLoaderKeyId;
        this.hasAcronym = hasAcronym;
        this.officeMappings = officeMappings;
        this.airLookupVenue = airLookupVenue;
    }

    public PanelComposition getPanelComposition() {
        switch (this) {
            case PIP: case DLA: case ATTENDANCE_ALLOWANCE:
                return JUDGE_DOCTOR_AND_DISABILITY_EXPERT;
            case ESA:
                return JUDGE_AND_A_DOCTOR;
            case CARERS_ALLOWANCE: case BEREAVEMENT_BENEFIT: case JSA: case MATERNITY_ALLOWANCE: case SOCIAL_FUND: case INCOME_SUPPORT:
            case BEREAVEMENT_SUPPORT_PAYMENT_SCHEME: case PENSION_CREDITS: case RETIREMENT_PENSION:
                return JUDGE;
            case IIDB: case INDUSTRIAL_DEATH_BENEFIT:
                return JUDGE_AND_ONE_OR_TWO_DOCTORS;
            default: return JUDGE_DOCTOR_AND_DISABILITY_EXPERT_IF_APPLICABLE;
        }
    }

    public static Benefit getBenefitByCodeOrThrowException(String code) {
        return getBenefitOptionalByCode(code)
                .orElseThrow(() -> createException(code));
    }


    public static Optional<Benefit> getBenefitOptionalByCode(String code) {
        return findBenefitByShortName(code)
                .or(() -> findBenefitByDescription(code));
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
        return getBenefitOptionalByCode(code)
                .map(b -> b.getBenefitNameDescriptionWithAcronym(isEnglish))
                .orElse(EMPTY);
    }

    private Optional<String> getShortNameOptional() {
        return isHasAcronym() ? of(getShortName()) : empty();
    }

}
