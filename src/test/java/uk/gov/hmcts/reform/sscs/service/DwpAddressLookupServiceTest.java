package uk.gov.hmcts.reform.sscs.service;

import static org.junit.Assert.*;
import static uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils.buildCaseData;

import java.util.List;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.domain.MrnDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.exception.DwpAddressLookupException;
import uk.gov.hmcts.reform.sscs.exception.NoMrnDetailsException;
import uk.gov.hmcts.reform.sscs.model.dwp.OfficeAddress;

@RunWith(JUnitParamsRunner.class)
public class DwpAddressLookupServiceTest {

    private static final String PIP = "PIP";
    private static final String ESA = "ESA";

    private SscsCaseData caseData;

    private final DwpAddressLookupService dwpAddressLookup = new DwpAddressLookupService();

    @Before
    public void setup() {
        caseData = buildCaseData();
    }

    @Test
    public void dwpLookupAddressShouldBeHandled() {
        Address address = dwpAddressLookup.lookupDwpAddress(caseData);

        assertEquals("Mail Handling Site A", address.getLine1());
        assertEquals("WOLVERHAMPTON", address.getTown());
        assertEquals("WV98 1AA", address.getPostcode());
    }

    @Test
    @Parameters({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"})
    public void pipAddressesExist(final String dwpIssuingOffice) {
        OfficeAddress address = dwpAddressLookup.lookup(PIP, dwpIssuingOffice);
        assertNotNull(address);
    }

    @Test
    @Parameters({
        "PIP, 1", "pip, 1", "PiP, 1", "pIP, 1",
        "ESA, Balham DRT", "EsA, Balham DRT", "esa, Balham DRT"
    })
    public void benefitTypeIsCaseInsensitive(final String benefitType, String dwpIssuingOffice) {
        OfficeAddress address = dwpAddressLookup.lookup(benefitType, dwpIssuingOffice);
        assertNotNull(address);
    }

    @Test
    public void dwpOfficeStripsText() {
        OfficeAddress address = dwpAddressLookup.lookup("PIP", "DWP Issuing Office(10)");
        assertNotNull(address);
    }

    @Test
    public void handleCaseInsensitiveAddresses() {
        OfficeAddress address = dwpAddressLookup.lookup("ESA", "BALHAM DRT");
        assertNotNull(address);
    }

    @Test
    @Parameters({
        "Balham DRT", "Birkenhead LM DRT", "Lowestoft DRT", "Wellingborough DRT", "Chesterfield DRT",
        "Coatbridge Benefit Centre", "Inverness DRT", "Milton Keynes DRT", "Springburn DRT", "Watford DRT",
        "Norwich DRT", "Sheffield DRT", "Worthing DRT"
    })
    public void esaAddressesExist(final String dwpIssuingOffice) {
        OfficeAddress address = dwpAddressLookup.lookup(ESA, dwpIssuingOffice);
        assertNotNull(address);
    }

    @Test(expected = DwpAddressLookupException.class)
    @Parameters({"JOB", "UNK", "PLOP", "BIG", "FIG"})
    public void unknownBenefitTypeReturnsNone(final String benefitType) {
        dwpAddressLookup.lookup(benefitType, "1");
    }

    @Test(expected = DwpAddressLookupException.class)
    @Parameters({"11", "12", "13", "14", "JOB"})
    public void unknownPipDwpIssuingOfficeReturnsNone(final String dwpIssuingOffice) {
        dwpAddressLookup.lookup(PIP, dwpIssuingOffice);
    }

    @Test(expected = DwpAddressLookupException.class)
    @Parameters({"JOB", "UNK", "PLOP", "BIG", "11"})
    public void unknownEsaDwpIssuingOfficeReturnsNone(final String dwpIssuingOffice) {
        dwpAddressLookup.lookup(ESA, dwpIssuingOffice);
    }

    @Test
    @Parameters({"PIP", "ESA", "JOB", "UNK", "PLOP", "BIG", "11"})
    public void willAlwaysReturnTestAddressForATestDwpIssuingOffice(final String benefitType) {
        OfficeAddress address = dwpAddressLookup.lookup(benefitType, "testHmctsAddress");
        assertNotNull(address);
        assertEquals("E1 8FA", address.getPostCode());
    }

    @Test(expected = NoMrnDetailsException.class)
    public void asAppealWithNoMrnDetailsWillNotHaveADwpAddress() {
        caseData = buildCaseData();
        caseData.setRegionalProcessingCenter(null);

        caseData = caseData.toBuilder().appeal(caseData.getAppeal().toBuilder().mrnDetails(null).build()).build();
        dwpAddressLookup.lookupDwpAddress(caseData);
    }

    @Test(expected = NoMrnDetailsException.class)
    public void anAppealWithNoDwpIssuingOfficeWillNotHaveADwpAddress() {
        caseData = buildCaseData();
        caseData.setRegionalProcessingCenter(null);

        caseData = caseData.toBuilder().appeal(caseData.getAppeal().toBuilder().mrnDetails(
            MrnDetails.builder().mrnLateReason("soz").build()).build()).build();
        dwpAddressLookup.lookupDwpAddress(caseData);
    }

    @Test
    public void generateListOfDwpGapsOfficeNames() {
        List<String> result = dwpAddressLookup.getDwpGapsOffices();

        assertEquals("DWP PIP (1)", result.get(0));
        assertEquals(23, result.size());
    }
}
