package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BenefitCode {
    UC(1,"Universal Credit"),
    PIP_NEW_CLAIM(2,"Personal Independent Payment (New Claim)"),
    PIP_REASSESSMENT_CASE(3,"Personal Independent Payment (Reassessment Case)"),
    ATTENDANCE_ALLOWANCE(13,"Attendance Allowance"),
    DIFFUSE_MESOTHELIOMA(14,"Diffuse Mesothelioma"),
    GUARDIANS_ALLOWANCE(15,"Guardians Allowance"),
    CHILD_BENEFIT_LONE_PARENT(16,"Child Benefit/Child Benefit(Lone Parent)"),
    CHILD_SUPPORT_ASSESSMENTS(22,"Child Support Assessments"),
    CHILD_SUPPORT_REFORMS(23,"Child Support Reforms"),
    CHILD_SUPPORT_REFORM_VARIATION_REFERRAL(24,"Child Support Reform  Variation Referrals"),
    CHILD_SUPPORT_DEPARTURES(25,"Child Support Departures"),
    CHILD_SUPPORT_VARIATION_REFERRAL(26,"Child Support Variation Referral"),
    CHILD_SUPPORT_DEPARTURES_REFERRAL(28,"Child Support Departures Referral"),
    CREDITS_APPROVED_TRAINING(30,"Credits - Approved Training"),
    COMPENSATION_RECOVERY_UNIT(31,"Compensation Recovery Unit"),
    CREDITS_JURY_SERVICE(32,"Credits - Jury Service"),
    CREDITS_STATUTORY_SICK_PAY(33,"Credits - Statutory Sick Pay"),
    COEG(34,"COEG"),
    CREDITS_STATUTORY_MATERNITY_PAY(35,"Credits - Statutory Maternity Pay"),
    ROAD_TRAFFIC_NHS_CHARGES(36,"Road Traffic (Nhs Charges)"),
    DLA(37,"Disability Living Allowance"),
    PENSION_CREDITS(45,"Pension Credits"),
    HRP(50,"HRP"),
    ESA(51,"Employment Support Allowance"),
    CHILD_TAX_CREDIT(53,"Child Tax Credit"),
    WORKING_TAX_CREDIT(54,"Working Tax Credit"),
    PENALTY_PROCEEDINGS(55,"Penalty Proceedings"),
    TAX_FREE_CHILDCARE(57,"Tax-Free Childcare Scheme"),
    THIRTY_HOURS_FREE_CHILDCARE(58,"30 Hours' Free Childcare Scheme"),
    INCOME_SUPPORT(61,"Income Support"),
    ESA_REASSESSMENT(62,"Employment Support Allowance Reassessment "),
    INDUSTRIAL_DEATH_BENEFIT(64,"Industrial Death Benefit"),
    IIDB(67,"Industrial Injuries Disablement Benefit"),
    CARERS_ALLOWANCE(70,"Carer's Allowance"),
    JSA(73,"Job Seekers Allowance"),
    HOUSING_BENEFIT(75,"Housing Benefit"),
    MATERNITY_ALLOWANCE(79,"Maternity Benefit/Allowances"),
    RETIREMENT_PENSION(82,"Retirement Pension"),
    SOCIAL_FUND_MATERNITY(88,"Social Fund - Maternity"),
    SOCIAL_FUND_FUNERAL(89,"Social Fund - Funeral"),
    CHILDRENS_FUNERAL_FUND(90, "Children's Funeral Fund For England"),
    VACCINE_DAMAGE_APPEALS_TRIBUNALS(91,"Vaccine Damage Appeals Tribunals"),
    IBC(93, "Infected Blood Compensation"),
    BEREAVEMENT_BENEFIT(94,"Bereavement Benefit"),
    BEREAVEMENT_SUPPORT_PAYMENT(95,"Bereavement Support Payment Scheme"),
    WORKING_FAMILIES_TAX_CREDIT(96,"Working Families Tax Credit");

    private final int ccdReference;
    private final String description;

    public static BenefitCode getBenefitCode(String ccdReference) {
        if (isNotBlank(ccdReference) && ccdReference.trim().matches("\\d+")) {
            return getBenefitCode(Integer.parseInt(ccdReference.trim()));
        }
        return null;
    }

    public static BenefitCode getBenefitCode(int code) {
        return Arrays.stream(values())
                .filter(benefit -> benefit.ccdReference == code)
                .findFirst()
                .orElse(null);
    }
}
