package uk.gov.hmcts.reform.sscs.service;

import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.io.IOUtils.resourceToString;
import static org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.stripToEmpty;
import static org.apache.commons.lang3.StringUtils.substringBetween;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.*;

import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.exception.BenefitMappingException;
import uk.gov.hmcts.reform.sscs.exception.DwpAddressLookupException;
import uk.gov.hmcts.reform.sscs.exception.NoMrnDetailsException;
import uk.gov.hmcts.reform.sscs.model.dwp.DwpMappings;
import uk.gov.hmcts.reform.sscs.model.dwp.OfficeAddress;
import uk.gov.hmcts.reform.sscs.model.dwp.OfficeMapping;

@Service
@Slf4j
public class DwpAddressLookupService {

    private static final String TEST_HMCTS_ADDRESS = "test-hmcts-address";

    private final DwpMappings dwpMappings;

    public DwpAddressLookupService() {
        try {
            String json = resourceToString("reference-data/dwpAddresses.json",
                StandardCharsets.UTF_8, Thread.currentThread().getContextClassLoader());
            Gson gson = new Gson();
            dwpMappings = gson.fromJson(json, DwpMappings.class);
        } catch (Exception exception) {
            log.error("Cannot parse dwp addresses. " + exception.getMessage(), exception);
            throw new RuntimeException("cannot parse dwp addresses", exception);
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
            throw new DwpAddressLookupException(String.format("could not find dwp officeAddress for benefitType %s and dwpIssuingOffice %s",
                    benefitType, dwpIssuingOffice));
        }
        return officeMapping.get().getAddress();
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
        return getDwpRegionalCenterByBenefitType(benefitType, officeMapping);
    }

    public String getDefaultDwpRegionalCenterByBenefitTypeAndOffice(String benefitType) {
        Optional<OfficeMapping> officeMapping = getDefaultDwpMappingByBenefitType(benefitType);
        return getDwpRegionalCenterByBenefitType(benefitType, officeMapping);
    }

    private String getDwpRegionalCenterByBenefitType(String benefitType, Optional<OfficeMapping> officeMapping) {
        Benefit benefit;
        try {
            benefit = Benefit.getBenefitByCode(benefitType);
            if (benefit.isHasDwpRegionCentre()) {
                return officeMapping.map(mapping -> mapping.getMapping().getDwpRegionCentre()).orElse(null);
            } else {
                return officeMapping.map(mapping -> mapping.getMapping().getCcd()).orElse(null);
            }
        } catch (BenefitMappingException e) {
            return null;
        }
    }

    public OfficeMapping[] getDwpOfficeMappings(String benefitType) {
        log.info("looking up all officeAddress for benefitType {}", benefitType);

        return  findBenefitByShortName(benefitType)
                .map(b -> b.getOfficeMappings().apply(this)).orElse(new OfficeMapping[0]);
    }

    public Optional<OfficeMapping> getDwpMappingByOffice(String benefitType, String dwpIssuingOffice) {
        log.info("looking up officeAddress for benefitType {} and dwpIssuingOffice {}", benefitType, dwpIssuingOffice);

        if (equalsIgnoreCase(dwpIssuingOffice, TEST_HMCTS_ADDRESS)) {
            return Optional.of(dwpMappings.getTestHmctsAddress());
        }
        return  findBenefitByShortName(benefitType)
                .flatMap(b -> b.getOfficeMappingsByDwpIssuingOffice().apply(this, dwpIssuingOffice));
    }

    public Optional<OfficeMapping> carersAllowanceOfficeMapping(@SuppressWarnings("unused") String dwpIssuingOffice) {
        return Optional.of(dwpMappings.getCarersAllowance());
    }

    public Optional<OfficeMapping> dlaOfficeMapping(String dwpIssuingOffice) {
        return getOfficeMappingByDwpIssuingOffice(dwpIssuingOffice, dwpMappings.getDla());
    }

    public Optional<OfficeMapping> attendanceAllowanceOfficeMapping(String dwpIssuingOffice) {
        return getOfficeMappingByDwpIssuingOffice(dwpIssuingOffice, dwpMappings.getAttendanceAllowance());
    }

    public Optional<OfficeMapping> ucOfficeMapping(@SuppressWarnings("unused") String dwpIssuingOffice) {
        return Optional.of(dwpMappings.getUc());
    }

    public Optional<OfficeMapping> esaOfficeMapping(String dwpIssuingOffice) {
        return getOfficeMappingByDwpIssuingOffice(dwpIssuingOffice, dwpMappings.getEsa());
    }

    public Optional<OfficeMapping> pipOfficeMapping(String dwpIssuingOffice) {
        String dwpIssuingOfficeStripped = ofNullable(substringBetween(dwpIssuingOffice,"(", ")"))
                .orElse(dwpIssuingOffice.replaceAll("\\D+",""));

        if (isEmpty(dwpIssuingOfficeStripped)) {
            dwpIssuingOfficeStripped = dwpIssuingOffice;
        }

        return getOfficeMappingByDwpIssuingOffice(dwpIssuingOfficeStripped, dwpMappings.getPip());
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

    public OfficeMapping[] ucOfficeMappings() {
        return new OfficeMapping[]{dwpMappings.getUc()};
    }

    public OfficeMapping[] carersAllowanceOfficeMappings() {
        return new OfficeMapping[]{dwpMappings.getCarersAllowance()};
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

    private Optional<OfficeMapping> getOfficeMappingByDwpIssuingOffice(String dwpIssuingOffice, OfficeMapping[] mappings) {
        return stream(mappings)
                .filter(office -> equalsAnyIgnoreCase(office.getCode(), stripToEmpty(dwpIssuingOffice)))
                .findFirst();
    }
}
