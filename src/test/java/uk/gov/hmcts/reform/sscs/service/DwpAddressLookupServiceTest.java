package uk.gov.hmcts.reform.sscs.service;

import static java.util.Arrays.stream;
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

    @Test
    @Parameters({"The Pension Service 11", "Recovery from Estates"})
    public void attendanceAllowanceAddressesExist(final String dwpIssuingOffice) {
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
    public void pipOfficeMappings() {
        OfficeMapping[] result = dwpAddressLookup.pipOfficeMappings();

        assertEquals("DWP PIP (1)", result[0].getMapping().getGaps());
        assertEquals(10, result.length);
        assertTrue(stream(result).anyMatch(OfficeMapping::isDefault));
    }

    @Test
    public void ucOfficeMappings() {
        OfficeMapping[] result = dwpAddressLookup.ucOfficeMappings();
        assertEquals(1, result.length);
        assertTrue(stream(result).anyMatch(OfficeMapping::isDefault));
    }

    @Test
    public void bereavementBenefitOfficeMappings() {
        OfficeMapping[] result = dwpAddressLookup.bereavementBenefitOfficeMappings();
        assertEquals(1, result.length);
        assertTrue(stream(result).anyMatch(OfficeMapping::isDefault));
    }

    @Test
    public void carersAllowanceOfficeMappings() {
        OfficeMapping[] result = dwpAddressLookup.carersAllowanceOfficeMappings();
        assertEquals(1, result.length);
        assertTrue(stream(result).anyMatch(OfficeMapping::isDefault));
    }

    @Test
    public void maternityAllowanceOfficeMappings() {
        OfficeMapping[] result = dwpAddressLookup.maternityAllowanceOfficeMappings();
        assertEquals(1, result.length);
        assertTrue(stream(result).anyMatch(OfficeMapping::isDefault));
    }

    @Test
    public void esaOfficeMappings() {
        OfficeMapping[] result = dwpAddressLookup.esaOfficeMappings();
        assertEquals(13, result.length);
        assertTrue(stream(result).anyMatch(OfficeMapping::isDefault));
    }

    @Test
    public void jsaOfficeMappings() {
        OfficeMapping[] result = dwpAddressLookup.jsaOfficeMappings();
        assertEquals(4, result.length);
        assertTrue(stream(result).anyMatch(OfficeMapping::isDefault));
    }

    @Test
    public void socialFundOfficeMappings() {
        OfficeMapping[] result = dwpAddressLookup.socialFundOfficeMappings();
        assertEquals(3, result.length);
        assertTrue(stream(result).anyMatch(OfficeMapping::isDefault));
    }

    @Test
    public void dlaOfficeMappings() {
        OfficeMapping[] result = dwpAddressLookup.dlaOfficeMappings();
        assertEquals(3, result.length);
        assertTrue(stream(result).anyMatch(OfficeMapping::isDefault));
    }

    @Test
    public void attendanceAllowanceOfficeMappings() {
        OfficeMapping[] result = dwpAddressLookup.attendanceAllowanceOfficeMappings();
        assertEquals(2, result.length);
        assertTrue(stream(result).anyMatch(OfficeMapping::isDefault));
    }

    @Test
    public void incomeSupportOfficeMappings() {
        OfficeMapping[] result = dwpAddressLookup.incomeSupportOfficeMappings();
        assertEquals(4, result.length);
        assertTrue(stream(result).anyMatch(OfficeMapping::isDefault));
    }

    @Test
    @Parameters({
            "PIP, 10",
            "UC, 1",
            "ESA, 13",
            "DLA, 3",
            "CARERS_ALLOWANCE, 1",
            "BEREAVEMENT_BENEFIT, 1",
            "ATTENDANCE_ALLOWANCE, 2",
            "MATERNITY_ALLOWANCE, 1",
            "JSA, 4"
    })
    public void getDwpOfficeMappings(Benefit benefit, int expectedNumberOfOffices) {
        OfficeMapping[] officeMappings = dwpAddressLookup.getDwpOfficeMappings(benefit.getShortName());
        assertEquals(officeMappings.length, expectedNumberOfOffices);
        assertTrue(stream(officeMappings).anyMatch(OfficeMapping::isDefault));
    }

    @Test
    public void givenAPipBenefitTypeAndDwpOffice_thenReturnAPipOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("pip", "3");

        assertTrue(result.isPresent());
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

        assertTrue(result.isPresent());
        assertEquals("AE", result.get().getCode());
    }

    @Test
    public void givenAEsaBenefitTypeAndDwpOffice_thenReturnEsaEmpty() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("esa", "Balham DRT");

        assertTrue(result.isPresent());
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

        assertTrue(result.isPresent());
        assertEquals("Universal Credit", result.get().getCode());
    }

    @Test
    public void givenAUcBenefitTypeAndDwpOffice_thenCorrectDwpRegionalCenter() {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("uc", null);

        assertEquals("Universal Credit", result);
    }

    @Test
    public void givenAPipBenefitTypeAndDwpOffice_thenCorrectDwpRegionalCenter() {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("pip", "3");

        assertEquals("Bellevale", result);
    }

    @Test
    public void givenAEsaBenefitTypeAndDwpOffice_thenCorrectDwpRegionalCenter() {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("esa", "Balham DRT");

        assertEquals("Balham DRT", result);
    }

    @Test
    public void givenAAttendanceAllowanceBenefitTypeAndRfeDwpOffice_thenCorrectDwpRegionalCenter() {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("attendanceAllowance", "Recovery from Estates");

        assertEquals("RfE", result);
    }

    @Test
    public void givenAAttendanceAllowanceBenefitTypeAndAaDwpOffice_thenCorrectDwpRegionalCenter() {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("attendanceAllowance", "The Pension Service 11");

        assertEquals("Attendance Allowance", result);
    }

    @Test
    public void givenACarersAllowanceBenefitTypeAndDwpOffice_thenCorrectDwpRegionalCenter() {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("carersAllowance", null);

        assertEquals("Carers Allowance", result);
    }

    @Test
    public void givenACarersAllowanceBenefitType_thenReturnTheOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("carersAllowance", null);

        assertTrue(result.isPresent());
        assertEquals("Carer’s Allowance Dispute Resolution Team", result.get().getCode());
    }

    @Test
    public void givenAnAttendanceAllowanceBenefitType_thenReturnTheOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("attendanceAllowance", "Recovery from Estates");

        assertTrue(result.isPresent());
        assertEquals("Recovery from Estates", result.get().getCode());
    }

    @Test
    public void givenABereavementBenefitTypeAndDwpOffice_thenCorrectDwpRegionalCenter() {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("bereavementBenefit", null);

        assertEquals("Bereavement Benefit", result);
    }

    @Test
    public void givenABereavementBenefitType_thenReturnTheOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("bereavementBenefit", null);

        assertTrue(result.isPresent());
        assertEquals("Pensions Dispute Resolution Team", result.get().getCode());
    }

    @Test
    @Parameters({
            "Disability Benefit Centre 4, DLA Child/Adult", "The Pension Service 11, DLA 65", "Recovery from Estates, RfE"
    })
    public void givenADlaBenefitType_thenReturnTheCorrectDwpRegionalCentre(String office, String dwpRegionalCentre) {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("dla", office);

        assertEquals(dwpRegionalCentre, result);
    }

    @Test
    public void givenAMaternityAllowance_thenReturnTheOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("maternityAllowance", null);

        assertTrue(result.isPresent());
        assertEquals("Walsall Benefit Centre", result.get().getCode());
    }

    @Test
    @Parameters({
            "Barrow IIDB Centre, IIDB Barrow", "Barnsley Benefit Centre, IIDB Barnsley"
    })
    public void givenAIidbBenefitType_thenReturnTheCorrectDwpRegionalCentre(String office, String dwpRegionalCentre) {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("industrialInjuriesDisablement", office);

        assertEquals(dwpRegionalCentre, result);
    }

    @Test
    public void givenABenefitTypeNotMapped_thenReturnEmptyOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("hb", null);

        assertTrue(result.isEmpty());
    }

    @Test
    public void givenAPipBenefitType_thenReturnTheDefaultPipOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDefaultDwpMappingByBenefitType("pip");

        assertTrue(result.isPresent());
        assertEquals("1", result.get().getCode());
    }

    @Test
    public void givenAEsaBenefitType_thenReturnTheDefaultEsaOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDefaultDwpMappingByBenefitType("esa");

        assertTrue(result.isPresent());
        assertEquals("Sheffield DRT", result.get().getCode());
    }

    @Test
    public void givenAUcBenefitType_thenReturnTheDefaultUcOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDefaultDwpMappingByBenefitType("uc");

        assertTrue(result.isPresent());
        assertEquals("Universal Credit", result.get().getCode());
    }

    @Test
    public void givenAIidbBenefitType_thenReturnTheDefaultIidbOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDefaultDwpMappingByBenefitType("industrialInjuriesDisablement");

        assertTrue(result.isPresent());
        assertEquals("Barrow IIDB Centre", result.get().getCode());
    }

    @Test
    public void givenAMaternityAllowanceBenefitType_thenReturnTheDefaultMaternityAllowanceOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDefaultDwpMappingByBenefitType("maternityAllowance");

        assertTrue(result.isPresent());
        assertEquals("Walsall Benefit Centre", result.get().getCode());
    }

    @Test
    public void givenAnIncomeSupportBenefitType_thenReturnTheDefaultIncomeSupprtOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDefaultDwpMappingByBenefitType("incomeSupport");

        assertTrue(result.isPresent());
        assertEquals("Worthing DRT", result.get().getCode());
    }

    @Test
    public void allDwpMappingsHaveADwpRegionCentre() {
        stream(Benefit.values())
            .flatMap(benefit -> stream(benefit.getOfficeMappings().apply(dwpAddressLookup)))
            .forEach(f -> assertNotNull(f.getMapping().getDwpRegionCentre()));
    }

}
