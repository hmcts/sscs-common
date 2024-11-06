package uk.gov.hmcts.reform.sscs.model.dwp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class DwpMappings {

    private OfficeMapping[] pip;
    private OfficeMapping[] esa;
    private OfficeMapping[] uc;
    private OfficeMapping[] dla;
    private OfficeMapping carersAllowance;
    private OfficeMapping[] attendanceAllowance;
    private OfficeMapping[] jsa;
    private OfficeMapping bereavementBenefit;
    private OfficeMapping[] iidb;
    private OfficeMapping maternityAllowance;
    private OfficeMapping testHmctsAddress;
    private OfficeMapping[] socialFund;
    private OfficeMapping[] incomeSupport;
    private OfficeMapping bereavementSupportPaymentScheme;
    private OfficeMapping[] industrialDeathBenefit;
    private OfficeMapping[] pensionCredit;
    private OfficeMapping[] retirementPension;
    private OfficeMapping childSupport;
    private OfficeMapping taxCredit;
    private OfficeMapping guardiansAllowance;
    private OfficeMapping taxFreeChildcare;
    private OfficeMapping homeResponsibilitiesProtection;
    private OfficeMapping childBenefit;
    private OfficeMapping thirtyHoursFreeChildcare;
    private OfficeMapping guaranteedMinimumPension;
    private OfficeMapping nationalInsuranceCredits;
    private OfficeMapping infectedBloodCompensation;
}

