package uk.gov.hmcts.reform.sscs.service;

import com.google.gson.Gson;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.exception.DwpAddressLookupException;
import uk.gov.hmcts.reform.sscs.exception.NoMrnDetailsException;
import uk.gov.hmcts.reform.sscs.model.dwp.DwpMappings;
import uk.gov.hmcts.reform.sscs.model.dwp.OfficeAddress;
import uk.gov.hmcts.reform.sscs.model.dwp.OfficeMapping;

@Service
@Slf4j
public class DwpAddressLookupService {

    private static final String PIP = "PIP";
    private static final String ESA = "ESA";
    private static final String UC = "UC";
    private static final String TEST_HMCTS_ADDRESS = "test-hmcts-address";

    private static String json;
    private static DwpMappings dwpMappings;
    private static OfficeMapping[] allDwpBenefitOffices;

    static {
        try {
            json = IOUtils.resourceToString("reference-data/dwpAddresses.json",
                    Charset.forName("UTF-8"), Thread.currentThread().getContextClassLoader());
            Gson gson = new Gson();
            dwpMappings = gson.fromJson(json, DwpMappings.class);

            allDwpBenefitOffices = Stream.of(dwpMappings.getPip(), dwpMappings.getEsa())
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
        if (Objects.nonNull(caseData.getAppeal()) && Objects.nonNull(caseData.getAppeal().getMrnDetails())
            && Objects.nonNull(caseData.getAppeal().getMrnDetails().getDwpIssuingOffice())) {

            final OfficeAddress dwpAddress = lookup(caseData.getAppeal().getBenefitType().getCode(),
                caseData.getAppeal().getMrnDetails().getDwpIssuingOffice());

            return buildAddress(dwpAddress);
        } else {
            throw new NoMrnDetailsException(caseData);
        }
    }

    private OfficeAddress lookup(String benefitType, String dwpIssuingOffice) {
        Optional<OfficeMapping> officeMapping = getDwpMappingByOffice(benefitType, dwpIssuingOffice);

        if (!officeMapping.isPresent()) {
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

    public Optional<OfficeMapping> getDwpMappingByOffice(String benefitType, String dwpIssuingOffice) {
        log.info("looking up officeAddress for benefitType {} and dwpIssuingOffice {}", benefitType, dwpIssuingOffice);

        if (StringUtils.equalsIgnoreCase(dwpIssuingOffice, TEST_HMCTS_ADDRESS)) {
            return Optional.of(dwpMappings.getTestHmctsAddress());
        }
        Optional<OfficeMapping> officeMapping = Optional.empty();
        if (StringUtils.equalsIgnoreCase(PIP, benefitType)) {
            String dwpIssuingOfficeStripped = dwpIssuingOffice.replaceAll("\\D+","");
            officeMapping = getOfficeMappingByDwpIssuingOffice(dwpIssuingOfficeStripped, dwpMappings.getPip());
        } else if (StringUtils.equalsIgnoreCase(ESA, benefitType)) {
            officeMapping = getOfficeMappingByDwpIssuingOffice(dwpIssuingOffice, dwpMappings.getEsa());
        }  else if (StringUtils.equalsIgnoreCase(UC, benefitType)) {
            return Optional.of(dwpMappings.getUc());
        }

        return officeMapping;
    }

    private Optional<OfficeMapping> getOfficeMappingByDwpIssuingOffice(String dwpIssuingOffice, OfficeMapping[] mappings) {
        for (OfficeMapping office : mappings) {
            if (StringUtils.stripToEmpty(dwpIssuingOffice).equalsIgnoreCase(office.getCode())) {
                return Optional.of(office);
            }
        }
        return Optional.empty();
    }

    public OfficeMapping[] allDwpBenefitOffices() {
        return allDwpBenefitOffices;
    }
}
