package uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata;

import static java.util.Collections.emptyMap;
import static junit.framework.TestCase.assertFalse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.MockitoAnnotations.openMocks;
import static uk.gov.hmcts.reform.sscs.ccd.callback.ValidationType.EXCEPTION_RECORD;
import static uk.gov.hmcts.reform.sscs.ccd.callback.ValidationType.SYA_APPEAL;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.*;
import static uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata.AppealValidator.ERRORS;
import static uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata.AppealValidator.WARNINGS;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.BENEFIT_TYPE_DESCRIPTION;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.HAS_REPRESENTATIVE_FIELD_MISSING;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.HEARING_TYPE_FACE_TO_FACE_LITERAL;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.HEARING_TYPE_ORAL;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.HEARING_TYPE_PAPER;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.HEARING_TYPE_TELEPHONE_LITERAL;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.HEARING_TYPE_VIDEO_LITERAL;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.converters.Nullable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appeal;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appellant;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appointee;
import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitType;
import uk.gov.hmcts.reform.sscs.ccd.domain.CcdValue;
import uk.gov.hmcts.reform.sscs.ccd.domain.Contact;
import uk.gov.hmcts.reform.sscs.ccd.domain.DateRange;
import uk.gov.hmcts.reform.sscs.ccd.domain.DocumentLink;
import uk.gov.hmcts.reform.sscs.ccd.domain.ExcludeDate;
import uk.gov.hmcts.reform.sscs.ccd.domain.FormType;
import uk.gov.hmcts.reform.sscs.ccd.domain.HearingOptions;
import uk.gov.hmcts.reform.sscs.ccd.domain.HearingSubtype;
import uk.gov.hmcts.reform.sscs.ccd.domain.Identity;
import uk.gov.hmcts.reform.sscs.ccd.domain.MrnDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.Name;
import uk.gov.hmcts.reform.sscs.ccd.domain.OtherParty;
import uk.gov.hmcts.reform.sscs.ccd.domain.RegionalProcessingCenter;
import uk.gov.hmcts.reform.sscs.ccd.domain.Representative;
import uk.gov.hmcts.reform.sscs.ccd.domain.Role;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsDocument;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsDocumentDetails;
import uk.gov.hmcts.reform.sscs.ccd.validation.address.PostcodeValidator;
import uk.gov.hmcts.reform.sscs.service.DwpAddressLookupService;
import uk.gov.hmcts.reform.sscs.service.RegionalProcessingCenterService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(JUnitParamsRunner.class)
public class AppealValidatorTest {

    private static final String CHILD_MAINTENANCE_NUMBER = "Test1234";
    private static final String OTHER_PARTY_ADDRESS_LINE1 = "299 Harrow";
    private static final String OTHER_PARTY_ADDRESS_LINE2 = "The Avenue";
    private static final String OTHER_PARTY_ADDRESS_LINE3 = "Hatch End";
    private static final String OTHER_PARTY_POSTCODE = "HA5 4QT";

    private static final String VALID_MOBILE = "07832882849";
    private static final String VALID_POSTCODE = "CM13 0GD";
    private final List<String> titles = List.of("Mr", "Mrs");
    private final Map<String, Object> ocrCaseData = new HashMap<>();
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    DwpAddressLookupService dwpAddressLookupService;
    @Mock
    RegionalProcessingCenterService regionalProcessingCenterService;
    @Mock
    private PostcodeValidator postcodeValidator;
    private AddressValidator addressValidator;
    private AppealValidator validator;
    private MrnDetails defaultMrnDetails;

    @Before
    public void setup() {
        openMocks(this);
        addressValidator = new AddressValidator(regionalProcessingCenterService, postcodeValidator);
        dwpAddressLookupService = new DwpAddressLookupService();

        defaultMrnDetails = MrnDetails.builder().dwpIssuingOffice("test-hmcts-address").mrnDate("2018-12-09").build();

        ocrCaseData.put("person1_address_line4", "county");
        ocrCaseData.put("person2_address_line4", "county");
        ocrCaseData.put("representative_address_line4", "county");
        ocrCaseData.put("office", "2");

        given(postcodeValidator.isValid(anyString(), isNull())).willReturn(true);
        given(postcodeValidator.isValidPostcodeFormat(anyString())).willReturn(true);
        given(regionalProcessingCenterService.getByPostcode(VALID_POSTCODE))
                .willReturn(RegionalProcessingCenter.builder().address1("Address 1").name("Liverpool").build());

        validator = new AppealValidator(dwpAddressLookupService, addressValidator, EXCEPTION_RECORD, titles);
    }

    @Test
    @Parameters({"ESA", "JSA", "PIP", "DLA", "attendanceAllowance", "industrialInjuriesDisablement",
            "socialFund", "incomeSupport", "industrialDeathBenefit", "pensionCredit", "retirementPension"})
    public void givenAnAppealContainsAnInvalidOfficeForBenefitTypeOtherNotAutoOffice_thenAddAWarning(
            String benefitShortName) {
        defaultMrnDetails.setDwpIssuingOffice("Invalid Test Office");

        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                buildMinimumAppealDataWithBenefitTypeAndFormType(benefitShortName, buildAppellant(false), true,
                        FormType.SSCS1U),
                false, false, true);

        assertEquals("office is invalid", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    @Parameters({"carersAllowance", "bereavementBenefit", "maternityAllowance", "bereavementSupportPaymentScheme"})
    public void givenAnAppealContainsAnInvalidOfficeForBenefitTypeOtherAutoOffice_thenDoNotAddAWarning(
            String benefitShortName) {
        defaultMrnDetails.setDwpIssuingOffice("Invalid Test Office");

        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                buildMinimumAppealDataWithBenefitTypeAndFormType(benefitShortName, buildAppellant(false), true,
                        FormType.SSCS1U),
                false, false, true);

        assertEquals(0, errsWarns.get(ERRORS).size());
        assertEquals(0, errsWarns.get(WARNINGS).size());
    }

    @Test
    public void givenAnAppealContainsAnInvalidOfficeForBenefitTypeUC_ucOfficeFeatureActive_thenAddAWarning() {
        defaultMrnDetails.setDwpIssuingOffice("Invalid Test Office");

        var errsWarns= validator.validateAppeal(
                ocrCaseData,
                buildMinimumAppealDataWithBenefitTypeAndFormType(UC.getShortName(), buildAppellant(false), true,
                        FormType.SSCS1U),
                false, false, true);

        assertEquals("office is invalid", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    @Parameters({"The Pension Service 11", "Recovery from Estates"})
    public void givenAnAppealContainsAValidOfficeForBenefitTypeAttendanceAllowance_thenDoNotAddAWarning(
            String dwpIssuingOffice) {
        defaultMrnDetails.setDwpIssuingOffice(dwpIssuingOffice);

        var errsWarns = validator.validateAppeal(
                        ocrCaseData,
                        buildMinimumAppealDataWithBenefitTypeAndFormType(ATTENDANCE_ALLOWANCE.getShortName(),
                                buildAppellant(false),
                                true, FormType.SSCS1U),
                        false, false, true);

        String assertionMessage = "Asserting Benefit: Attendance Allowance with Office: " + dwpIssuingOffice;
        assertEquals(0, errsWarns.get(WARNINGS).size());
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    @Parameters({"Disability Benefit Centre 4", "The Pension Service 11", "Recovery from Estates"})
    public void givenAnAppealContainsAValidOfficeForBenefitTypeDla_thenDoNotAddAWarning(String dwpIssuingOffice) {
        defaultMrnDetails.setDwpIssuingOffice(dwpIssuingOffice);

        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                buildMinimumAppealDataWithBenefitTypeAndFormType(DLA.getShortName(), buildAppellant(false), true,
                        FormType.SSCS1U),
                false, false, true);

        String assertionMessage = "Asserting Benefit: DLA with Office: " + dwpIssuingOffice;
        assertEquals(0, errsWarns.get(WARNINGS).size());
        assertEquals(0, errsWarns.get(ERRORS).size(), assertionMessage);
    }

    @Test
    @Parameters({"Worthing DRT", "Birkenhead DRT", "Recovery from Estates", "Inverness DRT"})
    public void givenAnAppealContainsAValidOfficeForBenefitTypeIncomeSupport_thenDoNotAddAWarning(
            String dwpIssuingOffice) {
        defaultMrnDetails.setDwpIssuingOffice(dwpIssuingOffice);

        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                buildMinimumAppealDataWithBenefitTypeAndFormType(INCOME_SUPPORT.getShortName(), buildAppellant(false), true,
                        FormType.SSCS1U),
                false, false, true);

        String assertionMessage = "Asserting Benefit: Income Support with Office: " + dwpIssuingOffice;
        assertEquals(0, errsWarns.get(WARNINGS).size(), assertionMessage);
        assertEquals(0, errsWarns.get(ERRORS).size(), assertionMessage);
    }

    @Test
    @Parameters({"Barrow IIDB Centre", "Barnsley Benefit Centre"})
    public void givenAnAppealContainsAValidOfficeForBenefitTypeIidb_thenDoNotAddAWarning(String dwpIssuingOffice) {
        defaultMrnDetails.setDwpIssuingOffice(dwpIssuingOffice);

        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                buildMinimumAppealDataWithBenefitTypeAndFormType(IIDB.getShortName(), buildAppellant(false), true,
                        FormType.SSCS1U),
                false, false, true);

        String assertionMessage = "Asserting Benefit: IIDB with Office: " + dwpIssuingOffice;
        assertEquals(0, errsWarns.get(WARNINGS).size(), assertionMessage);
        assertEquals(0, errsWarns.get(ERRORS).size(), assertionMessage);
    }

    @Test
    @Parameters({"Worthing DRT", "Birkenhead DRT", "Recovery from Estates", "Inverness DRT"})
    public void givenAnAppealContainsAValidOfficeForBenefitTypeJsa_thenDoNotAddAWarning(String dwpIssuingOffice) {
        defaultMrnDetails.setDwpIssuingOffice(dwpIssuingOffice);

        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                buildMinimumAppealDataWithBenefitTypeAndFormType(JSA.getShortName(), buildAppellant(false), true,
                        FormType.SSCS1U),
                false, false, true);

        String assertionMessage = "Asserting Benefit: JSA with Office: " + dwpIssuingOffice;
        assertEquals(0, errsWarns.get(WARNINGS).size(), assertionMessage);
        assertEquals(0, errsWarns.get(ERRORS).size(), assertionMessage);
    }

    @Test
    @Parameters({"St Helens Sure Start Maternity Grant", "Funeral Payment Dispute Resolution Team",
            "Pensions Dispute Resolution Team"})
    public void givenAnAppealContainsAValidOfficeForBenefitTypeSocialFund_thenDoNotAddAWarning(
            String dwpIssuingOffice) {
        defaultMrnDetails.setDwpIssuingOffice(dwpIssuingOffice);

        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                buildMinimumAppealDataWithBenefitTypeAndFormType(SOCIAL_FUND.getShortName(), buildAppellant(false), true,
                        FormType.SSCS1U),
                false, false, true);

        String assertionMessage = "Asserting Benefit: Social Fund with Office: " + dwpIssuingOffice;
        assertEquals(0, errsWarns.get(WARNINGS).size(), assertionMessage);
        assertEquals(0, errsWarns.get(ERRORS).size(), assertionMessage);
    }

    @Test
    @Parameters({"Barrow IIDB Centre", "Barnsley Benefit Centre"})
    public void givenAnAppealContainsAValidOfficeForBenefitTypeIndustrialDeathBenefit_thenDoNotAddAWarning(
            String dwpIssuingOffice) {
        defaultMrnDetails.setDwpIssuingOffice(dwpIssuingOffice);

        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                buildMinimumAppealDataWithBenefitTypeAndFormType(INDUSTRIAL_DEATH_BENEFIT.getShortName(),
                        buildAppellant(false), true, FormType.SSCS1U),
                false, false, true);

        String assertionMessage = "Asserting Benefit: Industrial Death Benefit with Office: " + dwpIssuingOffice;
        assertEquals(0, errsWarns.get(WARNINGS).size(), assertionMessage);
        assertEquals(0, errsWarns.get(ERRORS).size(), assertionMessage);
    }

    @Test
    @Parameters({"Pensions Dispute Resolution Team", "Recovery from Estates"})
    public void givenAnAppealContainsAValidOfficeForBenefitTypePensionCredit_thenDoNotAddAWarning(
            String dwpIssuingOffice) {
        defaultMrnDetails.setDwpIssuingOffice(dwpIssuingOffice);

        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                buildMinimumAppealDataWithBenefitTypeAndFormType(PENSION_CREDIT.getShortName(), buildAppellant(false), true,
                        FormType.SSCS1U),
                false, false, true);

        String assertionMessage = "Asserting Benefit: Pension Credit with Office: " + dwpIssuingOffice;
        assertEquals(0, errsWarns.get(WARNINGS).size(), assertionMessage);
        assertEquals(0, errsWarns.get(ERRORS).size(), assertionMessage);
    }

    @Test
    @Parameters({"Pensions Dispute Resolution Team", "Recovery from Estates"})
    public void givenAnAppealContainsAValidOfficeForBenefitTypeRetirementPension_thenDoNotAddAWarning(
            String dwpIssuingOffice) {
        defaultMrnDetails.setDwpIssuingOffice(dwpIssuingOffice);

        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                buildMinimumAppealDataWithBenefitTypeAndFormType(RETIREMENT_PENSION.getShortName(), buildAppellant(false),
                        true, FormType.SSCS1U),
                false, false, true);

        String assertionMessage = "Asserting Benefit: Retirement Pension with Office: " + dwpIssuingOffice;
        assertEquals(0, errsWarns.get(WARNINGS).size(), assertionMessage);
        assertEquals(0, errsWarns.get(ERRORS).size(), assertionMessage);
    }

    @Test
    @Parameters({"Pensions Dispute Resolution Team"})
    public void givenAnAppealContainsAValidOfficeForBenefitTypeBereavementBenefit_thenDoNotAddAWarning(
            String dwpIssuingOffice) {
        defaultMrnDetails.setDwpIssuingOffice(dwpIssuingOffice);

        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                buildMinimumAppealDataWithBenefitTypeAndFormType(BEREAVEMENT_BENEFIT.getShortName(), buildAppellant(false),
                        true, FormType.SSCS1U),
                false, false, true);

        String assertionMessage = "Asserting Benefit: Bereavement Benefit with Office: " + dwpIssuingOffice;
        assertEquals(0, errsWarns.get(WARNINGS).size(), assertionMessage);
        assertEquals(0, errsWarns.get(ERRORS).size(), assertionMessage);
    }

    @Test
    @Parameters({"Carer’s Allowance Dispute Resolution Team"})
    public void givenAnAppealContainsAValidOfficeForBenefitTypeCarersAllowance_thenDoNotAddAWarning(
            String dwpIssuingOffice) {
        defaultMrnDetails.setDwpIssuingOffice(dwpIssuingOffice);

        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                buildMinimumAppealDataWithBenefitTypeAndFormType(CARERS_ALLOWANCE.getShortName(), buildAppellant(false),
                        true, FormType.SSCS1U),
                false, false, true);

        String assertionMessage = "Asserting Benefit: Carers Allowance with Office: " + dwpIssuingOffice;
        assertEquals(0, errsWarns.get(WARNINGS).size(), assertionMessage);
        assertEquals(0, errsWarns.get(ERRORS).size(), assertionMessage);
    }

    @Test
    @Parameters({"Walsall Benefit Centre"})
    public void givenAnAppealContainsAValidOfficeForBenefitTypeMaternityAllowance_thenDoNotAddAWarning(
            String dwpIssuingOffice) {
        defaultMrnDetails.setDwpIssuingOffice(dwpIssuingOffice);

        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                buildMinimumAppealDataWithBenefitTypeAndFormType(MATERNITY_ALLOWANCE.getShortName(), buildAppellant(false),
                        true, FormType.SSCS1U),
                false, false, true);

        String assertionMessage = "Asserting Benefit: Maternity Allowance with Office: " + dwpIssuingOffice;
        assertEquals(0, errsWarns.get(WARNINGS).size(), assertionMessage);
        assertEquals(0, errsWarns.get(ERRORS).size(), assertionMessage);
    }

    @Test
    @Parameters({"Pensions Dispute Resolution Team"})
    public void givenAnAppealContainsAValidOfficeForBenefitTypeBsps_thenDoNotAddAWarning(String dwpIssuingOffice) {
        defaultMrnDetails.setDwpIssuingOffice(dwpIssuingOffice);

        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                buildMinimumAppealDataWithBenefitTypeAndFormType(BEREAVEMENT_SUPPORT_PAYMENT_SCHEME.getShortName(),
                        buildAppellant(false),
                        true, FormType.SSCS1U),
                false, false, true);

        String assertionMessage =
                "Asserting Benefit: Bereavement Support Payment Scheme with Office: " + dwpIssuingOffice;
        assertEquals(0, errsWarns.get(WARNINGS).size(), assertionMessage);
        assertEquals(0, errsWarns.get(ERRORS).size(), assertionMessage);
    }

    @Test
    public void givenAnAppellantIsEmpty_thenAddAWarning() {
        Map<String, Object> ocrCaseDataEmptyOffice = new HashMap<>();
        ocrCaseDataEmptyOffice.put("person1_address_line4", "county");
        ocrCaseDataEmptyOffice.put("person2_address_line4", "county");
        ocrCaseDataEmptyOffice.put("representative_address_line4", "county");
        Map<String, Object> pairs = new HashMap<>();
        pairs.put("appeal", Appeal.builder().hearingType(HEARING_TYPE_ORAL).build());
        pairs.put("bulkScanCaseReference", 123);
        pairs.put("formType", FormType.SSCS1);

        var errsWarns = validator.validateAppeal(ocrCaseDataEmptyOffice, pairs, false, false, true);

        assertThat(errsWarns.get(WARNINGS))
                .containsOnly(
                        "person1_title is empty",
                        "person1_first_name is empty",
                        "person1_last_name is empty",
                        "person1_address_line1 is empty",
                        "person1_address_line3 is empty",
                        "person1_address_line4 is empty",
                        "person1_postcode is empty",
                        "person1_nino is empty",
                        "mrn_date is empty",
                        "office is empty",
                        "benefit_type_description is empty");
    }

    @Test
    public void givenAnAppellantWithNoName_thenAddWarnings() {
        Map<String, Object> pairs = new HashMap<>();

        pairs.put("appeal", Appeal.builder().appellant(Appellant.builder().address(
                                Address.builder().line1("123 The Road").town("Harlow").county("Essex").postcode(VALID_POSTCODE).build())
                        .identity(Identity.builder().nino("BB000000B").build()).build())
                .benefitType(BenefitType.builder().code(PIP.name()).build())
                .mrnDetails(defaultMrnDetails)
                .hearingType(HEARING_TYPE_ORAL).build());
        pairs.put("bulkScanCaseReference", 123);

        var errsWarns = validator.validateAppeal( ocrCaseData, pairs, false, false, true);

        assertThat(errsWarns.get(WARNINGS))
                .containsOnly("person1_title is empty",
                        "person1_first_name is empty",
                        "person1_last_name is empty");
    }

    @Test
    public void givenAnAppellantWithHearingTypeOralAndNoHearingSubType_thenAddWarnings() {
        Map<String, Object> pairs = new HashMap<>();

        pairs.put("appeal", Appeal.builder()
                .appellant(Appellant.builder().name(Name.builder().firstName("Harry").lastName("Kane").build())
                        .address(Address.builder().line1("123 The Road").town("Harlow").county("Essex").postcode(VALID_POSTCODE)
                                .build())
                        .identity(Identity.builder().nino("BB000000B").build()).build())
                .benefitType(BenefitType.builder().code(PIP.name()).build())
                .mrnDetails(defaultMrnDetails)
                .hearingType(HEARING_TYPE_ORAL).build());
        pairs.put("bulkScanCaseReference", 123);
        pairs.put("formType", FormType.SSCS1PEU);

        ocrCaseData.put(HEARING_TYPE_TELEPHONE_LITERAL, "");
        ocrCaseData.put(HEARING_TYPE_VIDEO_LITERAL, "");
        ocrCaseData.put(HEARING_TYPE_FACE_TO_FACE_LITERAL, "");

        var errsWarns = validator.validateAppeal( ocrCaseData, pairs, false, false, true);

        assertThat(errsWarns.get(WARNINGS))
                .containsOnly("person1_title is empty",
                        "hearing_type_telephone, hearing_type_video and hearing_type_face_to_face are empty. At least one must be populated");
    }

    @Test
    public void givenAnAppellantWithNoNameAndEmptyAppointeeDetails_thenAddWarnings() {
        Map<String, Object> pairs = new HashMap<>();

        Appointee appointee = Appointee.builder()
                .address(Address.builder().build())
                .name(Name.builder().build())
                .contact(Contact.builder().build())
                .identity(Identity.builder().build())
                .build();

        pairs.put("appeal", Appeal.builder().appellant(Appellant.builder()
                        .appointee(appointee)
                        .address(
                                Address.builder().line1("123 The Road").town("Harlow").county("Essex").postcode(VALID_POSTCODE).build())
                        .identity(Identity.builder().nino("BB000000B").build()).build())
                .benefitType(BenefitType.builder().code(PIP.name()).build())
                .mrnDetails(defaultMrnDetails)
                .hearingType(HEARING_TYPE_ORAL).build());
        pairs.put("bulkScanCaseReference", 123);

        var errsWarns = validator.validateAppeal( ocrCaseData, pairs, false, false, true);

        assertThat(errsWarns.get(WARNINGS))
                .containsOnly(
                        "person1_title is empty",
                        "person1_first_name is empty",
                        "person1_last_name is empty");
    }

    @Test
    public void givenAnAppellantWithNoAddress_thenAddWarnings() {
        Map<String, Object> pairs = new HashMap<>();

        pairs.put("appeal", Appeal.builder().appellant(Appellant.builder().name(
                                Name.builder().firstName("Harry").lastName("Kane").build())
                        .identity(Identity.builder().nino("BB000000B").build()).build())
                .benefitType(BenefitType.builder().code(PIP.name()).build())
                .mrnDetails(defaultMrnDetails)
                .hearingType(HEARING_TYPE_ORAL)
                .hearingSubtype(HearingSubtype.builder().wantsHearingTypeFaceToFace("Yes").build()).build());
        pairs.put("bulkScanCaseReference", 123);

        var errsWarns = validator.validateAppeal(ocrCaseData, pairs, false, false, true);

        assertThat(errsWarns.get(WARNINGS))
                .containsOnly(
                        "person1_title is empty",
                        "person1_address_line1 is empty",
                        "person1_address_line3 is empty",
                        "person1_address_line4 is empty",
                        "person1_postcode is empty");
    }

    @Test
    public void givenAnAppellantDoesNotContainATitle_thenAddAWarning() {
        Appellant appellant = buildAppellant(false);
        appellant.getName().setTitle(null);

        var errsWarns =
                validator.validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_title is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppellantDoesNotContainAValidTitle_thenAddAWarning() {
        Appellant appellant = buildAppellant(false);
        appellant.getName().setTitle("Bla");

        var errsWarns = validator.validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_title is invalid", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppellantDoesContainAValidTitleWithFullStop_thenDoNotAddAWarning() {
        Appellant appellant = buildAppellant(false);
        appellant.getName().setTitle("Mr.");

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals(0, errsWarns.get(WARNINGS).size());
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenAnAppellantDoesContainAValidTitleLowercase_thenDoNotAddAWarning() {
        Appellant appellant = buildAppellant(false);
        appellant.getName().setTitle("mr");

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals(0, errsWarns.get(WARNINGS).size());
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenAnAppellantDoesContainAValidTitle_thenDoNotAddAWarning() {
        Appellant appellant = buildAppellant(false);
        appellant.getName().setTitle("Mr");

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals(0, errsWarns.get(WARNINGS).size());
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenAnAppellantDoesNotContainAFirstName_thenAddAWarning() {
        Appellant appellant = buildAppellant(false);
        appellant.getName().setFirstName(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_first_name is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppellantDoesNotContainALastName_thenAddAWarning() {
        Appellant appellant = buildAppellant(false);
        appellant.getName().setLastName(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_last_name is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppellantDoesNotContainAnAddressLine1_thenAddAWarning() {
        Appellant appellant = buildAppellant(false);
        appellant.getAddress().setLine1(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_address_line1 is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppellantDoesNotContainValidAddressLine1_thenAddAWarning() {
        Appellant appellant = buildAppellant(false);
        appellant.getAddress().setLine1("[my house");

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_address_line1 has invalid characters at the beginning", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppellantDoesNotContainATownAndContainALine2_thenAddAWarning() {
        Appellant appellant = buildAppellant(false);
        appellant.getAddress().setLine2("101 Street");
        appellant.getAddress().setTown(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_address_line3 is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppellantDoesNotContainAValidTownAndContainALine2_thenAddAWarning() {
        Appellant appellant = buildAppellant(false);
        appellant.getAddress().setLine2("101 Street");
        appellant.getAddress().setTown("@invalid");

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_address_line3 has invalid characters at the beginning", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppellantDoesNotContainATownAndALine2_thenAddAWarning() {
        Appellant appellant = buildAppellant(false);
        ocrCaseData.remove("person1_address_line4");
        ocrCaseData.remove("person2_address_line4");
        appellant.getAddress().setLine2(null);
        appellant.getAddress().setTown(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_address_line2 is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppellantDoesNotContainAValidTownAndALine2_thenAddAWarning() {
        Appellant appellant = buildAppellant(false);
        ocrCaseData.remove("person1_address_line4");
        ocrCaseData.remove("person2_address_line4");
        appellant.getAddress().setLine2(null);
        appellant.getAddress().setTown("@invalid");

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_address_line2 has invalid characters at the beginning", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppellantDoesNotContainACountyAndContainALine2_thenAddAWarning() {
        Appellant appellant = buildAppellant(false);
        appellant.getAddress().setLine2("101 Street");
        appellant.getAddress().setCounty(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_address_line4 is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppellantDoesNotContainAValidCountyAndContainALine2_thenAddAWarning() {
        Appellant appellant = buildAppellant(false);
        appellant.getAddress().setLine2("101 Street");
        appellant.getAddress().setCounty("(Bad County");

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_address_line4 has invalid characters at the beginning", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppellantDoesNotContainACountyAndALine2_thenAddAWarning() {
        ocrCaseData.remove("person1_address_line4");
        ocrCaseData.remove("person2_address_line4");
        Appellant appellant = buildAppellant(false);
        appellant.getAddress().setLine2(null);
        appellant.getAddress().setCounty(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_address_line3 is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppellantDoesNotContainAValidCountyAndALine2_thenAddAWarning() {
        ocrCaseData.remove("person1_address_line4");
        ocrCaseData.remove("person2_address_line4");
        Appellant appellant = buildAppellant(false);
        appellant.getAddress().setLine2(null);
        appellant.getAddress().setCounty("£bad County");

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_address_line3 has invalid characters at the beginning", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppellantContainsAPlaceholderCountyAndALine2_thenNoAWarning() {
        ocrCaseData.remove("person1_address_line4");
        ocrCaseData.remove("person2_address_line4");
        Appellant appellant = buildAppellant(false);
        appellant.getAddress().setLine2(null);
        appellant.getAddress().setCounty(".");

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals(0, errsWarns.get(WARNINGS).size());
    }

    @Test
    public void givenAnAppellantDoesNotContainAPostcode_thenAddAWarningAndDoNotAddRegionalProcessingCenter() {
        Appellant appellant = buildAppellant(false);
        appellant.getAddress().setPostcode(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_postcode is empty", errsWarns.get(WARNINGS).get(0));
        verifyNoInteractions(regionalProcessingCenterService);
    }

    @Test
    public void givenAnAppellantContainsPostcodeWithNoRegionalProcessingCenter_thenDoNotAddRegionalProcessingCenter() {
        Appellant appellant = buildAppellant(false);
        given(regionalProcessingCenterService.getByPostcode(VALID_POSTCODE)).willReturn(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_postcode is not a postcode that maps to a regional processing center",
                errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppointee_thenRegionalProcessingCenterIsAlwaysFromTheAppellantsPostcode() {
        Appellant appellant = buildAppellant(true);
        RegionalProcessingCenter rpc = RegionalProcessingCenter.builder().name("person2_postcode").build();
        given(regionalProcessingCenterService.getByPostcode(appellant.getAddress().getPostcode())).willReturn(rpc);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        Assert.assertEquals(0, errsWarns.get(WARNINGS).size());
    }

    @Test
    public void givenAnAppellantDoesNotContainANino_thenAddAWarning() {
        Appellant appellant = buildAppellant(false);
        appellant.getIdentity().setNino(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_nino is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppellantDoesNotContainAValidNino_thenAddAWarning() {
        Appellant appellant = buildAppellant(false);
        appellant.getIdentity().setNino("Bla");

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_nino is invalid", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppellantDoesContainANino_thenDoNotAddAWarning() {
        Appellant appellant = buildAppellant(false);
        appellant.getIdentity().setNino("BB000000B");

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals(0, errsWarns.get(WARNINGS).size());
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenAnAppointeeExistsAndAnAppellantDoesNotContainANino_thenAddAWarningAboutPerson2() {
        Appellant appellant = buildAppellant(true);
        appellant.getIdentity().setNino(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person2_nino is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppointeeWithEmptyDetailsAndAnAppellantDoesNotContainANino_thenAddAWarningAboutPerson1() {
        Appellant appellant = buildAppellant(true);
        appellant.getIdentity().setNino(null);
        appellant.getAppointee().setName(null);
        appellant.getAppointee().setAddress(null);
        appellant.getAppointee().setContact(null);
        appellant.getAppointee().setIdentity(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_nino is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppealDoesNotContainAnMrnDate_thenAddAWarning() {
        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithMrn(MrnDetails.builder().dwpIssuingOffice("2").build(), buildAppellant(false),
                        true, FormType.SSCS1PE), false, false, true);

        assertEquals("mrn_date is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppealContainsAnMrnDateInFuture_thenAddAWarning() {
        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithMrn(MrnDetails.builder().dwpIssuingOffice("2").mrnDate("2148-10-10").build(),
                        buildAppellant(false), true, FormType.SSCS1PE), false, false, true);

        assertEquals("mrn_date is in future", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnMrnDoesNotContainADwpIssuingOfficeAndOcrDataIsEmpty_thenAddAWarning() {
        Map<String, Object> ocrCaseDataInvalidOffice = new HashMap<>();
        var errsWarns = validator.validateAppeal(ocrCaseDataInvalidOffice,
                buildMinimumAppealDataWithMrn(MrnDetails.builder().mrnDate("2019-01-01").dwpIssuingOffice(null).build(),
                        buildAppellant(false), true, FormType.SSCS1PE), false, false, true);

        assertEquals("office is empty", errsWarns.get(WARNINGS).get(0));
        assertEquals(1, errsWarns.get(WARNINGS).size());
    }

    @Test
    @Parameters({"SSCS2", "SSCS5"})
    public void givenAnMrnDoesNotContainADwpIssuingOfficeAndFormTypeIsSscs2OrSscs5_thenDoNotAddAWarning(FormType formType) {
        Map<String, Object> ocrCaseDataInvalidOffice = new HashMap<>();

        var errsWarns = validator.validateAppeal(ocrCaseDataInvalidOffice,
                buildMinimumAppealDataWithMrn(MrnDetails.builder().mrnDate("2019-01-01").dwpIssuingOffice(null).build(),
                        buildAppellant(false), true, formType), false, false, true);

        assertEquals(0, errsWarns.get(WARNINGS).size());
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenAnMrnDoesNotContainAValidDwpIssuingOfficeAndOcrDataIsEmpty_thenAddAWarning() {
        Map<String, Object> ocrCaseDataInvalidOffice = new HashMap<>();
        var errsWarns = validator.validateAppeal(ocrCaseDataInvalidOffice,
                buildMinimumAppealDataWithMrn(MrnDetails.builder().mrnDate("2019-01-01").dwpIssuingOffice("Bla").build(),
                        buildAppellant(false), true, FormType.SSCS1PE), false, false, true);

        assertEquals("office is invalid", errsWarns.get(WARNINGS).get(0));
        assertEquals(1, errsWarns.get(WARNINGS).size());
    }

    @Test
    public void givenAnMrnDoesNotContainADwpIssuingOffice_thenAddAWarning() {
        var errsWarns = validator.validateAppeal(emptyMap(),
                buildMinimumAppealDataWithMrn(MrnDetails.builder().mrnDate("2019-01-01").dwpIssuingOffice(null).build(),
                        buildAppellant(false), true, FormType.SSCS1PE), false, false, true);

        assertEquals("office is empty", errsWarns.get(WARNINGS).get(0));
        assertEquals(1, errsWarns.get(WARNINGS).size());
    }

    @Test
    public void givenAnMrnDoesNotContainAValidDwpIssuingOffice_thenAddAWarning() {
        Map<String, Object> ocrCaseDataInvalidOffice = new HashMap<>();
        ocrCaseDataInvalidOffice.put("office", "Bla");

        var errsWarns = validator.validateAppeal(ocrCaseDataInvalidOffice,
                buildMinimumAppealDataWithMrn(MrnDetails.builder().mrnDate("2019-01-01").dwpIssuingOffice(null).build(),
                        buildAppellant(false), true, FormType.SSCS1PE), false, false, true);

        assertEquals("office is invalid", errsWarns.get(WARNINGS).get(0));
        assertEquals(1, errsWarns.get(WARNINGS).size());
    }

    @Test
    public void givenAnMrnDoesContainValidUpperCaseDwpIssuingOffice_thenNoWarning() {
        var errsWarns = validator.validateAppeal(emptyMap(),
                buildMinimumAppealDataWithMrn(
                        MrnDetails.builder().mrnDate("2019-01-01").dwpIssuingOffice("BALHAM DRT").build(),
                        buildAppellant(false), true, FormType.SSCS1PE), false, false, true);

        assertTrue(errsWarns.get(WARNINGS).isEmpty());
    }

    @Test
    public void givenAnMrnDoesContainValidCapitaliseDwpIssuingOffice_thenNoWarning() {
        var errsWarns = validator.validateAppeal(emptyMap(),
                buildMinimumAppealDataWithMrn(
                        MrnDetails.builder().mrnDate("2019-01-01").dwpIssuingOffice("Balham DRT").build(),
                        buildAppellant(false), true, FormType.SSCS1PE), false, false, true);

        assertTrue(errsWarns.get(WARNINGS).isEmpty());
    }

    @Test
    public void givenAnAppealContainsAnAppellantDateOfBirthInFuture_thenAddAWarning() {
        Appellant appellant = buildAppellant(false);
        appellant.getIdentity().setDob("2148-10-10");

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_dob is in future", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppealContainsAnAppointeeDateOfBirthInFuture_thenAddAWarning() {
        Appellant appellant = buildAppellant(true);
        appellant.getAppointee().getIdentity().setDob("2148-10-10");

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_dob is in future", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppealContainsAHearingExcludedDateInPast_thenAddAWarning() {
        Appellant appellant = buildAppellant(true);

        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithExcludedDate("2018-10-10", appellant, true, FormType.SSCS1PE), false, false, true);

        assertEquals("hearing_options_exclude_dates is in past", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppealDoesNotContainABenefitTypeDescription_thenAddAWarning() {
        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithBenefitType(null, buildAppellant(false), true, FormType.SSCS1PE), false, false, true);

        assertEquals(BENEFIT_TYPE_DESCRIPTION + " is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    @Parameters({"SSCS1U", "SSCS5"})
    public void givenAnAppealDoesNotContainABenefitTypeOtherForSscs1UOrSscs5Form_thenDoNotAddAWarning(FormType formType) {
        Map<String, Object> caseData = buildMinimumAppealDataWithBenefitType(null, buildAppellant(false), true, formType);
        caseData.put("formType", formType);
        var errsWarns =
                validator.validateAppeal( ocrCaseData, caseData, false, false, true);

        assertTrue(errsWarns.get(WARNINGS).isEmpty());
    }

    @Test
    public void givenAnAppealContainsAnInvalidBenefitTypeDescription_thenAddAnError() {
        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithBenefitType("Bla", buildAppellant(false), true, FormType.SSCS1PE), false, false, true);

        List<String> benefitNameList = new ArrayList<>();
        for (Benefit be : Benefit.values()) {
            benefitNameList.add(be.getShortName());
        }

        assertEquals(BENEFIT_TYPE_DESCRIPTION + " invalid. Should be one of: " + String.join(", ", benefitNameList), errsWarns.get(ERRORS).get(0));
    }

    @Test
    public void givenAnAppealContainsAValidLowercaseBenefitTypeDescription_thenDoNotAddAWarning() {
        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithBenefitType(PIP.name().toLowerCase(), buildAppellant(false), true, FormType.SSCS1PE), false, false, true);

        List<String> benefitNameList = new ArrayList<>();
        for (Benefit be : Benefit.values()) {
            benefitNameList.add(be.name());
        }

        assertEquals(0, errsWarns.get(WARNINGS).size());
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenAnAppealContainsAValidBenefitTypeDescription_thenDoNotAddAWarning() {
        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithBenefitType(PIP.name(), buildAppellant(false), true, FormType.SSCS1PE), false, false, true);

        assertEquals(0, errsWarns.get(WARNINGS).size());
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenAnAppealContainsAnInvalidAppellantMobileNumberLessThan10Digits_thenAddAnError() {
        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithBenefitType("Bla", buildAppellantWithMobileNumber("07776156"), true, FormType.SSCS1PE), false, false, true);

        assertEquals("person1_mobile is invalid", errsWarns.get(ERRORS).get(0));
    }

    @Test
    public void givenAnAppealContainsAnInvalidRepresentativeMobileNumberLessThan10Digits_thenAddAnError() {
        Representative representative = buildRepresentative();
        representative.setContact(Contact.builder().build());
        representative.getContact().setMobile("0123456");

        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithRepresentative(buildAppellant(false), representative, true, FormType.SSCS1PE), false, false, true);

        assertEquals("representative_mobile is invalid", errsWarns.get(ERRORS).get(0));
    }

    @Test
    public void givenAnAppealContainsValidAppellantAnInvalidAppointeeMobileNumberLessThan10Digits_thenAddAnError() {
        Appellant appellant = buildAppellant(true);
        appellant.getContact().setMobile(VALID_MOBILE);
        appellant.setAppointee(buildAppointeeWithMobileNumber("07776156"));
        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithBenefitType("Bla", appellant, true, FormType.SSCS1PE), false, false, true);

        assertEquals("person1_mobile is invalid", errsWarns.get(ERRORS).get(0));
    }

    @Test
    public void givenAnAppealContainsAnInValidAppellantAnInvalidAppointeeMobileNumberLessThan10Digits_thenAddAnError() {
        Appellant appellant = buildAppellant(true);
        appellant.getContact().setMobile("07776157");
        appellant.setAppointee(buildAppointeeWithMobileNumber("07776156"));
        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithBenefitType("Bla", appellant, true, FormType.SSCS1PE), false, false, true);

        assertEquals("person1_mobile is invalid", errsWarns.get(ERRORS).get(0));
        assertEquals("person2_mobile is invalid", errsWarns.get(ERRORS).get(1));
    }

    @Test
    public void givenAnAppealContainsAnInvalidAppellantMobileNumberGreaterThan11Digits_thenAddAnError() {
        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithBenefitType("Bla", buildAppellantWithMobileNumber("077761560000"), true, FormType.SSCS1PE), false, false, true);

        assertEquals("person1_mobile is invalid", errsWarns.get(ERRORS).get(0));
    }

    @Test
    public void givenAnAppealContainsAnInvalidRepresentativeMobileNumberGreaterThan11Digits_thenAddAnError() {
        Representative representative = buildRepresentative();
        representative.setContact(Contact.builder().build());
        representative.getContact().setMobile("0123456789000");

        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithRepresentative(buildAppellant(false), representative, true, FormType.SSCS1PE), false, false, true);

        assertEquals("representative_mobile is invalid", errsWarns.get(ERRORS).get(0));
    }

    @Test
    public void givenARepresentativeTitleIsInvalid_thenAddWarning() {
        Representative representative = buildRepresentative();
        representative.setContact(Contact.builder().build());
        representative.getName().setTitle("%54 3434 ^7*");

        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithRepresentative(buildAppellant(false), representative, true, FormType.SSCS1PE), false, false, true);

        assertEquals("representative_title is invalid", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    @Parameters({"", "null", " "})
    public void givenARepresentativeTitleIsEmpty_thenDoNotAddAnyWarnings(@Nullable String title) {
        Representative representative = buildRepresentative();
        representative.setContact(Contact.builder().build());
        representative.getName().setTitle(title);

        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithRepresentative(buildAppellant(false), representative, true, FormType.SSCS1PE), false, false, true);

        assertEquals(0, errsWarns.get(WARNINGS).size());
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenAnAppealContainsValidAppellantAnInvalidAppointeeMobileNumberGreaterThan11Digits_thenAddAnError() {
        Appellant appellant = buildAppellant(true);
        appellant.getContact().setMobile(VALID_MOBILE);
        appellant.setAppointee(buildAppointeeWithMobileNumber("077761560000"));
        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithBenefitType("Bla", appellant, true, FormType.SSCS1PE), false, false, true);

        assertEquals("person1_mobile is invalid", errsWarns.get(ERRORS).get(0));
    }

    @Test
    public void givenAnAppealContainsAnInvalidAppellantAnInvalidAppointeeMobileNumberGreaterThan11Digits_thenAddAnError() {
        Appellant appellant = buildAppellant(true);
        appellant.getContact().setMobile("077761560000");
        appellant.setAppointee(buildAppointeeWithMobileNumber("077761560000"));
        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithBenefitType("Bla", appellant, true, FormType.SSCS1PE), false, false, true);

        assertEquals("person1_mobile is invalid", errsWarns.get(ERRORS).get(0));
        assertEquals("person2_mobile is invalid", errsWarns.get(ERRORS).get(1));
    }

    @Test
    public void givenAnAppealContainsAValidAppellantMobileNumber_thenDoNotAddAWarning() {
        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithBenefitType(PIP.name(), buildAppellantWithMobileNumber(VALID_MOBILE), true, FormType.SSCS1PE),
                false, false, true);

        assertEquals(0, errsWarns.get(WARNINGS).size());
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenAnAppealContainsAnInvalidPostcode_thenAddAnError() {
        given(postcodeValidator.isValidPostcodeFormat(anyString())).willReturn(false);
        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithBenefitType("Bla", buildAppellantWithPostcode("Bla Bla"), true, FormType.SSCS1PE), false, false, true);

        assertEquals("person1_postcode is not in a valid format", errsWarns.get(ERRORS).get(0));
    }

    @Test
    public void givenAnAppealContainsAnValidPostcodeFormatButNotFound_thenAddWarning() {
        given(postcodeValidator.isValidPostcodeFormat(anyString())).willReturn(true);
        given(postcodeValidator.isValid(anyString(), isNull())).willReturn(false);
        var errsWarns = validator.validateAppeal(ocrCaseData,
                buildMinimumAppealDataWithBenefitType(PIP.name(),
                        buildAppellantWithPostcode("W1 1LA"), true, FormType.SSCS1PE), false, false, true);

        assertEquals("person1_postcode is not a valid postcode", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppealContainsAnValidPostcodeFormatButFound_thenNoErrorOrWarnings() {
        given(postcodeValidator.isValidPostcodeFormat(anyString())).willReturn(true);
        given(postcodeValidator.isValid(anyString(), isNull())).willReturn(true);
        given(regionalProcessingCenterService.getByPostcode(anyString())).willReturn(RegionalProcessingCenter.builder().address1("Address 1").name("Liverpool").build());
        var errsWarns = validator.validateAppeal( ocrCaseData, buildMinimumAppealDataWithBenefitType(PIP.name(), buildAppellantWithPostcode("W1 1LA"), true, FormType.SSCS1PE), false, false, true);

        assertThat(errsWarns.get(WARNINGS).size()).isEqualTo(0);
        assertThat(errsWarns.get(ERRORS).size()).isEqualTo(0);
    }

    @Test
    public void givenAnAppealContainsAValidPostcode_thenDoNotAddAWarning() {
        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithBenefitType(PIP.name(), buildAppellantWithPostcode(VALID_POSTCODE), true, FormType.SSCS1PE), false, false, true);

        assertEquals(0, errsWarns.get(WARNINGS).size());
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenARepresentativeDoesNotContainAFirstNameOrLastNameOrTitleOrCompany_thenAddAWarning() {
        Representative representative = buildRepresentative();
        representative.getName().setFirstName(null);
        representative.getName().setLastName(null);
        representative.getName().setTitle(null);
        representative.setOrganisation(null);

        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithRepresentative(buildAppellant(false), representative, true, FormType.SSCS1PE), false, false, true);

        assertEquals(
                "representative_company, representative_first_name and representative_last_name are empty. At least one must be populated",
                errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenARepresentativeContainsAFirstNameButDoesNotContainALastNameOrTitleOrCompany_thenDoNotAddAWarning() {
        Representative representative = buildRepresentative();
        representative.getName().setLastName(null);
        representative.getName().setTitle(null);
        representative.setOrganisation(null);

        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithRepresentative(buildAppellant(false), representative, true, FormType.SSCS1PE), false, false, true);

        assertEquals(0, errsWarns.get(WARNINGS).size());
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenARepresentativeContainsALastNameButDoesNotContainAFirstNameOrTitleOrCompany_thenDoNotAddAWarning() {
        Representative representative = buildRepresentative();
        representative.getName().setFirstName(null);
        representative.getName().setTitle(null);
        representative.setOrganisation(null);

        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithRepresentative(buildAppellant(false), representative, true, FormType.SSCS1PE), false, false, true);

        assertEquals(0, errsWarns.get(WARNINGS).size());
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenARepresentativeContainsATitleButDoesNotContainAFirstNameOrLastNameOrCompany_thenAddAWarning() {
        Representative representative = buildRepresentative();
        representative.getName().setFirstName(null);
        representative.getName().setLastName(null);
        representative.setOrganisation(null);

        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithRepresentative(buildAppellant(false), representative, true, FormType.SSCS1PE), false, false, true);

        assertEquals(
                "representative_company, representative_first_name and representative_last_name are empty. At least one must be populated",
                errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenARepresentativeContainsACompanyButDoesNotContainAFirstNameOrLastNameOrTitle_thenDoNotAddAWarning() {
        Representative representative = buildRepresentative();
        representative.getName().setFirstName(null);
        representative.getName().setLastName(null);
        representative.getName().setTitle(null);

        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithRepresentative(buildAppellant(false), representative, true, FormType.SSCS1PE), false, false, true);

        assertEquals(0, errsWarns.get(WARNINGS).size());
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenARepresentativeDoesNotContainAnAddressLine1_thenAddAWarning() {
        Representative representative = buildRepresentative();
        representative.getAddress().setLine1(null);

        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithRepresentative(buildAppellant(false), representative, true, FormType.SSCS1PE), false, false, true);

        assertEquals("representative_address_line1 is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenARepresentativeDoesNotContainATown_thenAddAWarning() {
        Representative representative = buildRepresentative();
        representative.getAddress().setLine2("101 Street");
        representative.getAddress().setTown(null);

        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithRepresentative(buildAppellant(false), representative, true, FormType.SSCS1PE), false, false, true);

        assertEquals("representative_address_line3 is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenARepresentativeDoesNotContainACountyAndContainAddressLine2_thenAddAWarning() {
        Representative representative = buildRepresentative();
        representative.getAddress().setLine2("101 Street");
        representative.getAddress().setCounty(null);

        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithRepresentative(buildAppellant(false), representative, true, FormType.SSCS1PE), false, false, true);

        assertEquals("representative_address_line4 is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenARepresentativeDoesNotContainACountyAndAddressLine2_thenAddAWarning() {
        ocrCaseData.remove("representative_address_line4");
        Representative representative = buildRepresentative();
        representative.getAddress().setLine2(null);
        representative.getAddress().setCounty(null);

        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithRepresentative(buildAppellant(false), representative, true, FormType.SSCS1PE), false, false, true);

        assertEquals("representative_address_line3 is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenARepresentativeDoesNotContainAPostcode_thenAddAWarningAndDoNotAddRegionalProcessingCenter() {
        Representative representative = buildRepresentative();
        representative.getAddress().setPostcode(null);

        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithRepresentative(buildAppellant(false), representative, true, FormType.SSCS1PE), false, false, true);

        assertEquals("representative_postcode is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenANullRepresentative_thenAddAnError() {
        Representative representative = null;
        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithRepresentative(buildAppellant(false), representative, true, FormType.SSCS1PE), false, false, true);

        assertEquals(1, errsWarns.get(ERRORS).size());
        assertEquals("The \"Has representative\" field is not selected, please select an option to proceed",
                errsWarns.get(ERRORS).get(0));
    }

    @Test
    public void givenARepresentativeWithHasRepresentativeFieldNotSet_thenAddAnError() {
        Representative representative = buildRepresentative();
        representative.setHasRepresentative(null);
        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithRepresentative(buildAppellant(false), representative, true, FormType.SSCS1PE), false, false, true);

        assertEquals(1, errsWarns.get(ERRORS).size());
        assertEquals(HAS_REPRESENTATIVE_FIELD_MISSING, errsWarns.get(ERRORS).get(0));
    }

    @Test
    public void givenAnAppointeeDoesNotContainATitle_thenAddAWarning() {
        Appellant appellant = buildAppellant(true);
        appellant.getAppointee().getName().setTitle(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_title is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppointeeDoesNotContainAFirstName_thenAddAWarning() {
        Appellant appellant = buildAppellant(true);
        appellant.getAppointee().getName().setFirstName(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_first_name is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppointeeDoesNotContainALastName_thenAddAWarning() {
        Appellant appellant = buildAppellant(true);
        appellant.getAppointee().getName().setLastName(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_last_name is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppointeeDoesNotContainAnAddressLine1_thenAddAWarning() {
        Appellant appellant = buildAppellant(true);
        appellant.getAppointee().getAddress().setLine1(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_address_line1 is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppointeeDoesNotContainAnAddressLine3AndContainAddressLine2_thenAddAWarning() {
        Appellant appellant = buildAppellant(true);
        appellant.getAppointee().getAddress().setLine2("101 Street");
        appellant.getAppointee().getAddress().setTown(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_address_line3 is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppointeeDoesNotContainAnAddressLine3And2_thenAddAWarning() {
        Appellant appellant = buildAppellant(true);
        ocrCaseData.remove("person1_address_line4");
        ocrCaseData.remove("person2_address_line4");
        appellant.getAppointee().getAddress().setLine2(null);
        appellant.getAppointee().getAddress().setTown(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_address_line2 is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppointeeDoesNotContainAnAddressLine4AndContainAddressLine2_thenAddAWarning() {
        Appellant appellant = buildAppellant(true);
        appellant.getAppointee().getAddress().setLine2("101 Street");
        appellant.getAppointee().getAddress().setCounty(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_address_line4 is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppointeeDoesNotContainAnAddressLine4And2_thenAddAWarning() {
        ocrCaseData.remove("person1_address_line4");
        ocrCaseData.remove("person2_address_line4");
        Appellant appellant = buildAppellant(true);
        appellant.getAppointee().getAddress().setLine2(null);
        appellant.getAppointee().getAddress().setCounty(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_address_line3 is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppointeeDoesNotContainAnAddressLinePostcode_thenAddAWarning() {
        Appellant appellant = buildAppellant(true);
        appellant.getAppointee().getAddress().setPostcode(null);

        var errsWarns = validator
                .validateAppeal( ocrCaseData, buildMinimumAppealData(appellant, true, FormType.SSCS1PE),
                        false, false, true);

        assertEquals("person1_postcode is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppealWithNoHearingType_thenAddAWarning() {
        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithHearingType(null, buildAppellant(false), true, FormType.SSCS1PE), false, false, true);

        assertEquals("is_hearing_type_oral and/or is_hearing_type_paper is invalid", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAllMandatoryFieldsForAnAppellantExists_thenDoNotAddAWarning() {
        Map<String, Object> pairs = buildMinimumAppealData(buildAppellant(false), true, FormType.SSCS1PE);

        var errsWarns = validator.validateAppeal( ocrCaseData, pairs, false, false, true);

        assertEquals(0, errsWarns.get(WARNINGS).size());
    }

    @Test
    public void givenAllMandatoryFieldsAndValidDocumentDoNotAddAnError() {
        Map<String, Object> pairs = buildMinimumAppealData(buildAppellant(false), false, FormType.SSCS1PE);

        pairs.put("sscsDocument", buildDocument("myfile.pdf"));

        validator = new AppealValidator(dwpAddressLookupService, addressValidator, SYA_APPEAL, titles);
        var errsWarns = validator.validateAppeal(new HashMap<>(), pairs, true, false, false);

        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenAllMandatoryFieldsAndDocumentNameIsNullAddAnError() {
        Map<String, Object> pairs = buildMinimumAppealData(buildAppellant(false), false, FormType.SSCS1PE);

        pairs.put("sscsDocument", buildDocument(null));

        validator = new AppealValidator(dwpAddressLookupService, addressValidator, SYA_APPEAL, titles);
        var errsWarns = validator.validateAppeal(new HashMap<>(), pairs, true, false, false);

        assertEquals(
                "There is a file attached to the case that does not have a filename, add a filename, e.g. filename.pdf",
                errsWarns.get(ERRORS).get(0));
    }

    @Test
    public void givenAllMandatoryFieldsAndDocumentNameNoExtensionAddAnError() {
        Map<String, Object> pairs = buildMinimumAppealData(buildAppellant(false), false, FormType.SSCS1PE);

        pairs.put("sscsDocument", buildDocument("Waiver"));

        validator = new AppealValidator(dwpAddressLookupService, addressValidator, SYA_APPEAL, titles);
        var errsWarns = validator.validateAppeal(new HashMap<>(), pairs, true, false, false);

        assertEquals(
                "There is a file attached to the case called Waiver, filenames must have extension, e.g. filename.pdf",
                errsWarns.get(ERRORS).get(0));
    }

    @Test
    public void givenAValidationCallbackTypeWithIncompleteDetails_thenAddAWarningWithCorrectMessage() {

        Appellant appellant = buildAppellant(false);
        appellant.getAddress().setPostcode(null);

        validator = new AppealValidator(dwpAddressLookupService, addressValidator, SYA_APPEAL, titles);
        var errsWarns = validator.validateAppeal(new HashMap<>(), buildMinimumAppealData(appellant, false, FormType.SSCS1PE),
                false, false, false);

        assertEquals("Appellant postcode is empty", errsWarns.get(WARNINGS).get(0));
        verifyNoInteractions(regionalProcessingCenterService);
    }

    @Test
    public void givenAValidationCallbackEventIsAppealToProceedAndMrnDateIsEmpty_thenNoWarningOrErrorMessage() {

        Map<String, Object> pairs =
                buildMinimumAppealDataWithMrn(MrnDetails.builder().dwpIssuingOffice("Sheffield DRT").build(),
                        buildAppellant(false), false, FormType.SSCS1PE);

        validator = new AppealValidator(dwpAddressLookupService, addressValidator, SYA_APPEAL, titles);
        var errsWarns = validator.validateAppeal(new HashMap<>(), pairs, true, false, false);

        assertEquals(0, errsWarns.get(WARNINGS).size());
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenAValidationCallbackEventIsOtherAndMrnDateIsEmpty_thenNoWarningOrErrorMessage() {

        Map<String, Object> pairs =
                buildMinimumAppealDataWithMrn(MrnDetails.builder().dwpIssuingOffice("Sheffield DRT").build(),
                        buildAppellant(false), false, FormType.SSCS1PE);

        validator = new AppealValidator(dwpAddressLookupService, addressValidator, SYA_APPEAL, titles);
        var errsWarns = validator.validateAppeal(new HashMap<>(), pairs, false, false, false);

        assertEquals("Mrn date is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppealWithInvalidMobileNumber_thenAddError() {
        var caseData =
                buildMinimumAppealDataWithBenefitType("Bla", buildAppellantWithMobileNumber("07776156"), true, FormType.SSCS1PE);
        ((Appeal)caseData.get("appeal")).setMrnDetails(MrnDetails.builder().dwpIssuingOffice("2").mrnDate("2018-12-09").build());

        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                caseData,
                true, false, true);

        assertEquals("person1_mobile is invalid", errsWarns.get(ERRORS).get(0));
        assertEquals(1, errsWarns.get(WARNINGS).size());
    }

    @Test
    @Parameters({"07900123456", "01277323440", "01277323440 ext 123"})
    public void givenAnAppealWithValidHearingPhoneNumber_thenDoNotAddWarning(String number) {
        HearingSubtype hearingSubtype = HearingSubtype.builder().hearingTelephoneNumber(number).build();

        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithHearingSubtype(hearingSubtype, buildAppellant(false),
                        true, FormType.SSCS1PE), false, false, true);

        assertEquals(0, errsWarns.get(ERRORS).size());
        assertEquals(0, errsWarns.get(WARNINGS).size());
    }

    @Test
    public void givenAnAppealWithAnInvalidHearingPhoneNumber_thenAddWarning() {
        HearingSubtype hearingSubtype = HearingSubtype.builder().hearingTelephoneNumber("01222").build();

        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithHearingSubtype(hearingSubtype, buildAppellant(false), true, FormType.SSCS1PE),
                false, false, true);

        assertEquals(
                "Telephone hearing selected but the number used is invalid. Please check either the hearing_telephone_number or person1_phone fields",
                errsWarns.get(WARNINGS).get(0));
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenAnAppealWithAnInvalidHearingPhoneNumberForSscsCase_thenAddWarning() {
        Map<String, Object> pairs = buildMinimumAppealDataWithHearingSubtype(
                HearingSubtype.builder().wantsHearingTypeTelephone("Yes").hearingTelephoneNumber("01222").build(),
                buildAppellant(false), false, FormType.SSCS1PE);

        validator = new AppealValidator(dwpAddressLookupService, addressValidator, SYA_APPEAL, titles);
        var errsWarns = validator.validateAppeal(new HashMap<>(), pairs, true, false, false);

        assertEquals(
                "Telephone hearing selected but the number used is invalid. Please check either the telephone or hearing telephone number fields",
                errsWarns.get(WARNINGS).get(0));
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenAnAppealWithAHearingTypeTelephoneSelectedButNoTelephoneEntered_thenAddWarning() {
        HearingSubtype hearingSubtype =
                HearingSubtype.builder().wantsHearingTypeTelephone("Yes").hearingTelephoneNumber(null).build();

        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithHearingSubtype(hearingSubtype, buildAppellant(false), true, FormType.SSCS1PE), false, false, true);

        assertEquals("hearing_telephone_number has not been provided but data indicates hearing telephone is required",
                errsWarns.get(WARNINGS).get(0));
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenAnAppealWithAHearingTypeTelephoneSelectedButNoTelephoneEnteredForSscsCase_thenAddWarning() {
        Map<String, Object> pairs = buildMinimumAppealDataWithHearingSubtype(
                HearingSubtype.builder().wantsHearingTypeTelephone("Yes").hearingTelephoneNumber(null).build(),
                buildAppellant(false), false, FormType.SSCS1PE);

        validator = new AppealValidator(dwpAddressLookupService, addressValidator, SYA_APPEAL, titles);
        var errsWarns = validator.validateAppeal(new HashMap<>(), pairs, true, false, false);

        assertEquals("Hearing telephone number has not been provided but data indicates hearing telephone is required",
                errsWarns.get(WARNINGS).get(0));
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenAnAppealWithAHearingTypeVideoSelectedButNoVideoEmailEntered_thenAddWarning() {
        HearingSubtype hearingSubtype =
                HearingSubtype.builder().wantsHearingTypeVideo("Yes").hearingVideoEmail(null).build();

        var errsWarns = validator.validateAppeal( ocrCaseData,
                buildMinimumAppealDataWithHearingSubtype(hearingSubtype, buildAppellant(false), true, FormType.SSCS1PE), false, false, true);

        assertEquals("hearing_video_email has not been provided but data indicates hearing video is required",
                errsWarns.get(WARNINGS).get(0));
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenAnAppealWithAHearingTypeVideoSelectedButNoVideoEmailEnteredForSscsCase_thenAddWarning() {
        Map<String, Object> pairs = buildMinimumAppealDataWithHearingSubtype(
                HearingSubtype.builder().wantsHearingTypeVideo("Yes").hearingVideoEmail(null).build(),
                buildAppellant(false), false, FormType.SSCS1PE);

        validator = new AppealValidator(dwpAddressLookupService, addressValidator, SYA_APPEAL, titles);
        var errsWarns = validator.validateAppeal(new HashMap<>(), pairs, true, false, false);

        assertEquals("Hearing video email address has not been provided but data indicates hearing video is required",
                errsWarns.get(WARNINGS).get(0));
        assertEquals(0, errsWarns.get(ERRORS).size());
    }

    @Test
    public void givenAnAppealWithAnHearingTypePaperAndEmptyHearingSubTypeForSscsCase_thenNoWarning() {
        Map<String, Object> pairs =
                buildMinimumAppealDataWithHearingType(HEARING_TYPE_PAPER, buildAppellant(false), false, FormType.SSCS1PE);

        validator = new AppealValidator(dwpAddressLookupService, addressValidator, SYA_APPEAL, titles);
        var errsWarns = validator.validateAppeal(new HashMap<>(), pairs, true, false, false);

        assertEquals(0, errsWarns.get(WARNINGS).size());
    }

    @Test
    @Parameters({"SSCS1PEU", "SSCS2", "SSCS5"})
    public void givenAnAppealWithAnEmptyHearingSubTypeAndFormTypeIsSscs1peuForSscsCase_thenAddWarning(FormType formType) {
        Map<String, Object> pairs =
                buildMinimumAppealDataWithHearingSubtype(HearingSubtype.builder().build(), buildAppellant(false), false, formType);
        if (formType.equals(FormType.SSCS2)) {
            pairs.put("childMaintenanceNumber", "123456");
        }
        pairs.put("formType", formType);

        validator = new AppealValidator(dwpAddressLookupService, addressValidator, SYA_APPEAL, titles);
        var errsWarns = validator.validateAppeal(new HashMap<>(), pairs, true, false, false);

        assertEquals(1, errsWarns.get(WARNINGS).size());
        assertEquals("Hearing option telephone, video and face to face are empty. At least one must be populated",
                errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenAnAppealWithAnEmptyHearingSubTypeForSscsCase_thenNoWarning() {
        Map<String, Object> pairs = buildMinimumAppealDataWithHearingSubtype(HearingSubtype.builder().build(), buildAppellant(false), false, FormType.SSCS1PE);
        pairs.put("formType", FormType.SSCS1);

        validator = new AppealValidator(dwpAddressLookupService, addressValidator, SYA_APPEAL, titles);
        var errsWarns = validator.validateAppeal(new HashMap<>(), pairs, true, false, false);

        assertEquals(0, errsWarns.get(WARNINGS).size());
    }

    @Test
    public void givenAnAppealWithAnEmptyHearingSubTypeAndFormTypIsNullForSscsCase_thenNoWarning() {
        Map<String, Object> pairs =
                buildMinimumAppealDataWithHearingSubtype(HearingSubtype.builder().build(), buildAppellant(false), false, FormType.SSCS1PE);
        pairs.put("formType", null);

        validator = new AppealValidator(dwpAddressLookupService, addressValidator, SYA_APPEAL, titles);
        var errsWarns = validator.validateAppeal(new HashMap<>(), pairs, true, false, false);

        assertEquals(0, errsWarns.get(WARNINGS).size());
    }

    @Test
    public void givenAnAppealWithAnHearingSubTypeVideoForSscsCase_thenNoWarning() {
        Map<String, Object> pairs = buildMinimumAppealDataWithHearingSubtype(
                HearingSubtype.builder().wantsHearingTypeVideo("Yes").hearingVideoEmail("m@m.com").build(),
                buildAppellant(false), false, FormType.SSCS1PE);

        validator = new AppealValidator(dwpAddressLookupService, addressValidator, SYA_APPEAL, titles);
        var errsWarns = validator.validateAppeal(new HashMap<>(), pairs, true, false, false);

        assertEquals(0, errsWarns.get(WARNINGS).size());
    }

    @Test
    public void givenAnAppealWithAnHearingSubTypeFaceForSscsCase_thenNoWarning() {
        Map<String, Object> pairs =
                buildMinimumAppealDataWithHearingSubtype(HearingSubtype.builder().wantsHearingTypeFaceToFace("Yes").build(),
                        buildAppellant(false), false, FormType.SSCS1PE);

        validator = new AppealValidator(dwpAddressLookupService, addressValidator, SYA_APPEAL, titles);
        var errsWarns = validator.validateAppeal(new HashMap<>(), pairs, true, false, false);

        assertEquals(0, errsWarns.get(WARNINGS).size());
    }

    @Test
    public void givenSscs2FormWithoutChildMaintenance_thenAddAWarning() {

        Map<String, Object> caseData = buildMinimumAppealDataWithBenefitTypeAndFormType(CHILD_SUPPORT.getShortName(), buildAppellant(false), true, FormType.SSCS2);
        caseData.remove("childMaintenanceNumber");

        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                caseData,
                false, false, true);

        assertEquals("person1_child_maintenance_number is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenSscs2FormWithChildMaintenance_thenAppellantShouldReturnValue() {

        var errsWarns = validator.validateAppeal(
                ocrCaseData, buildCaseWithChildMaintenanceWithOtherPartyNameAddress(CHILD_MAINTENANCE_NUMBER,
                        OTHER_PARTY_ADDRESS_LINE1, OTHER_PARTY_ADDRESS_LINE2,OTHER_PARTY_ADDRESS_LINE3, OTHER_PARTY_POSTCODE, buildOtherPartyName()),
                false, false, true);

        assertEquals(0, errsWarns.get(WARNINGS).size());
    }

    @Test
    @Parameters({", test2, test3, TS1 1ST, other_party_address_line1 is empty, 1",
            "test1, , , TS1 1ST, other_party_address_line2 is empty, 1",
            "test1, test2, , , other_party_postcode is empty, 1",
            "test1, , , , other_party_address_line2 is empty, 2",
            ", , , TS1 1ST, other_party_address_line1 is empty, 2",
    })
    public void givenSscs2FormWithoutOtherPartyAddressEntryAndIgnoreWarningsFalse_thenAddAWarning(String line1, String line2, String line3, String postcode, String warning, int size) {

        var caseData = buildCaseWithChildMaintenanceWithOtherPartyNameAddress(CHILD_MAINTENANCE_NUMBER,line1, line2,line3, postcode, buildOtherPartyName());
        ((Appeal)caseData.get("appeal")).getMrnDetails().setDwpIssuingOffice("test-hmcts-address");

        var errsWarns = validator.validateAppeal(ocrCaseData, caseData, true, false, true);

        assertFalse(errsWarns.get(WARNINGS).isEmpty());
        assertEquals(size, errsWarns.get(WARNINGS).size());
        assertEquals(warning, errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenOtherParty_WithFirstNameLastNamePopulated_WithNoAddress_noWarnings() {
        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                buildCaseWithChildMaintenanceWithOtherPartyNameAddress(CHILD_MAINTENANCE_NUMBER,"", "","", "", buildOtherPartyName()),
                false, false, true);
        assertTrue(errsWarns.get(WARNINGS).isEmpty());
    }

    @Test
    public void givenOtherParty_WithNoName_WithNoAddress_noWarnings() {
        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                buildCaseWithChildMaintenanceWithOtherPartyNameAddress(CHILD_MAINTENANCE_NUMBER,"", "","", "", Name.builder().build()),
                false, false, true);
        assertTrue(errsWarns.get(WARNINGS).isEmpty());
    }

    @Test
    public void givenOtherParty_NoName_WithAddressPresent_WarningSeen() {
        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                buildCaseWithChildMaintenanceWithOtherPartyNameAddress(CHILD_MAINTENANCE_NUMBER,"line1", "","line3", "W1", Name.builder().build()),
                false, false, true);
        assertFalse(errsWarns.get(WARNINGS).isEmpty());
    }

    @Test
    @Parameters({"true", "false"})
    public void givenOtherParty_WithFirstNameOrLastNameMissing_WarningSeen(boolean isFirstnameBlank) {

        Name name = Name.builder().firstName(isFirstnameBlank ? "" : "fn").lastName(!isFirstnameBlank ? "" : "ln").build();
        var caseData = buildCaseWithChildMaintenanceWithOtherPartyNameAddress(CHILD_MAINTENANCE_NUMBER,"line1", "line2","line3", "W1", name);
        ((Appeal)caseData.get("appeal")).getMrnDetails().setDwpIssuingOffice("test-hmcts-address");

        var errsWarns = validator.validateAppeal(ocrCaseData, caseData, true, false, true);

        assertFalse(errsWarns.get(WARNINGS).isEmpty());
        assertEquals(1, errsWarns.get(WARNINGS).size());
    }

    @Test
    public void givenSscs2FormWithOtherPartyAddressEntry_thenValueIsSet() {

        var errsWarns = validator.validateAppeal(
                ocrCaseData,
                buildCaseWithChildMaintenanceWithOtherPartyNameAddress(
                        CHILD_MAINTENANCE_NUMBER,OTHER_PARTY_ADDRESS_LINE1, OTHER_PARTY_ADDRESS_LINE2,OTHER_PARTY_ADDRESS_LINE3, OTHER_PARTY_POSTCODE, buildOtherPartyName()),
                false, false, true);

        assertTrue(errsWarns.get(WARNINGS).isEmpty());
        assertTrue(errsWarns.get(ERRORS).isEmpty());
    }


    @Test
    public void givenSscs2FormWithOtherPartyLastNameMissingAndIgnoreWarningsFalse_thenOtherPartyShouldReturnWarning() {
        Name otherPartyName = buildOtherPartyName();
        otherPartyName.setLastName(null);
        var errsWarns = validator.validateAppeal(
                ocrCaseData, buildCaseWithChildMaintenanceWithOtherPartyNameAddress(
                        CHILD_MAINTENANCE_NUMBER,OTHER_PARTY_ADDRESS_LINE1, OTHER_PARTY_ADDRESS_LINE2,OTHER_PARTY_ADDRESS_LINE3, OTHER_PARTY_POSTCODE, otherPartyName),
                false, false, true);

        assertEquals(1, errsWarns.get(WARNINGS).size());
        assertEquals("other_party_last_name is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenSscs2FormWithOtherPartyFirstNameMissingAndIgnoreWarningsFalse_thenOtherPartyShouldReturnWarning() {
        Name otherPartyName = buildOtherPartyName();
        otherPartyName.setFirstName(null);
        var caseData = buildCaseWithChildMaintenanceWithOtherPartyNameAddress(
                CHILD_MAINTENANCE_NUMBER,OTHER_PARTY_ADDRESS_LINE1, OTHER_PARTY_ADDRESS_LINE2,OTHER_PARTY_ADDRESS_LINE3, OTHER_PARTY_POSTCODE, otherPartyName);
        ((Appeal)caseData.get("appeal")).getMrnDetails().setDwpIssuingOffice("test-hmcts-address");

        var errsWarns = validator.validateAppeal(
                ocrCaseData, caseData,
                false, false, true);

        assertEquals(1, errsWarns.get(WARNINGS).size());
        assertEquals("other_party_first_name is empty", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenSscs2FormWithOtherPartyTitleInvalidAndIgnoreWarningsFalse_thenOtherPartyShouldReturnWarning() {
        Name otherPartyName = buildOtherPartyName();
        otherPartyName.setTitle("Random");
        var errsWarns = validator.validateAppeal(
                ocrCaseData, buildCaseWithChildMaintenanceWithOtherPartyNameAddress(
                        CHILD_MAINTENANCE_NUMBER,OTHER_PARTY_ADDRESS_LINE1, OTHER_PARTY_ADDRESS_LINE2,OTHER_PARTY_ADDRESS_LINE3, OTHER_PARTY_POSTCODE, otherPartyName),
                false, false, true);

        assertEquals(1, errsWarns.get(WARNINGS).size());
        assertEquals("other_party_title is invalid", errsWarns.get(WARNINGS).get(0));
    }

    @Test
    public void givenSscs2FormWithOtherPartyLastNameMissingAndIgnoreWarningsTrue_thenNoWarningsShownAndOtherPartyRemoved() {
        Name otherPartyName = buildOtherPartyName();
        otherPartyName.setLastName(null);
        var caseData = buildCaseWithChildMaintenanceWithOtherPartyNameAddress(
                CHILD_MAINTENANCE_NUMBER,OTHER_PARTY_ADDRESS_LINE1, OTHER_PARTY_ADDRESS_LINE2,null, OTHER_PARTY_POSTCODE, otherPartyName);
        ((Appeal)caseData.get("appeal")).getMrnDetails().setDwpIssuingOffice("test-hmcts-address");

        var errsWarns = validator.validateAppeal(
                ocrCaseData, caseData,
                false, true, false);

        assertEquals(0, errsWarns.get(WARNINGS).size());
    }

    @Test
    public void givenSscs2FormWithChildMaintenanceNumberMissingAndIgnoreWarningsTrue_thenNoWarningsShownAndChildMaintenanceNumberRemoved() {

        var errsWarns = validator.validateAppeal(
                ocrCaseData, buildCaseWithChildMaintenanceWithOtherPartyNameAddress(null,
                        OTHER_PARTY_ADDRESS_LINE1, OTHER_PARTY_ADDRESS_LINE2,null, OTHER_PARTY_POSTCODE,
                        buildOtherPartyName()), false, true, false);

        assertEquals(0, errsWarns.get(WARNINGS).size());
    }

    @Test
    public void givenSscs2FormWithChildMaintenanceNumberMissingAndIgnoreWarningsFalse_thenWarningShown() {

        var errsWarns = validator.validateAppeal(
                ocrCaseData, buildCaseWithChildMaintenanceWithOtherPartyNameAddress(null,
                        OTHER_PARTY_ADDRESS_LINE1, OTHER_PARTY_ADDRESS_LINE2,null, OTHER_PARTY_POSTCODE,
                        buildOtherPartyName()), false, false, true);

        assertEquals(1, errsWarns.get(WARNINGS).size());
        assertEquals("person1_child_maintenance_number is empty", errsWarns.get(WARNINGS).get(0));
    }


    @Test
    public void givenSscs2FormWithOtherPartyFirstNameMissingAndIgnoreWarningsTrue_thenNoWarningsShown() {
        Name otherPartyName = buildOtherPartyName();
        otherPartyName.setFirstName(null);

        var errsWarns = validator.validateAppeal(
                ocrCaseData, buildCaseWithChildMaintenanceWithOtherPartyNameAddress(
                        CHILD_MAINTENANCE_NUMBER,OTHER_PARTY_ADDRESS_LINE1, OTHER_PARTY_ADDRESS_LINE2,OTHER_PARTY_ADDRESS_LINE3, OTHER_PARTY_POSTCODE, otherPartyName),
                false, true, false);

        assertEquals(0, errsWarns.get(WARNINGS).size());
    }

    @Test
    public void givenSscs2FormWithOtherPartyTitleInvalidAndIgnoreWarningsTrue_thenNoWarningsShown() {
        Name otherPartyName = buildOtherPartyName();
        otherPartyName.setTitle("random");

        var errsWarns = validator.validateAppeal(
                ocrCaseData, buildCaseWithChildMaintenanceWithOtherPartyNameAddress(
                        CHILD_MAINTENANCE_NUMBER,OTHER_PARTY_ADDRESS_LINE1, OTHER_PARTY_ADDRESS_LINE2,OTHER_PARTY_ADDRESS_LINE3, OTHER_PARTY_POSTCODE, otherPartyName),
                false, true, false);

        assertEquals(0, errsWarns.get(WARNINGS).size());
    }

    @Test
    public void givenSscs2FormWithValidOtherPartyAndIgnoreWarningsTrue_thenNoWarningsShownAndOtherPartiesCreated() {
        Name otherPartyName = buildOtherPartyName();

        var errsWarns = validator.validateAppeal(
                ocrCaseData, buildCaseWithChildMaintenanceWithOtherPartyNameAddress(
                        CHILD_MAINTENANCE_NUMBER,OTHER_PARTY_ADDRESS_LINE1, OTHER_PARTY_ADDRESS_LINE2,OTHER_PARTY_ADDRESS_LINE3, OTHER_PARTY_POSTCODE, otherPartyName),
                false, true, false);

        assertEquals(0, errsWarns.get(WARNINGS).size());
    }

    @Test
    @Parameters({", test2, test3, TS1 1ST",
            "test1, , , TS1 1ST",
            "test1, test2, test3,"
    })
    public void givenSscs2FormWithOtherPartyAddressFieldMissingAndIgnoreWarningsTrue_thenNoWarningsShownAddressNull(String line1, String line2, String line3, String postcode) {

        var caseData = buildCaseWithChildMaintenanceWithOtherPartyNameAddress(CHILD_MAINTENANCE_NUMBER,line1, line2, line3, postcode, buildOtherPartyName());
        ((Appeal)caseData.get("appeal")).getMrnDetails().setDwpIssuingOffice("test-hmcts-address");
        var errsWarns = validator.validateAppeal(ocrCaseData, caseData, true, true, false);

        assertEquals(0, errsWarns.get(WARNINGS).size());
    }

    private Object buildDocument(String filename) {
        List<SscsDocument> documentDetails = new ArrayList<>();

        SscsDocumentDetails details = SscsDocumentDetails.builder()
                .documentFileName(filename).documentLink(DocumentLink.builder().documentFilename(filename).build()).build();
        documentDetails.add(SscsDocument.builder().value(details).build());

        return documentDetails;
    }

    private Map<String, Object> buildMinimumAppealData(Appellant appellant, Boolean exceptionCaseType,
                                                       FormType formType) {
        return buildMinimumAppealDataWithMrnDateAndBenefitType(defaultMrnDetails, PIP.name(), appellant,
                buildMinimumRep(), null, exceptionCaseType, HEARING_TYPE_ORAL,
                HearingSubtype.builder().wantsHearingTypeFaceToFace("Yes").build(), formType);
    }

    private Map<String, Object> buildMinimumAppealDataWithMrn(MrnDetails mrn, Appellant appellant,
                                                              Boolean exceptionCaseType,
                                                              FormType formType) {
        return buildMinimumAppealDataWithMrnDateAndBenefitType(mrn, ESA.name(), appellant, buildMinimumRep(), null,
                exceptionCaseType, HEARING_TYPE_PAPER, null, formType);
    }

    private Map<String, Object> buildMinimumAppealDataWithBenefitType(String benefitCode, Appellant appellant,
                                                                      Boolean exceptionCaseType,
                                                                      FormType formType) {
        return buildMinimumAppealDataWithMrnDateAndBenefitType(defaultMrnDetails, benefitCode, appellant,
                buildMinimumRep(), null, exceptionCaseType, HEARING_TYPE_ORAL,
                HearingSubtype.builder().wantsHearingTypeFaceToFace("Yes").build(), formType);
    }

    private Map<String, Object> buildMinimumAppealDataWithRepresentative(Appellant appellant,
                                                                         Representative representative,
                                                                         Boolean exceptionCaseType,
                                                                         FormType formType) {
        return buildMinimumAppealDataWithMrnDateAndBenefitType(defaultMrnDetails, PIP.name(), appellant, representative,
                null, exceptionCaseType, HEARING_TYPE_ORAL,
                HearingSubtype.builder().wantsHearingTypeFaceToFace("Yes").build(), formType);
    }

    private Map<String, Object> buildMinimumAppealDataWithExcludedDate(String excludedDate, Appellant appellant,
                                                                       Boolean exceptionCaseType,
                                                                       FormType formType) {
        return buildMinimumAppealDataWithMrnDateAndBenefitType(defaultMrnDetails, PIP.name(), appellant,
                buildMinimumRep(), excludedDate, exceptionCaseType, HEARING_TYPE_ORAL, null, formType);
    }

    private Map<String, Object> buildMinimumAppealDataWithHearingType(String hearingType, Appellant appellant,
                                                                      Boolean exceptionCaseType,
                                                                      FormType formType) {
        return buildMinimumAppealDataWithMrnDateAndBenefitType(defaultMrnDetails, PIP.name(), appellant,
                buildMinimumRep(), null, exceptionCaseType, hearingType, null, formType);
    }

    private Map<String, Object> buildMinimumAppealDataWithHearingSubtype(HearingSubtype hearingSubtype,
                                                                         Appellant appellant,
                                                                         Boolean exceptionCaseType,
                                                                         FormType formType) {
        return buildMinimumAppealDataWithMrnDateAndBenefitType(defaultMrnDetails, PIP.name(), appellant,
                buildMinimumRep(), null, exceptionCaseType, HEARING_TYPE_ORAL, hearingSubtype, formType);
    }

    private Representative buildMinimumRep() {
        return Representative.builder().hasRepresentative("No").build();
    }

    private Map<String, Object> buildMinimumAppealDataWithBenefitTypeAndFormType(String benefitCode,
                                                                                 Appellant appellant,
                                                                                 Boolean exceptionCaseType,
                                                                                 FormType formType) {
        return buildMinimumAppealDataWithMrnDateFormTypeAndBenefitType(defaultMrnDetails, benefitCode, appellant,
                buildMinimumRep(), null, exceptionCaseType, HEARING_TYPE_ORAL,
                HearingSubtype.builder().wantsHearingTypeFaceToFace("Yes").build(), formType);
    }

    private Map<String, Object> buildMinimumAppealDataWithMrnDateAndBenefitType(MrnDetails mrn, String benefitCode,
                                                                                Appellant appellant,
                                                                                Representative representative,
                                                                                String excludeDates,
                                                                                Boolean exceptionCaseType,
                                                                                String hearingType,
                                                                                HearingSubtype hearingSubtype,
                                                                                FormType formType) {
        return buildMinimumAppealDataWithMrnDateFormTypeAndBenefitType(mrn, benefitCode, appellant, representative,
                excludeDates,
                exceptionCaseType, hearingType, hearingSubtype, formType);
    }

    private Map<String, Object> buildMinimumAppealDataWithMrnDateFormTypeAndBenefitType(MrnDetails mrn,
                                                                                        String benefitCode,
                                                                                        Appellant appellant,
                                                                                        Representative representative,
                                                                                        String excludeDates,
                                                                                        Boolean exceptionCaseType,
                                                                                        String hearingType,
                                                                                        HearingSubtype hearingSubtype,
                                                                                        FormType formType) {
        Map<String, Object> dataMap = new HashMap<>();
        List<ExcludeDate> excludedDates = new ArrayList<>();
        excludedDates.add(ExcludeDate.builder().value(DateRange.builder().start(excludeDates).build()).build());

        dataMap.put("formType", formType);
        dataMap.put("appeal", Appeal.builder()
                .mrnDetails(
                        MrnDetails.builder().mrnDate(mrn.getMrnDate()).dwpIssuingOffice(mrn.getDwpIssuingOffice()).build())
                .benefitType(BenefitType.builder().code(benefitCode).build())
                .appellant(appellant)
                .rep(representative)
                .hearingOptions(HearingOptions.builder().excludeDates(excludedDates).build())
                .hearingType(hearingType)
                .hearingSubtype(hearingSubtype)
                .build());

        if (exceptionCaseType) {
            dataMap.put("bulkScanCaseReference", 123);
        }
        if (formType.equals(FormType.SSCS2)) {
            dataMap.put("childMaintenanceNumber", "123456");
        }
        return dataMap;
    }

    private Appellant buildAppellant(Boolean withAppointee) {
        return buildAppellantWithMobileNumberAndPostcode(withAppointee, VALID_MOBILE, VALID_POSTCODE);
    }

    private Appellant buildAppellantWithPostcode(String postcode) {
        return buildAppellantWithMobileNumberAndPostcode(false, VALID_MOBILE, postcode);
    }

    private Appellant buildAppellantWithMobileNumber(String mobileNumber) {
        return buildAppellantWithMobileNumberAndPostcode(false, mobileNumber, VALID_POSTCODE);
    }

    private Appellant buildAppellantWithMobileNumberAndPostcode(Boolean withAppointee, String mobileNumber,
                                                                String postcode) {
        Appointee appointee = withAppointee ? buildAppointee(VALID_MOBILE) : null;

        return Appellant.builder()
                .name(Name.builder().title("Mr").firstName("Bob").lastName("Smith").build())
                .address(
                        Address.builder().line1("101 My Road").town("Brentwood").county("Essex").postcode(postcode).build())
                .identity(Identity.builder().nino("BB000000B").build())
                .contact(Contact.builder().mobile(mobileNumber).build())
                .appointee(appointee)
                .role(Role.builder().name("Paying parent").build()).build();
    }


    private Map<String, Object> buildCaseWithChildMaintenanceWithOtherPartyNameAddress(String childMaintenanceNumber, String line1, String line2, String line3, String postcode, Name otherPartyName) {
        Map<String, Object> datamap = buildMinimumAppealDataWithMrnDateFormTypeAndBenefitType(
                defaultMrnDetails,
                UC.getShortName(),
                buildAppellant(true),
                buildMinimumRep(),
                null,
                true,
                HEARING_TYPE_ORAL,
                HearingSubtype.builder().wantsHearingTypeFaceToFace("Yes").build(),
                FormType.SSCS2);
        datamap.put("childMaintenanceNumber", childMaintenanceNumber);
        datamap.put("otherParties", Collections.singletonList(CcdValue.<OtherParty>builder().value(
                        OtherParty.builder()
                                .name(otherPartyName)
                                .address(Address.builder()
                                        .line1(line1)
                                        .town(line2)
                                        .county((line3 != null && !line3.equals("")) ? "." : line3)
                                        .postcode(postcode)
                                        .build())
                                .build())
                .build()));
        return datamap;
    }

    private Name buildOtherPartyName() {
        return Name.builder().title("Mr").firstName("Jerry").lastName("Fisher").build();
    }

    private Appointee buildAppointeeWithMobileNumber(String mobileNumber) {
        return buildAppointee(mobileNumber);
    }

    private Appointee buildAppointee(String mobileNumber) {

        return Appointee.builder()
                .name(Name.builder().title("Mr").firstName("Tim").lastName("Garwood").build())
                .address(Address.builder().line1("101 My Road").town("Gidea Park").county("Essex").postcode(VALID_POSTCODE)
                        .build())
                .identity(Identity.builder().build())
                .contact(Contact.builder().mobile(mobileNumber).build())
                .build();
    }

    private Representative buildRepresentative() {

        return Representative.builder()
                .hasRepresentative("Yes")
                .organisation("Bob the builders Ltd")
                .name(Name.builder().title("Mr").firstName("Bob").lastName("Smith").build())
                .address(
                        Address.builder().line1("101 My Road").town("Brentwood").county("Essex").postcode("CM13 1HG").build())
                .build();
    }

}
