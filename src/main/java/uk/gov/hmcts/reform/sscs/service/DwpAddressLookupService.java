package uk.gov.hmcts.reform.sscs.service;

import com.google.gson.Gson;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
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
    private static final String TEST_HMCTS_ADDRESS = "testHmctsAddress";

    private static String json;
    private static DwpMappings dwpMappings;
    private static OfficeMapping[] allDwpBenefitOffices;

    static {
        try {
            json = IOUtils.resourceToString("reference-data/dwpAddresses.json",
                    Charset.forName("UTF-8"), Thread.currentThread().getContextClassLoader());
            Gson gson = new Gson();
            dwpMappings = gson.fromJson(json, DwpMappings.class);
            OfficeMapping[] pipOffices = dwpMappings.getPip();
            OfficeMapping[] esaOffices = dwpMappings.getEsa();

            allDwpBenefitOffices = ArrayUtils.addAll(pipOffices, esaOffices);
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

    OfficeAddress lookup(String benefitType, String dwpIssuingOffice) {
        OfficeAddress dwpAddress = getDwpAddress(benefitType, dwpIssuingOffice);

        if (dwpAddress == null) {
            throw new DwpAddressLookupException(String.format("could not find dwp officeAddress for benefitType %s and dwpIssuingOffice %s",
                    benefitType, dwpIssuingOffice));
        }
        return dwpAddress;
    }

    private Address buildAddress(OfficeAddress dwpAddress) {
        return Address.builder()
            .line1(dwpAddress.getLine1())
            .town(dwpAddress.getLine2())
            .county(dwpAddress.getLine3())
            .postcode(dwpAddress.getPostCode())
            .build();
    }

    public OfficeAddress getDwpAddress(String benefitType, String dwpIssuingOffice) {
        log.info("looking up officeAddress for benefitType {} and dwpIssuingOffice {}", benefitType, dwpIssuingOffice);

        if (StringUtils.equalsIgnoreCase(dwpIssuingOffice, TEST_HMCTS_ADDRESS)) {
            return dwpMappings.getTestHmctsAddress().getAddress();
        }
        if (StringUtils.equalsIgnoreCase(PIP, benefitType)) {
            String dwpIssuingOfficeStripped = dwpIssuingOffice.replaceAll("\\D+","");
            return getOfficeAddressByOffice(dwpIssuingOfficeStripped);
        } else if (StringUtils.equalsIgnoreCase(ESA, benefitType)) {
            return getOfficeAddressByOffice(dwpIssuingOffice);
        }
        return null;
    }

    private OfficeAddress getOfficeAddressByOffice(String dwpIssuingOffice) {
        for (OfficeMapping office : allDwpBenefitOffices) {
            if (StringUtils.stripToEmpty(dwpIssuingOffice).equalsIgnoreCase(office.getCode())) {
                return office.getAddress();
            }
        }
        return null;
    }

    public List<String> getDwpGapsOffices() {

        List<String> gapsOffices = new ArrayList<>();
        for (OfficeMapping office : allDwpBenefitOffices) {
            gapsOffices.add(office.getMapping().getGaps());
        }
        return gapsOffices;
    }

}
