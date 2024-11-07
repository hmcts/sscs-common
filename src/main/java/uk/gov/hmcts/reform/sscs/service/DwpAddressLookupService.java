package uk.gov.hmcts.reform.sscs.service;

import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.apache.commons.io.IOUtils.resourceToString;
import static org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.stripToEmpty;
import static org.apache.commons.lang3.StringUtils.substringBetween;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.PIP;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.findBenefitByShortName;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.exception.DwpAddressLookupException;
import uk.gov.hmcts.reform.sscs.exception.NoMrnDetailsException;
import uk.gov.hmcts.reform.sscs.model.dwp.DwpMappings;
import uk.gov.hmcts.reform.sscs.model.dwp.OfficeAddress;
import uk.gov.hmcts.reform.sscs.model.dwp.OfficeMapping;

@Service
@Slf4j
public class DwpAddressLookupService {
    //FIXME: Now we are onboarding HMRC, we need to replace all references to DWP with FTA (First Tier Agency)

    private static final String TEST_HMCTS_ADDRESS = "test-hmcts-address";

    private final DwpMappings dwpMappings;

    public DwpAddressLookupService() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = resourceToString("reference-data/ftaAddresses.json",
                    StandardCharsets.UTF_8, Thread.currentThread().getContextClassLoader());
            dwpMappings = mapper.readValue(json, DwpMappings.class);
        } catch (Exception exception) {
            log.error("Cannot parse FTA addresses. " + exception.getMessage(), exception);
            throw new RuntimeException("cannot parse FTA addresses", exception);
        }
    }


    public Address lookupDwpAddress(SscsCaseData caseData) {
        if (nonNull(caseData.getAppeal()) && nonNull(caseData.getAppeal().getMrnDetails())
                && nonNull(caseData.getAppeal().getMrnDetails().getDwpIssuingOffice())) {

            final OfficeAddress dwpAddress = lookup(caseData.getAppeal().getBenefitType().getCode(),
                    caseData.getAppeal().getMrnDetails().getDwpIssuingOffice());

            return buildAddress(dwpAddress);
        } else {
            throw new NoMrnDetailsException(caseData);
        }
    }

    private OfficeAddress lookup(String benefitType, String dwpIssuingOffice) {
        Optional<OfficeMapping> officeMapping = getDwpMappingByOffice(benefitType, dwpIssuingOffice);

        if (officeMapping.isEmpty()) {
            throw new DwpAddressLookupException(String.format("could not find ogd officeAddress for benefitType %s and dwpIssuingOffice %s",
                    benefitType, dwpIssuingOffice));
        }
        return officeMapping.get().getAddress();
    }

    public boolean validateIssuingOffice(String benefitType, String dwpIssuingOffice) {
        if (dwpIssuingOffice == null) {
            return false;
        }

        OfficeMapping[] dwpOfficeMappings = getDwpOfficeMappings(benefitType);
        if (dwpOfficeMappings.length == 1) {
            return dwpOfficeMappings[0].getCode().equalsIgnoreCase(dwpIssuingOffice);
        }

        String dwpIssuingOfficeSearch = isPipBenefit(benefitType) ? stripOgdIssuingOfficeForPip(dwpIssuingOffice) : dwpIssuingOffice;
        Optional<OfficeMapping> officeMapping = getOfficeMappingByOgdIssuingOffice(dwpIssuingOfficeSearch, dwpOfficeMappings);
        return officeMapping.isPresent();
    }

    private Address buildAddress(OfficeAddress dwpAddress) {
        return Address.builder()
                .line1(dwpAddress.getLine1())
                .town(dwpAddress.getLine2())
                .county(dwpAddress.getLine3())
                .postcode(dwpAddress.getPostCode())
                .build();
    }

    public String getDwpRegionalCenterByBenefitTypeAndOffice(String benefitType, String dwpIssuingOffice) {
        Optional<OfficeMapping> officeMapping = getDwpMappingByOffice(benefitType, dwpIssuingOffice);
        return getOgdRegionalCenterByBenefitType(officeMapping).orElse(null);
    }

    private Optional<String> getOgdRegionalCenterByBenefitType(Optional<OfficeMapping> officeMapping) {
        return officeMapping.map(mapping -> mapping.getMapping().getDwpRegionCentre());
    }

    public OfficeMapping[] getDwpOfficeMappings(String benefitType) {
        log.info("looking up all officeAddress for benefitType {}", benefitType);

        return findBenefitByShortName(benefitType)
                .map(b -> b.getOfficeMappings().apply(this)).orElse(new OfficeMapping[0]);
    }

    public Optional<OfficeMapping> getDwpMappingByOffice(String benefitType, String dwpIssuingOffice) {
        log.info("looking up officeAddress for benefitType {} and dwpIssuingOffice {}", benefitType, dwpIssuingOffice);

        if (equalsIgnoreCase(dwpIssuingOffice, TEST_HMCTS_ADDRESS)) {
            return of(dwpMappings.getTestHmctsAddress());
        }
        OfficeMapping[] dwpOfficeMappings = getDwpOfficeMappings(benefitType);
        if (dwpOfficeMappings.length == 1) {
            return of(dwpOfficeMappings[0]);
        }
        if (dwpIssuingOffice == null) {
            return stream(dwpOfficeMappings).filter(OfficeMapping::isDefault).findFirst();
        }

        String dwpIssuingOfficeSearch = isPipBenefit(benefitType) ? stripOgdIssuingOfficeForPip(dwpIssuingOffice) : dwpIssuingOffice;

        return getOfficeMappingByOgdIssuingOffice(dwpIssuingOfficeSearch, dwpOfficeMappings);
    }

    private boolean isPipBenefit(String benefitType) {
        return findBenefitByShortName(benefitType).filter(benefit -> benefit.equals(PIP)).isPresent();
    }

    private String stripOgdIssuingOfficeForPip(String dwpIssuingOffice) {
        String ogdIssuingOfficeStripped = ofNullable(substringBetween(dwpIssuingOffice, "(", ")"))
                .orElse(StringUtils.isEmpty(dwpIssuingOffice.replaceAll("\\D+", ""))
                        ? fuzzyPipOfficeMatching(dwpIssuingOffice) : dwpIssuingOffice.replaceAll("\\D+", ""));

        if (isEmpty(ogdIssuingOfficeStripped)) {
            ogdIssuingOfficeStripped = dwpIssuingOffice;
        }
        return ogdIssuingOfficeStripped;
    }

    private String fuzzyPipOfficeMatching(String dwpIssuingOffice) {
        String office;
        switch (StringUtils.lowerCase(dwpIssuingOffice)) {
            case "pip ae":
                office = "AE";
                break;
            case "pip recovery from estates":
                office = "Recovery from Estates";
                break;
            default:
                office = dwpIssuingOffice;
                break;
        }
        return office;
    }

    public Optional<OfficeMapping> getDefaultDwpMappingByBenefitType(String benefitType) {
        return Benefit.findBenefitByShortName(benefitType)
                .flatMap(benefit -> stream(benefit.getOfficeMappings().apply(this))
                        .filter(OfficeMapping::isDefault)
                        .findFirst());
    }

    public OfficeMapping[] esaOfficeMappings() {
        return dwpMappings.getEsa();
    }

    public OfficeMapping[] jsaOfficeMappings() {
        return dwpMappings.getJsa();
    }

    public OfficeMapping[] ucOfficeMappings() {
        return dwpMappings.getUc();
    }

    public OfficeMapping[] carersAllowanceOfficeMappings() {
        return new OfficeMapping[]{dwpMappings.getCarersAllowance()};
    }

    public OfficeMapping[] bereavementBenefitOfficeMappings() {
        return new OfficeMapping[]{dwpMappings.getBereavementBenefit()};
    }

    public OfficeMapping[] dlaOfficeMappings() {
        return dwpMappings.getDla();
    }

    public OfficeMapping[] attendanceAllowanceOfficeMappings() {
        return dwpMappings.getAttendanceAllowance();
    }

    public OfficeMapping[] pipOfficeMappings() {
        return dwpMappings.getPip();
    }

    public OfficeMapping[] iidbOfficeMappings() {
        return dwpMappings.getIidb();
    }

    public OfficeMapping[] maternityAllowanceOfficeMappings() {
        return new OfficeMapping[]{dwpMappings.getMaternityAllowance()};
    }

    public OfficeMapping[] socialFundOfficeMappings() {
        return dwpMappings.getSocialFund();
    }

    public OfficeMapping[] incomeSupportOfficeMappings() {
        return dwpMappings.getIncomeSupport();
    }

    public OfficeMapping[] bereavementSupportPaymentSchemeOfficeMappings() {
        return new OfficeMapping[]{dwpMappings.getBereavementSupportPaymentScheme()};
    }

    public OfficeMapping[] industrialDeathBenefitOfficeMappings() {
        return dwpMappings.getIndustrialDeathBenefit();
    }

    public OfficeMapping[] pensionCreditsOfficeMappings() {
        return dwpMappings.getPensionCredit();
    }

    public OfficeMapping[] childSupportOfficeMappings() {
        return new OfficeMapping[]{dwpMappings.getChildSupport()};
    }

    public OfficeMapping[] taxCreditOfficeMappings() {
        return new OfficeMapping[]{dwpMappings.getTaxCredit()};
    }

    public OfficeMapping[] guardiansAllowanceOfficeMappings() {
        return new OfficeMapping[]{dwpMappings.getGuardiansAllowance()};
    }

    public OfficeMapping[] taxFreeChildcareOfficeMappings() {
        return new OfficeMapping[]{dwpMappings.getTaxFreeChildcare()};
    }

    public OfficeMapping[] homeResponsibilitiesProtectionOfficeMappings() {
        return new OfficeMapping[]{dwpMappings.getHomeResponsibilitiesProtection()};
    }

    public OfficeMapping[] childBenefitOfficeMappings() {
        return new OfficeMapping[]{dwpMappings.getChildBenefit()};
    }

    public OfficeMapping[] thirtyHoursFreeChildcareOfficeMappings() {
        return new OfficeMapping[]{dwpMappings.getThirtyHoursFreeChildcare()};
    }

    public OfficeMapping[] guaranteedMinimumPensionOfficeMappings() {
        return new OfficeMapping[]{dwpMappings.getGuaranteedMinimumPension()};
    }

    public OfficeMapping[] nationalInsuranceCreditsOfficeMappings() {
        return new OfficeMapping[]{dwpMappings.getNationalInsuranceCredits()};
    }

    public OfficeMapping[] infectedBloodCompensationMappings() {
        return new OfficeMapping[]{dwpMappings.getInfectedBloodCompensation()};
    }

    public OfficeMapping[] retirementPensionOfficeMappings() {
        return dwpMappings.getRetirementPension();
    }

    private Optional<OfficeMapping> getOfficeMappingByOgdIssuingOffice(String dwpIssuingOffice, OfficeMapping[] mappings) {
        Optional<OfficeMapping> officeMapping = stream(mappings)
                .filter(office -> equalsAnyIgnoreCase(office.getCode(), stripToEmpty(dwpIssuingOffice)))
                .findFirst();

        if (officeMapping.isEmpty()) {
            officeMapping = stream(mappings)
                    .filter(office -> equalsAnyIgnoreCase(office.getMapping().getCcd(), stripToEmpty(dwpIssuingOffice)))
                    .findFirst();
        }

        return officeMapping;
    }
}
