package uk.gov.hmcts.reform.sscs.service;

import static java.util.Arrays.stream;
import static org.apache.commons.io.IOUtils.resourceToString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils.buildCaseData;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appeal;
import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitType;
import uk.gov.hmcts.reform.sscs.ccd.domain.MrnDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.exception.DwpAddressLookupException;
import uk.gov.hmcts.reform.sscs.exception.NoMrnDetailsException;
import uk.gov.hmcts.reform.sscs.model.dwp.OfficeMapping;

public class DwpAddressLookupServiceTest {

    private final DwpAddressLookupService dwpAddressLookup = new DwpAddressLookupService();
    private SscsCaseData caseData;

    @BeforeEach
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

    @ParameterizedTest
    @CsvSource({"1", "2", "3", "4", "5", "6", "7", "8", "9", "(AE)"})
    public void pipAddressesExist(final String dwpIssuingOffice) {
        SscsCaseData caseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("PIP").build()).mrnDetails(MrnDetails.builder().dwpIssuingOffice(dwpIssuingOffice).build()).build()).build();
        Address address = dwpAddressLookup.lookupDwpAddress(caseData);
        assertNotNull(address);
    }

    @ParameterizedTest
    @CsvSource({"PIP (3)", "  PIP 3  ", "PIP 3", "DWP PIP (3)", "(AE)", "AE", "PIP AE", "DWP PIP (AE)", "Recovery from Estates", "PIP Recovery from Estates"})
    public void pipFuzzyMatchingAddressesExist(final String dwpIssuingOffice) {
        SscsCaseData caseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("PIP").build()).mrnDetails(MrnDetails.builder().dwpIssuingOffice(dwpIssuingOffice).build()).build()).build();
        Address address = dwpAddressLookup.lookupDwpAddress(caseData);
        assertNotNull(address);
    }

    @ParameterizedTest
    @CsvSource({
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

    @ParameterizedTest
    @CsvSource({
        "Balham DRT", "Birkenhead LM DRT", "Lowestoft DRT", "Wellingborough DRT", "Chesterfield DRT",
        "Coatbridge Benefit Centre", "Inverness DRT", "Milton Keynes DRT", "Springburn DRT", "Watford DRT",
        "Norwich DRT", "Sheffield DRT", "Worthing DRT"
    })
    public void esaAddressesExist(final String dwpIssuingOffice) {
        SscsCaseData caseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("ESA").build()).mrnDetails(MrnDetails.builder().dwpIssuingOffice(dwpIssuingOffice).build()).build()).build();
        Address address = dwpAddressLookup.lookupDwpAddress(caseData);
        assertNotNull(address);
    }

    @ParameterizedTest
    @CsvSource({
        "Disability Benefit Centre 4", "The Pension Service 11", "Recovery from Estates"
    })
    public void dlaAddressesExist(final String dwpIssuingOffice) {
        SscsCaseData caseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("DLA").build()).mrnDetails(MrnDetails.builder().dwpIssuingOffice(dwpIssuingOffice).build()).build()).build();
        Address address = dwpAddressLookup.lookupDwpAddress(caseData);
        assertNotNull(address);
    }

    @ParameterizedTest
    @CsvSource({"The Pension Service 11", "Recovery from Estates"})
    public void attendanceAllowanceAddressesExist(final String dwpIssuingOffice) {
        SscsCaseData caseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("DLA").build()).mrnDetails(MrnDetails.builder().dwpIssuingOffice(dwpIssuingOffice).build()).build()).build();
        Address address = dwpAddressLookup.lookupDwpAddress(caseData);
        assertNotNull(address);
    }

    @ParameterizedTest
    @CsvSource({"JOB", "UNK", "PLOP", "BIG", "FIG"})
    public void unknownBenefitTypeReturnsNone(final String benefitType) {
        SscsCaseData caseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code(benefitType).build()).mrnDetails(MrnDetails.builder().dwpIssuingOffice("1").build()).build()).build();
        assertThrows(DwpAddressLookupException.class, () -> dwpAddressLookup.lookupDwpAddress(caseData));
    }

    @ParameterizedTest
    @CsvSource({"11", "12", "13", "14", "JOB"})
    public void unknownPipDwpIssuingOfficeReturnsNone(final String dwpIssuingOffice) {
        SscsCaseData caseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("PIP").build()).mrnDetails(MrnDetails.builder().dwpIssuingOffice(dwpIssuingOffice).build()).build()).build();
        assertThrows(DwpAddressLookupException.class, () -> dwpAddressLookup.lookupDwpAddress(caseData));
    }

    @ParameterizedTest
    @CsvSource({"JOB", "UNK", "PLOP", "BIG", "11"})
    public void unknownEsaDwpIssuingOfficeReturnsNone(final String dwpIssuingOffice) {
        SscsCaseData caseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("ESA").build()).mrnDetails(MrnDetails.builder().dwpIssuingOffice(dwpIssuingOffice).build()).build()).build();
        assertThrows(DwpAddressLookupException.class, () -> dwpAddressLookup.lookupDwpAddress(caseData));
    }

    @ParameterizedTest
    @CsvSource({"PIP", "ESA", "JOB", "UNK", "PLOP", "BIG", "11"})
    public void willAlwaysReturnTestAddressForATestDwpIssuingOffice(final String benefitType) {
        SscsCaseData caseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code(benefitType).build()).mrnDetails(MrnDetails.builder().dwpIssuingOffice("test-hmcts-address").build()).build()).build();
        Address address = dwpAddressLookup.lookupDwpAddress(caseData);
        assertNotNull(address);
        assertEquals("E1 8FA", address.getPostcode());
    }

    @Test
    public void asAppealWithNoMrnDetailsWillNotHaveADwpAddress() {
        caseData = buildCaseData();
        caseData.setRegionalProcessingCenter(null);

        caseData = caseData.toBuilder().appeal(caseData.getAppeal().toBuilder().mrnDetails(null).build()).build();
        assertThrows(NoMrnDetailsException.class, () -> dwpAddressLookup.lookupDwpAddress(caseData));
    }

    @Test
    public void anAppealWithNoDwpIssuingOfficeWillNotHaveADwpAddress() {
        caseData = buildCaseData();
        caseData.setRegionalProcessingCenter(null);

        caseData = caseData.toBuilder().appeal(caseData.getAppeal().toBuilder().mrnDetails(
                MrnDetails.builder().mrnLateReason("soz").build()).build()).build();
        assertThrows(NoMrnDetailsException.class, () -> dwpAddressLookup.lookupDwpAddress(caseData));
    }

    @Test
    public void pipOfficeMappings() {
        OfficeMapping[] result = dwpAddressLookup.pipOfficeMappings();

        assertEquals("DWP PIP (1)", result[0].getMapping().getGaps());
        assertEquals(11, result.length);
        assertTrue(stream(result).anyMatch(OfficeMapping::isDefault));
    }

    @Test
    public void ucOfficeMappings() {
        OfficeMapping[] result = dwpAddressLookup.ucOfficeMappings();
        assertEquals(2, result.length);
        assertTrue(stream(result).anyMatch(OfficeMapping::isDefault));
    }

    @Test
    public void bereavementBenefitOfficeMappings() {
        OfficeMapping[] result = dwpAddressLookup.bereavementBenefitOfficeMappings();
        assertEquals(1, result.length);
        assertTrue(stream(result).anyMatch(OfficeMapping::isDefault));
    }

    @Test
    public void childSupportOfficeMappings() {
        OfficeMapping[] result = dwpAddressLookup.childSupportOfficeMappings();
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
        assertEquals(14, result.length);
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
    public void industrialDeathBenefitOfficeMappings() {
        OfficeMapping[] result = dwpAddressLookup.industrialDeathBenefitOfficeMappings();
        assertEquals(2, result.length);
        assertTrue(stream(result).anyMatch(OfficeMapping::isDefault));
    }

    @Test
    public void pensionCreditsOfficeMappings() {
        OfficeMapping[] result = dwpAddressLookup.pensionCreditsOfficeMappings();
        assertEquals(2, result.length);
        assertTrue(stream(result).anyMatch(OfficeMapping::isDefault));
    }

    @Test
    public void retirementPensionOfficeMappings() {
        OfficeMapping[] result = dwpAddressLookup.retirementPensionOfficeMappings();
        assertEquals(2, result.length);
        assertTrue(stream(result).anyMatch(OfficeMapping::isDefault));
    }

    @ParameterizedTest
    @CsvSource({
        "PIP, 11",
        "UC, 2",
        "ESA, 14",
        "DLA, 3",
        "CARERS_ALLOWANCE, 1",
        "BEREAVEMENT_BENEFIT, 1",
        "ATTENDANCE_ALLOWANCE, 2",
        "MATERNITY_ALLOWANCE, 1",
        "JSA, 4",
        "IIDB, 2",
        "SOCIAL_FUND, 3",
        "INCOME_SUPPORT, 4",
        "BEREAVEMENT_SUPPORT_PAYMENT_SCHEME, 1",
        "INDUSTRIAL_DEATH_BENEFIT, 2",
        "PENSION_CREDIT, 2",
        "RETIREMENT_PENSION, 2",
    })
    public void getDwpOfficeMappings(Benefit benefit, int expectedNumberOfOffices) {
        OfficeMapping[] officeMappings = dwpAddressLookup.getDwpOfficeMappings(benefit.getShortName());
        assertEquals(officeMappings.length, expectedNumberOfOffices);
        assertTrue(stream(officeMappings).anyMatch(OfficeMapping::isDefault));
    }

    @ParameterizedTest
    @CsvSource({"3, 3", "PIP Recovery from Estates, Recovery from Estates"})
    public void givenAPipBenefitTypeAndDwpOffice_thenReturnAPipOffice(String office, String expectedResult) {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("pip", office);

        assertTrue(result.isPresent());
        assertEquals(expectedResult, result.get().getCode());
    }

    @Test
    public void givenAEsaBenefitTypeAndInvalidOffice_thenReturnEmpty() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("esa", "3");

        assertEquals(Optional.empty(), result);
    }

    @ParameterizedTest
    @CsvSource({"(AE)", "AE", "DWP PIP (AE)"})
    public void givenAPipBenefitTypeAndAeOffice_thenFuzzyMatch(String pipAe) {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("pip", pipAe);

        assertTrue(result.isPresent());
        assertEquals("AE", result.get().getCode());
    }

    @ParameterizedTest
    @CsvSource({"Balham DRT, Balham DRT", "ESA Recovery from Estates, Recovery from Estates"})
    public void givenAEsaBenefitTypeAndDwpOffice_thenReturnEsaOffice(String office, String expectedResult) {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("esa", office);

        assertTrue(result.isPresent());
        assertEquals(expectedResult, result.get().getCode());
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
    public void givenAUcBenefitTypeAndOffice_thenReturnTheUcOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("uc", "UC Recovery from Estates");

        assertEquals("Recovery from Estates", result.get().getCode());
    }

    @Test
    public void givenAnIncomeSupportBenefitTypeAndSheffieldOffice_thenReturnTheRfeOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("incomeSupport", "Recovery from Estates");

        assertEquals("Recovery from Estates", result.get().getCode());
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
    public void givenAChildSupportTypeAndDwpOffice_thenCorrectDwpRegionalCenter() {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("childSupport", null);

        assertEquals("Child Support", result);
    }

    @Test
    public void givenAChildSupportType_thenReturnTheOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("childSupport", null);

        assertTrue(result.isPresent());
        assertEquals("Child Maintenance Service Group", result.get().getCode());
    }

    @Test
    public void givenATaxCreditTypeAndDwpOffice_thenCorrectDwpRegionalCenter() {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("taxCredit", null);

        assertEquals("TCO Preston Appeals Team", result);
    }

    @Test
    public void givenATaxCreditType_thenReturnTheOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("taxCredit", null);

        assertTrue(result.isPresent());
        assertEquals("Tax Credit Office", result.get().getCode());
    }

    @Test
    public void givenAGuardiansAllowanceTypeAndDwpOffice_thenCorrectDwpRegionalCenter() {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("guardiansAllowance", null);

        assertEquals("C.B.O (HMRC)", result);
    }

    @Test
    public void givenAGuardiansAllowanceType_thenReturnTheOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("guardiansAllowance", null);

        assertTrue(result.isPresent());
        assertEquals("Child Benefit Office", result.get().getCode());
    }

    @Test
    public void givenATaxFreeChildcareTypeAndDwpOffice_thenCorrectDwpRegionalCenter() {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("taxFreeChildcare", null);

        assertEquals("C.B.O (HMRC)", result);
    }

    @Test
    public void givenATaxFreeChildcareType_thenReturnTheOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("taxFreeChildcare", null);

        assertTrue(result.isPresent());
        assertEquals("Childcare Service HMRC", result.get().getCode());
    }

    @Test
    public void givenAHomeResponsibilitiesProtectionTypeAndDwpOffice_thenCorrectDwpRegionalCenter() {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("homeResponsibilitiesProtection", null);

        assertEquals("TCO Preston Appeals Team", result);
    }

    @Test
    public void givenAHomeResponsibilitiesProtectionType_thenReturnTheOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("homeResponsibilitiesProtection", null);

        assertTrue(result.isPresent());
        assertEquals("PT Operations North East England", result.get().getCode());
    }

    @Test
    public void givenAChildBenefitTypeAndDwpOffice_thenCorrectDwpRegionalCenter() {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("childBenefit", null);

        assertEquals("C.B.O (HMRC)", result);
    }

    @Test
    public void givenAChildBenefitType_thenReturnTheOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("childBenefit", null);

        assertTrue(result.isPresent());
        assertEquals("Child Benefit Office", result.get().getCode());
    }

    @Test
    public void givenAThirtyHoursFreeChildcareAndDwpOffice_thenCorrectDwpRegionalCenter() {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("thirtyHoursFreeChildcare", null);

        assertEquals("C.B.O (HMRC)", result);
    }

    @Test
    public void givenAThirtyHoursFreeChildcareType_thenReturnTheOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("thirtyHoursFreeChildcare", null);

        assertTrue(result.isPresent());
        assertEquals("Childcare Service HMRC", result.get().getCode());
    }

    @Test
    public void givenAGuaranteedMinimumPensionTypeAndDwpOffice_thenCorrectDwpRegionalCenter() {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("guaranteedMinimumPension", null);

        assertEquals("TCO Preston Appeals Team", result);
    }

    @Test
    public void givenAGuaranteedMinimumPensionType_thenReturnTheOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("guaranteedMinimumPension", null);

        assertTrue(result.isPresent());
        assertEquals("PT Operations North East England", result.get().getCode());
    }

    @Test
    public void givenANationalInsuranceCreditsTypeAndDwpOffice_thenCorrectDwpRegionalCenter() {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("nationalInsuranceCredits", null);

        assertEquals("TCO Preston Appeals Team", result);
    }

    @Test
    public void givenANationalInsuranceCreditsType_thenReturnTheOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("nationalInsuranceCredits", null);

        assertTrue(result.isPresent());
        assertEquals("PT Operations North East England", result.get().getCode());
    }

    @ParameterizedTest
    @CsvSource({
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
    public void givenABereavementSupportPaymentScheme_thenReturnTheOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDwpMappingByOffice("bereavementSupportPaymentScheme", null);

        assertTrue(result.isPresent());
        assertEquals("Pensions Dispute Resolution Team", result.get().getCode());
    }

    @ParameterizedTest
    @CsvSource({
        "Barrow IIDB Centre, IIDB Barrow", "Barnsley Benefit Centre, IIDB Barnsley"
    })
    public void givenAIidbBenefitType_thenReturnTheCorrectDwpRegionalCentre(String office, String dwpRegionalCentre) {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("industrialInjuriesDisablement", office);

        assertEquals(dwpRegionalCentre, result);
    }

    @ParameterizedTest
    @CsvSource({
        "Barrow IIDB Centre, IDB Barrow", "Barnsley Benefit Centre, IDB Barnsley"
    })
    public void givenAIndustrialDeathBenefitType_thenReturnTheCorrectDwpRegionalCentre(String office, String dwpRegionalCentre) {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("industrialDeathBenefit", office);

        assertEquals(dwpRegionalCentre, result);
    }

    @ParameterizedTest
    @CsvSource({
        "Pensions Dispute Resolution Team, Pension Credit", "Recovery from Estates, RfE"
    })
    public void givenAPensionCreditsBenefitType_thenReturnTheCorrectDwpRegionalCentre(String office, String dwpRegionalCentre) {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("pensionCredit", office);

        assertEquals(dwpRegionalCentre, result);
    }

    @ParameterizedTest
    @CsvSource({
        "Pensions Dispute Resolution Team, Retirement Pension", "Recovery from Estates, RfE"
    })
    public void givenARetirementPensionBenefitType_thenReturnTheCorrectDwpRegionalCentre(String office, String dwpRegionalCentre) {
        String result = dwpAddressLookup.getDwpRegionalCenterByBenefitTypeAndOffice("retirementPension", office);

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
        assertEquals("3", result.get().getCode());
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
    public void givenAMaternityAllowanceBenefitType_thenReturnTheDefaultBereavementSupportPaymentSchemeOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDefaultDwpMappingByBenefitType("bereavementSupportPaymentScheme");

        assertTrue(result.isPresent());
        assertEquals("Pensions Dispute Resolution Team", result.get().getCode());
    }

    @Test
    public void givenAMaternityAllowanceBenefitType_thenReturnTheDefaultIndustrialDeathBenefitOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDefaultDwpMappingByBenefitType("industrialDeathBenefit");

        assertTrue(result.isPresent());
        assertEquals("Barrow IIDB Centre", result.get().getCode());
    }

    @Test
    public void givenAMaternityAllowanceBenefitType_thenReturnTheDefaultPensionCreditsOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDefaultDwpMappingByBenefitType("pensionCredit");

        assertTrue(result.isPresent());
        assertEquals("Pensions Dispute Resolution Team", result.get().getCode());
    }

    @Test
    public void givenAMaternityAllowanceBenefitType_thenReturnTheDefaultRetirementPensionOffice() {
        Optional<OfficeMapping> result = dwpAddressLookup.getDefaultDwpMappingByBenefitType("retirementPension");

        assertTrue(result.isPresent());
        assertEquals("Pensions Dispute Resolution Team", result.get().getCode());
    }

    @Test
    public void allDwpMappingsHaveADwpRegionCentre() {
        stream(Benefit.values())
                .flatMap(benefit -> stream(benefit.getOfficeMappings().apply(dwpAddressLookup)))
                .forEach(f -> assertNotNull(f.getMapping().getDwpRegionCentre()));
    }

    @Test
    public void isValidJsonWithNoDuplicateValues() throws Exception {
        String json = resourceToString("reference-data/ftaAddresses.json",
                StandardCharsets.UTF_8, Thread.currentThread().getContextClassLoader());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
        final JsonNode tree = mapper.readTree(json);
        assertThat(tree, is(notNullValue()));
    }

    @ParameterizedTest
    @CsvSource({"PIP,1,true", "ESA,,false", "carersAllowance,Invalid,false", "carersAllowance,Carer’s Allowance Dispute Resolution Team,true"})
    public void validateIssuingOffice(String benefitType, String issuingOffice, boolean isValid) {
        boolean result = dwpAddressLookup.validateIssuingOffice(benefitType, issuingOffice);
        assertEquals(isValid, result);
    }
}
