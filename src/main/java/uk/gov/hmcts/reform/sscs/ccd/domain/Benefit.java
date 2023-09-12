package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelComposition.JUDGE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelComposition.JUDGE_AND_A_DOCTOR;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelComposition.JUDGE_AND_FINANCIALLY_QUALIFIED_PANEL_MEMBER;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelComposition.JUDGE_AND_ONE_OR_TWO_DOCTORS;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelComposition.JUDGE_DOCTOR_AND_DISABILITY_EXPERT;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelComposition.JUDGE_DOCTOR_AND_DISABILITY_EXPERT_IF_APPLICABLE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.SscsType.SSCS1;
import static uk.gov.hmcts.reform.sscs.ccd.domain.SscsType.SSCS2;
import static uk.gov.hmcts.reform.sscs.ccd.domain.SscsType.SSCS5;
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

    ESA("Employment and Support Allowance", "Lwfans Cyflogaeth a Chymorth", "051", "ESA", List.of("051"), true, DwpAddressLookupService::esaOfficeMappings, AirLookupService::getEsaOrUcVenue, SSCS1),
    JSA("Jobseeker’s Allowance", "Lwfans Ceisio Gwaith", "073", "JSA", List.of("073"), true, DwpAddressLookupService::jsaOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue, SSCS1),
    PIP("Personal Independence Payment", "Taliad Annibyniaeth Personol", "002", "PIP", List.of("002", "003"), true, DwpAddressLookupService::pipOfficeMappings, AirLookupService::getPipDlaCarersOrAttendanceAllowanceVenue, SSCS1),
    DLA("Disability Living Allowance", "Lwfans Byw i’r Anabl", "037", "DLA", List.of("037"), true, DwpAddressLookupService::dlaOfficeMappings, AirLookupService::getPipDlaCarersOrAttendanceAllowanceVenue, SSCS1),
    UC("Universal Credit", "Credyd Cynhwysol", "001", "UC", List.of("001"), true, DwpAddressLookupService::ucOfficeMappings, AirLookupService::getEsaOrUcVenue, SSCS1),
    CARERS_ALLOWANCE("Carer's Allowance", "Lwfans Gofalwr", "070", "carersAllowance", List.of("070"), false, DwpAddressLookupService::carersAllowanceOfficeMappings, AirLookupService::getPipDlaCarersOrAttendanceAllowanceVenue, SSCS1),
    ATTENDANCE_ALLOWANCE("Attendance Allowance", "Lwfans Gweini", "013", "attendanceAllowance", List.of("013"), false, DwpAddressLookupService::attendanceAllowanceOfficeMappings, AirLookupService::getPipDlaCarersOrAttendanceAllowanceVenue, SSCS1),
    BEREAVEMENT_BENEFIT("Bereavement Benefit", "Budd-dal Profedigaeth", "094", "bereavementBenefit", List.of("094"), false, DwpAddressLookupService::bereavementBenefitOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue, SSCS1),
    IIDB("Industrial Injuries Disablement Benefit", "Budd-dal Anabledd Anafiadau Diwydiannol", "067", "industrialInjuriesDisablement", List.of("067"), false, DwpAddressLookupService::iidbOfficeMappings, AirLookupService::getIidbVenue, SSCS1),
    MATERNITY_ALLOWANCE("Maternity Allowance", "Lwfans Mamolaeth", "079", "maternityAllowance", List.of("079"), false, DwpAddressLookupService::maternityAllowanceOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue, SSCS1),
    SOCIAL_FUND("Social Fund", "Cronfa Gymdeithasol", "088", "socialFund", List.of("088", "089", "061"), false, DwpAddressLookupService::socialFundOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue, SSCS1),
    INCOME_SUPPORT("Income Support", "Cymhorthdal Incwm", "061", "incomeSupport", List.of("061"), false, DwpAddressLookupService::incomeSupportOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue, SSCS1),
    BEREAVEMENT_SUPPORT_PAYMENT_SCHEME("Bereavement Support Payment Scheme", "Cynllun Taliad Cymorth Profedigaeth", "095", "bereavementSupportPaymentScheme", List.of("095"), false, DwpAddressLookupService::bereavementSupportPaymentSchemeOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue, SSCS1),
    INDUSTRIAL_DEATH_BENEFIT("Industrial Death Benefit", "Budd Marwolaeth Ddiwydiannol", "064", "industrialDeathBenefit", List.of("064"), false, DwpAddressLookupService::industrialDeathBenefitOfficeMappings, AirLookupService::getIidbVenue, SSCS1),
    PENSION_CREDIT("Pension Credit", "Credydau Pensiwn", "045", "pensionCredit", List.of("045"), false, DwpAddressLookupService::pensionCreditsOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue, SSCS1),
    RETIREMENT_PENSION("Retirement Pension", "Pensiwn Ymddeol", "082", "retirementPension", List.of("082"), false, DwpAddressLookupService::retirementPensionOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue, SSCS1),
    CHILD_SUPPORT("Child Support", "Cynnal Plant", "022", "childSupport", List.of("022", "023", "024", "025", "026", "028"), false, DwpAddressLookupService::childSupportOfficeMappings, AirLookupService::getCsaVenue, SSCS2),
    TAX_CREDIT("Tax Credit", "Credyd Treth", "053", "taxCredit", List.of("053", "054", "055"), false, DwpAddressLookupService::taxCreditOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue, SSCS5),
    GUARDIANS_ALLOWANCE("Guardians Allowance", "Lwfans Gwarcheidwad", "015", "guardiansAllowance", List.of("015"), false, DwpAddressLookupService::guardiansAllowanceOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue, SSCS5),
    TAX_FREE_CHILDCARE("Tax-Free Childcare", "Gofal Plant Di-dreth", "057", "taxFreeChildcare", List.of("057"), false, DwpAddressLookupService::taxFreeChildcareOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue, SSCS5),
    HOME_RESPONSIBILITIES_PROTECTION("Home Responsibilities Protection", "Diogelu Cyfrifoldebau Cartref", "050", "homeResponsibilitiesProtection", List.of("050"), false, DwpAddressLookupService::homeResponsibilitiesProtectionOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue, SSCS5),
    CHILD_BENEFIT("Child Benefit", "Budd-dal Plant", "016", "childBenefit", List.of("016"), false, DwpAddressLookupService::childBenefitOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue, SSCS5),
    THIRTY_HOURS_FREE_CHILDCARE("30 Hours Free Childcare", "Gofal Plant am ddim - 30 awr", "058", "thirtyHoursFreeChildcare", List.of("058"), false, DwpAddressLookupService::thirtyHoursFreeChildcareOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue, SSCS5),
    GUARANTEED_MINIMUM_PENSION("Guaranteed Minimum Pension", "Isafswm Pensiwn Gwarantedig", "034", "guaranteedMinimumPension", List.of("034"), false, DwpAddressLookupService::guaranteedMinimumPensionOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue, SSCS5),
    NATIONAL_INSURANCE_CREDITS("National Insurance Credits", "Credydau Yswiriant Gwladol", "030", "nationalInsuranceCredits", List.of("030"), false, DwpAddressLookupService::nationalInsuranceCreditsOfficeMappings, AirLookupService::getJsaBereavementBenefitVenue, SSCS5);


    private static final org.slf4j.Logger LOG = getLogger(Benefit.class);
    private final String description;
    private final String welshDescription;
    private final String benefitCode;
    private final String shortName;
    private final List<String> caseLoaderKeyId;
    private final boolean hasAcronym;
    private final Function<DwpAddressLookupService, OfficeMapping[]> officeMappings;
    private final BiFunction<AirLookupService, AirlookupBenefitToVenue, String> airLookupVenue;
    private final SscsType sscsType;

    Benefit(String description, String welshDescription, String benefitCode, String shortName, List<String> caseLoaderKeyId, boolean hasAcronym,
            Function<DwpAddressLookupService, OfficeMapping[]> officeMappings,
            BiFunction<AirLookupService, AirlookupBenefitToVenue, String> airLookupVenue, SscsType sscsType) {
        this.description = description;
        this.welshDescription = welshDescription;
        this.benefitCode = benefitCode;
        this.shortName = shortName;
        this.caseLoaderKeyId = caseLoaderKeyId;
        this.hasAcronym = hasAcronym;
        this.officeMappings = officeMappings;
        this.airLookupVenue = airLookupVenue;
        this.sscsType = sscsType;
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

    public static Optional<Benefit> findBenefitOptionalByBenefitCode(String code) {
        return stream(values()).filter(benefit -> benefit.getBenefitCode().equals(code)).findFirst();
    }

    public static Boolean isBenefitTypeValid(String field) {
        return stream(values()).anyMatch(benefit -> benefit.toString().equalsIgnoreCase(field));
    }

    public static String getLongBenefitNameDescriptionWithOptionalAcronym(String code, boolean isEnglish) {
        return getBenefitOptionalByCode(code)
                .map(b -> b.getBenefitNameDescriptionWithAcronym(isEnglish))
                .orElse(EMPTY);
    }

    public static Benefit getBenefitFromBenefitCode(String benefitCode) {
        return stream(Benefit.values())
                .filter(benefit -> benefit.getCaseLoaderKeyId().contains(benefitCode))
                .findFirst().orElse(null);
    }

    public PanelComposition getPanelComposition() {
        return switch (this) {
            case PIP, DLA, ATTENDANCE_ALLOWANCE -> JUDGE_DOCTOR_AND_DISABILITY_EXPERT;
            case ESA -> JUDGE_AND_A_DOCTOR;
            case CARERS_ALLOWANCE, BEREAVEMENT_BENEFIT, JSA, MATERNITY_ALLOWANCE, SOCIAL_FUND, INCOME_SUPPORT, BEREAVEMENT_SUPPORT_PAYMENT_SCHEME, PENSION_CREDIT, RETIREMENT_PENSION ->
                    JUDGE;
            case IIDB, INDUSTRIAL_DEATH_BENEFIT -> JUDGE_AND_ONE_OR_TWO_DOCTORS;
            case CHILD_SUPPORT, TAX_CREDIT, GUARDIANS_ALLOWANCE, TAX_FREE_CHILDCARE, HOME_RESPONSIBILITIES_PROTECTION, CHILD_BENEFIT, THIRTY_HOURS_FREE_CHILDCARE, GUARANTEED_MINIMUM_PENSION, NATIONAL_INSURANCE_CREDITS ->
                    JUDGE_AND_FINANCIALLY_QUALIFIED_PANEL_MEMBER;
            default -> JUDGE_DOCTOR_AND_DISABILITY_EXPERT_IF_APPLICABLE;
        };
    }

    public String getBenefitNameDescriptionWithAcronym(boolean isEnglish) {
        String description = isEnglish || getWelshDescription().isEmpty() ? getDescription() : getWelshDescription();
        return description + getShortNameOptional().map(value -> format(" (%s)", value)).orElse(EMPTY);
    }

    private Optional<String> getShortNameOptional() {
        return isHasAcronym() ? of(getShortName()) : empty();
    }
}
