package uk.gov.hmcts.reform.sscs.service;

import static org.junit.Assert.*;
import static uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils.buildCaseData;

import java.util.Optional;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.gov.hmcts.reform.sscs.ccd.domain.*;
import uk.gov.hmcts.reform.sscs.exception.DwpAddressLookupException;
import uk.gov.hmcts.reform.sscs.exception.NoMrnDetailsException;
import uk.gov.hmcts.reform.sscs.model.dwp.OfficeMapping;

@RunWith(JUnitParamsRunner.class)
public class DwpAddressLookupServiceTest {

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
    @Parameters({"1", "2", "3", "4", "5", "6", "7", "8", "9", "(AE)"})
    public void pipAddressesExist(final String dwpIssuingOffice) {
        SscsCaseData caseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("PIP").build()).mrnDetails(MrnDetails.builder().dwpIssuingOffice(dwpIssuingOffice).build()).build()).build();
        Address address = dwpAddressLookup.lookupDwpAddress(caseData);
        assertNotNull(address);
    }

    @Test
    @Parameters({
        "PIP, 1", "pip, 1", "PiP, 1", "pIP, 1",
        "ESA, Balham DRT", "EsA, Balham DRT", "esa, Balham DRT"
    })
    public void benefitTypeIsCaseInsensitive(final String benefitType, String dwpIssuingOffice) {
        SscsCaseData caseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code(benefitType).build()).mrnDetails(MrnDetails.builder().dwpIssuingOffice(dwpIssuingOffice).build()).build()).build();
        Address address = dwpAddressLookup.lookupDwpAddress(caseData);
        assertNotNull(address);
    }

    @Test
    public void dwpOfficeStripsText() {
        SscsCaseData caseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("PIP").build()).mrnDetails(MrnDetails.builder().dwpIssuingOffice("DWP Issuing Office(9)").build()).build()).build();
        Address address = dwpAddressLookup.lookupDwpAddress(caseData);
        assertNotNull(address);
    }

    @Test
    public void handleCaseInsensitiveAddresses() {
        SscsCaseData caseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("ESA").build()).mrnDetails(MrnDetails.builder().dwpIssuingOffice("BALHAM DRT").build()).build()).build();
        Address address = dwpAddressLookup.lookupDwpAddress(caseData);
        assertNotNull(address);
    }

    @Test
    @Parameters({
        "Balham DRT", "Birkenhead LM DRT", "Lowestoft DRT", "Wellingborough DRT", "Chesterfield DRT",
        "Coatbridge Benefit Centre", "Inverness DRT", "Milton Keynes DRT", "Springburn DRT", "Watford DRT",
        "Norwich DRT", "Sheffield DRT", "Worthing DRT"
    })
    public void esaAddressesExist(final String dwpIssuingOffice) {
        SscsCaseData caseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("ESA").build()).mrnDetails(MrnDetails.builder().dwpIssuingOffice(dwpIssuingOffice).build()).build()).build();
        Address address = dwpAddressLookup.lookupDwpAddress(caseData);
        assertNotNull(address);
    }

    @Test
    @Parameters({
            "Disability Benefit Centre 4", "The Pension Service 11", "Recovery from Estates"
    })
    public void dlaAddressesExist(final String dwpIssuingOffice) {
        SscsCaseData caseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("DLA").build()).mrnDetails(MrnDetails.builder().dwpIssuingOffice(dwpIssuingOffice).build()).build()).build();
        Address address = dwpAddressLookup.lookupDwpAddress(caseData);
        assertNotNull(address);
    }

    @Test(expected = DwpAddressLookupException.class)
    @Parameters({"JOB", "UNK", "PLOP", "BIG", "FIG"})
    public void unknownBenefitTypeReturnsNone(final String benefitType) {
        SscsCaseData caseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code(benefitType).build()).mrnDetails(MrnDetails.builder().dwpIssuingOffice("1").build()).build()).build();
        dwpAddressLookup.lookupDwpAddress(caseData);
    }

    @Test(expected = DwpAddressLookupException.class)
    @Parameters({"11", "12", "13", "14", "JOB"})
    public void unknownPipDwpIssuingOfficeReturnsNone(final String dwpIssuingOffice) {
        SscsCaseData caseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("PIP").build()).mrnDetails(MrnDetails.builder().dwpIssuingOffice(dwpIssuingOffice).build()).build()).build();
        dwpAddressLookup.lookupDwpAddress(caseData);
    }

    @Test(expected = DwpAddressLookupException.class)
    @Parameters({"JOB", "UNK", "PLOP", "BIG", "11"})
    public void unknownEsaDwpIssuingOfficeReturnsNone(final String dwpIssuingOffice) {
        SscsCaseData caseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("ESA").build()).mrnDetails(MrnDetails.builder().dwpIssuingOffice(dwpIssuingOffice).build()).build()).build();
        dwpAddressLookup.lookupDwpAddress(caseData);
    }

    @Test
    @Parameters({"PIP", "ESA", "JOB", "UNK", "PLOP", "BIG", "11"})
    public void willAlwaysReturnTestAddressForATestDwpIssuingOffice(final String benefitType) {
        SscsCaseData caseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code(benefitType).build()).mrnDetails(MrnDetails.builder().dwpIssuingOffice("test-hmcts-address").build()).build()).build();
        Address address = dwpAddressLookup.lookupDwpAddress(caseData);
        assertNotNull(address);
        assertEquals("E1 8FA", address.getPostcode());
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
    public void getDwpOfficeNames() {
        OfficeMapping[] result = dwpAddressLookup.allDwpBenefitOffices();

        assertEquals("DWP PIP (1)", result[0].getMapping().getGaps());
        assertTrue(result[0].isDefault());
        assertFalse(result[1].isDefault());
        assertEquals(27, result.length);
    }

    @Test
    public void givenAPipBenefitTypeAndDwpOffice_thenReturnAPipOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("pip", "3");

        assertEquals("3", result.get().getCode());
    }

    @Test
    public void givenAEsaBenefitTypeAndInvalidOffice_thenReturnEmpty() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("esa", "3");

        assertEquals(Optional.empty(), result);
    }

    @Test
    @Parameters({"(AE)", "AE", "DWP PIP (AE)"})
    public void givenAPipBenefitTypeAndAeOffice_thenFuzzyMatch(String pipAe) {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("pip", pipAe);

        assertEquals("AE", result.get().getCode());
    }

    @Test
    public void givenAEsaBenefitTypeAndDwpOffice_thenReturnEsaEmpty() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("esa", "Balham DRT");

        assertEquals("Balham DRT", result.get().getCode());
    }

    @Test
    public void givenAPipBenefitTypeAndInvalidOffice_thenReturnEmpty() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("pip", "Balham DRT");

        assertEquals(Optional.empty(), result);
    }

    @Test
    public void givenAUcBenefitType_thenReturnTheUcOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("uc", null);

        assertEquals("Universal Credit", result.get().getCode());
    }

    @Test
    public void givenAPipBenefitTypeAndDwpOffice_thenCorrectDwpRegionalCenter() {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("pip", "3");

        assertEquals("Bellevale", result);
    }

    @Test
    public void givenAEsaBenefitTypeAndDwpOffice_thenCorrectDwpRegionalCenter() {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("esa", "Balham DRT");

        assertEquals("Sheffield DRT", result);
    }

    @Test
    public void givenACarresAllowanceBenefitType_thenReturnTheUcOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("carersAllowance", null);

        assertEquals("Carer’s Allowance", result.get().getCode());
    }

    @Test
    public void givenABenefitTypeNotMapped_thenReturnEmptyOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("hb", null);

        assertTrue(result.isEmpty());
    }

    @Test
    public void givenAPipBenefitType_thenReturnTheDefaultPipOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDefaultDwpMappingByBenefitType("pip");

        assertEquals("1", result.get().getCode());
    }

    @Test
    public void givenAEsaBenefitType_thenReturnTheDefaultEsaOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDefaultDwpMappingByBenefitType("esa");

        assertEquals("Sheffield DRT", result.get().getCode());
    }

    @Test
    public void givenAUcBenefitType_thenReturnTheDefaultUcOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDefaultDwpMappingByBenefitType("uc");

        assertEquals("Universal Credit", result.get().getCode());
    }

    @Test
    public void givenAPipBenefitType_thenDefaultDwpRegionalCenter() {
        String result = dwpAddressLookup.getDefaultDwpRegionalCenterByBenefitTypeAndOffice("pip");

        assertEquals("Newcastle", result);
    }

    @Test
    public void givenAEsaBenefitType_thenDefaultDwpRegionalCenter() {
        String result = dwpAddressLookup.getDefaultDwpRegionalCenterByBenefitTypeAndOffice("esa");

        assertEquals("Sheffield DRT", result);
    }

    @Test
    public void givenAUcBenefitType_thenDefaultDwpRegionalCenter() {
        String result = dwpAddressLookup.getDefaultDwpRegionalCenterByBenefitTypeAndOffice("uc");

        assertEquals("Universal Credit", result);
    }

    @Test
    public void givenACarersAllowanceBenefitType_thenDefaultDwpRegionalCenter() {
        String result = dwpAddressLookup.getDefaultDwpRegionalCenterByBenefitTypeAndOffice("carersAllowance");

        assertEquals("Tyneview Park DRT", result);
    }

    @Test
    public void givenABenefitTypeNotMapped_thenEmptyDwpRegionalCenter() {
        String result = dwpAddressLookup.getDefaultDwpRegionalCenterByBenefitTypeAndOffice("hb");

        assertNull(result);
    }

    @Test
    public void givenADlaBenefitType_thenDefaultDwpRegionalCenter() {
        String result = dwpAddressLookup.getDefaultDwpRegionalCenterByBenefitTypeAndOffice("dla");

        assertEquals("Bradford DRT", result);
    }
}
