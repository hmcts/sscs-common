package uk.gov.hmcts.reform.sscs.service;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Stream.of;
import static org.apache.commons.io.IOUtils.resourceToString;
import static org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.stripToEmpty;
import static org.apache.commons.lang3.StringUtils.substringBetween;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.*;

import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
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

    private static final DwpMappings dwpMappings;
    private static OfficeMapping[] allDwpBenefitOffices;

    static {
        try {
            String json = resourceToString("reference-data/dwpAddresses.json",
                StandardCharsets.UTF_8, Thread.currentThread().getContextClassLoader());
            Gson gson = new Gson();
            dwpMappings = gson.fromJson(json, DwpMappings.class);

            //FIXME: This is only used in one class in Tribunals so refactor this at some point to make cleaner and make work with the new benefit types, probably as part of SSCS-9041
            allDwpBenefitOffices = of(dwpMappings.getPip(), dwpMappings.getEsa(), dwpMappings.getDla())
                    .flatMap(Stream::of)
                    .toArray(OfficeMapping[]::new);

            allDwpBenefitOffices = Arrays.copyOf(allDwpBenefitOffices, allDwpBenefitOffices.length + 1);
            allDwpBenefitOffices[allDwpBenefitOffices.length - 1] = dwpMappings.getUc();

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

    public Optional<OfficeMapping> getDefaultDwpMappingByBenefitType(String benefitType) {
        return findBenefitByShortName(benefitType)
                .map(benefit -> benefit.getOfficeMappings().apply(this))
                .orElse(List.of())
                .stream()
                .filter(OfficeMapping::isDefault)
                .findFirst();
    }

    public String getDwpRegionalCenterByBenefitTypeAndOffice(String benefitType, String dwpIssuingOffice) {
        Optional<OfficeMapping> officeMapping = getDwpMappingByOffice(benefitType, dwpIssuingOffice);
        return getDwpRegionalCenterByBenefitType(officeMapping);
    }

    public Optional<OfficeMapping> getDwpMappingByOffice(String benefitType, String dwpIssuingOffice) {
        log.info("looking up officeAddress for benefitType {} and dwpIssuingOffice {}", benefitType, dwpIssuingOffice);

        if (equalsIgnoreCase(dwpIssuingOffice, TEST_HMCTS_ADDRESS)) {
            return Optional.of(dwpMappings.getTestHmctsAddress());
        }

        String pipIssuingOfficeStripped = extractPipIssuingOfficeSearchKey(benefitType, dwpIssuingOffice);
        String dwpIssuingOfficeSearchKey = isEmpty(pipIssuingOfficeStripped) ? dwpIssuingOffice : pipIssuingOfficeStripped;

        List<OfficeMapping> mappings = findBenefitByShortName(benefitType)
                .map(b -> b.getOfficeMappings().apply(this)).orElse(List.of());

        if (mappings.size() == 1) {
            return mappings.stream().findFirst();
        }
        return mappings.stream()
                .filter(office -> equalsAnyIgnoreCase(office.getCode(), stripToEmpty(dwpIssuingOfficeSearchKey)))
                .findFirst();
    }

    private String extractPipIssuingOfficeSearchKey(String benefitType, String dwpIssuingOffice) {
        String dwpIssuingOfficeStripped = null;
        if (findBenefitByShortName(benefitType).map(b -> b == PIP).orElse(false)) {
            dwpIssuingOfficeStripped = ofNullable(substringBetween(dwpIssuingOffice, "(", ")"))
                    .orElse(dwpIssuingOffice == null ? "" : dwpIssuingOffice.replaceAll("\\D+", ""));
        }
        return dwpIssuingOfficeStripped;
    }

    private String getDwpRegionalCenterByBenefitType(Optional<OfficeMapping> officeMapping) {
        try {
            if (officeMapping.map(m -> m.getMapping().getDwpRegionCentre() != null).orElse(false)) {
                return officeMapping.map(mapping -> mapping.getMapping().getDwpRegionCentre()).orElse(null);
            } else {
                return officeMapping.map(mapping -> mapping.getMapping().getCcd()).orElse(null);
            }
        } catch (BenefitMappingException e) {
            return null;
        }
    }

    public List<OfficeMapping> esaOfficeMapping() {
        return List.of(dwpMappings.getEsa());
    }

    public List<OfficeMapping> ucOfficeMapping() {
        return List.of(dwpMappings.getUc());
    }

    public List<OfficeMapping> bereavementBenefitOfficeMapping() {
        return List.of(dwpMappings.getBereavementBenefit());
    }

    public List<OfficeMapping> carersAllowanceOfficeMapping() {
        return List.of(dwpMappings.getCarersAllowance());
    }

    public List<OfficeMapping> dlaOfficeMapping() {
        return List.of(dwpMappings.getDla());
    }

    public List<OfficeMapping> attendanceAllowanceOfficeMapping() {
        return List.of(dwpMappings.getAttendanceAllowance());
    }

    public List<OfficeMapping> pipOfficeMapping() {
        return List.of(dwpMappings.getPip());
    }

    public List<OfficeMapping> iidbOfficeMapping() {
        return List.of(dwpMappings.getIidb());
    }

    public OfficeMapping[] allDwpBenefitOffices() {
        return allDwpBenefitOffices;
    }
}
