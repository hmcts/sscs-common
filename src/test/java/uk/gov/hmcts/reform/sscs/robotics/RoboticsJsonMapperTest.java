package uk.gov.hmcts.reform.sscs.robotics;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;
import static uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils.buildCaseData;

import java.time.LocalDate;
import java.util.*;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.converters.Nullable;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import uk.gov.hmcts.reform.sscs.ccd.domain.*;
import uk.gov.hmcts.reform.sscs.service.AirLookupService;
import uk.gov.hmcts.reform.sscs.service.DwpAddressLookupService;

@RunWith(JUnitParamsRunner.class)
public class RoboticsJsonMapperTest {

    private static final String caseId = "12345678";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private RoboticsJsonMapper roboticsJsonMapper;
    private RoboticsWrapper roboticsWrapper;
    private final RoboticsJsonValidator roboticsJsonValidator = new RoboticsJsonValidator(
            "/schema/sscs-robotics.json");
    private JSONObject roboticsJson;

    private final DwpAddressLookupService dwpAddressLookupService = new DwpAddressLookupService();

    @Mock
    private AirLookupService airLookupService;

    private final List<ElementDisputed> elementsDisputedGeneralList = new ArrayList<>();
    private final List<ElementDisputed> elementsDisputedSanctionsList = new ArrayList<>();
    private final List<ElementDisputed> elementsDisputedOverpaymentList = new ArrayList<>();
    private final List<ElementDisputed> elementsDisputedHousingList = new ArrayList<>();
    private final List<ElementDisputed> elementsDisputedChildCareList = new ArrayList<>();
    private final List<ElementDisputed> elementsDisputedCareList = new ArrayList<>();
    private final List<ElementDisputed> elementsDisputedChildElementList = new ArrayList<>();
    private final List<ElementDisputed> elementsDisputedChildDisabledList = new ArrayList<>();

    @Before
    public void setup() {
        openMocks(this);
        roboticsJsonMapper = new RoboticsJsonMapper(dwpAddressLookupService, airLookupService);

        SscsCaseData sscsCaseData = buildCaseData();
        sscsCaseData.getAppeal().getAppellant().setIsAppointee("Yes");
        roboticsWrapper = RoboticsWrapper
                .builder()
                .sscsCaseData(sscsCaseData)
                .ccdCaseId(123L).evidencePresent("Yes")
                .state(State.APPEAL_CREATED)
                .build();

        given(airLookupService.lookupAirVenueNameByPostCode(anyString(), eq(sscsCaseData.getAppeal().getBenefitType()))).willReturn("Bromley");
    }

    @Test
    public void mapsAppealToRoboticsJson() {
        String date = LocalDate.now().toString();
        roboticsWrapper.getSscsCaseData().setDwpResponseDate(date);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        roboticsJsonValidator.validate(roboticsJson, caseId);

        assertEquals(
                "If this fails, add an assertion below, do not just increment the number :)", 25,
                roboticsJson.length()
        );

        assertEquals("002DD", roboticsJson.get("caseCode"));
        assertEquals(123L, roboticsJson.get("caseId"));
        assertEquals("AB 22 55 66 B", roboticsJson.get("appellantNino"));
        assertEquals("Bromley", roboticsJson.get("appellantPostCode"));
        assertEquals(date, roboticsJson.get("appealDate"));
        assertEquals("2018-06-29", roboticsJson.get("mrnDate"));
        assertEquals("Lost my paperwork", roboticsJson.get("mrnReasonForBeingLate"));
        assertEquals("DWP PIP (1)", roboticsJson.get("pipNumber"));
        assertEquals("Oral", roboticsJson.get("hearingType"));
        assertEquals("Mr User Test", roboticsJson.get("hearingRequestParty"));
        assertEquals("Yes", roboticsJson.get("evidencePresent"));
        assertEquals("Online", roboticsJson.get("receivedVia"));
        assertTrue(roboticsJson.has("rpcEmail"));
        assertTrue(roboticsJson.has("isReadyToList"));
        assertEquals(date, roboticsJson.get("dwpResponseDate"));
        assertEquals("DWP PIP (1)", roboticsJson.get("dwpIssuingOffice"));
        assertEquals("DWP PIP (1)", roboticsJson.get("dwpPresentingOffice"));
        assertEquals("No", roboticsJson.get("dwpIsOfficerAttending"));
        assertEquals("No", roboticsJson.get("dwpUcb"));
        assertEquals("Yes", roboticsJson.get("wantsHearingTypeTelephone"));

        assertEquals(
                "If this fails, add an assertion below, do not just increment the number :)", 11,
                roboticsJson.getJSONObject("appointee").length()
        );

        assertEquals("Mrs", roboticsJson.getJSONObject("appointee").get("title"));
        assertEquals("April", roboticsJson.getJSONObject("appointee").get("firstName"));
        assertEquals("Appointer", roboticsJson.getJSONObject("appointee").get("lastName"));
        assertEquals("No", roboticsJson.getJSONObject("appointee").get("sameAddressAsAppellant"));
        assertEquals("Apton", roboticsJson.getJSONObject("appointee").get("townOrCity"));
        assertEquals("07700 900555", roboticsJson.getJSONObject("appointee").get("phoneNumber"));
        assertEquals("Appshire", roboticsJson.getJSONObject("appointee").get("county"));
        assertEquals("42 Appointed Mews", roboticsJson.getJSONObject("appointee").get("addressLine1"));
        assertEquals("Apford", roboticsJson.getJSONObject("appointee").get("addressLine2"));
        assertEquals("AP12 4PA", roboticsJson.getJSONObject("appointee").get("postCode"));
        assertEquals("appointee@hmcts.net", roboticsJson.getJSONObject("appointee").get("email"));

        assertEquals(
                "If this fails, add an assertion below, do not just increment the number :)", 10,
                roboticsJson.getJSONObject("appellant").length()
        );

        assertEquals("Mr", roboticsJson.getJSONObject("appellant").get("title"));
        assertEquals("User", roboticsJson.getJSONObject("appellant").get("firstName"));
        assertEquals("Test", roboticsJson.getJSONObject("appellant").get("lastName"));
        assertEquals("123 Hairy Lane", roboticsJson.getJSONObject("appellant").get("addressLine1"));
        assertEquals("Off Hairy Park", roboticsJson.getJSONObject("appellant").get("addressLine2"));
        assertEquals("Hairyfield", roboticsJson.getJSONObject("appellant").get("townOrCity"));
        assertEquals("Kent", roboticsJson.getJSONObject("appellant").get("county"));
        assertEquals("TN32 6PL", roboticsJson.getJSONObject("appellant").get("postCode"));
        assertEquals("01234567890", roboticsJson.getJSONObject("appellant").get("phoneNumber"));
        assertEquals("mail@email.com", roboticsJson.getJSONObject("appellant").get("email"));

        assertEquals(
                "If this fails, add an assertion below, do not just increment the number :)", 11,
                roboticsJson.getJSONObject("appointee").length()
        );

        assertEquals("Mrs", roboticsJson.getJSONObject("appointee").get("title"));
        assertEquals("April", roboticsJson.getJSONObject("appointee").get("firstName"));
        assertEquals("Appointer", roboticsJson.getJSONObject("appointee").get("lastName"));
        assertEquals("No", roboticsJson.getJSONObject("appointee").get("sameAddressAsAppellant"));
        assertEquals("42 Appointed Mews", roboticsJson.getJSONObject("appointee").get("addressLine1"));
        assertEquals("Apford", roboticsJson.getJSONObject("appointee").get("addressLine2"));
        assertEquals("Apton", roboticsJson.getJSONObject("appointee").get("townOrCity"));
        assertEquals("Appshire", roboticsJson.getJSONObject("appointee").get("county"));
        assertEquals("AP12 4PA", roboticsJson.getJSONObject("appointee").get("postCode"));
        assertEquals("07700 900555", roboticsJson.getJSONObject("appointee").get("phoneNumber"));
        assertEquals("appointee@hmcts.net", roboticsJson.getJSONObject("appointee").get("email"));

        assertEquals(
                "If this fails, add an assertion, do not just increment the number :)", 11,
                roboticsJson.getJSONObject("representative").length()
        );

        assertEquals("Mrs", roboticsJson.getJSONObject("representative").get("title"));
        assertEquals("Wendy", roboticsJson.getJSONObject("representative").get("firstName"));
        assertEquals("Giles", roboticsJson.getJSONObject("representative").get("lastName"));
        assertEquals("HP Ltd", roboticsJson.getJSONObject("representative").get("organisation"));
        assertEquals("123 Hairy Lane", roboticsJson.getJSONObject("representative").get("addressLine1"));
        assertEquals("Off Hairy Park", roboticsJson.getJSONObject("representative").get("addressLine2"));
        assertEquals("Hairyfield", roboticsJson.getJSONObject("representative").get("townOrCity"));
        assertEquals("Kent", roboticsJson.getJSONObject("representative").get("county"));
        assertEquals("TN32 6PL", roboticsJson.getJSONObject("representative").get("postCode"));
        assertEquals("01234567890", roboticsJson.getJSONObject("representative").get("phoneNumber"));
        assertEquals("mail@email.com", roboticsJson.getJSONObject("representative").get("email"));

        assertEquals(
                "If this fails, add an assertion below, do not just increment the number :)", 6,
                roboticsJson.getJSONObject("hearingArrangements").length()
        );

        assertEquals("Spanish", roboticsJson.getJSONObject("hearingArrangements").get("languageInterpreter"));
        assertEquals("A sign language", roboticsJson.getJSONObject("hearingArrangements").get("signLanguageInterpreter"));
        assertEquals("Yes", roboticsJson.getJSONObject("hearingArrangements").get("hearingLoop"));
        assertEquals("No", roboticsJson.getJSONObject("hearingArrangements").get("accessibleHearingRoom"));
        assertEquals("Yes, this...", roboticsJson.getJSONObject("hearingArrangements").get("other"));
        assertEquals(3, roboticsJson.getJSONObject("hearingArrangements").getJSONArray("datesCantAttend").length());
        assertEquals("2018-06-30", roboticsJson.getJSONObject("hearingArrangements").getJSONArray("datesCantAttend").get(0));
        assertEquals("2018-07-30", roboticsJson.getJSONObject("hearingArrangements").getJSONArray("datesCantAttend").get(1));
        assertEquals("2018-08-30", roboticsJson.getJSONObject("hearingArrangements").getJSONArray("datesCantAttend").get(2));
        assertFalse(roboticsJson.has("isConfidential"));
    }

    @Test
    public void mapRepTitleToDefaultValuesWhenSetToNull() {
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().getName().setTitle(null);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertEquals("s/m", roboticsJson.getJSONObject("representative").get("title"));
    }

    @Test
    public void mapRepTitleToDefaultValuesWhenSetToBlank() {
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().getName().setTitle(null);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertEquals("s/m", roboticsJson.getJSONObject("representative").get("title"));
    }

    @Test
    @Parameters({"PIP, 002DD", "ESA, 051DD", "null, 002DD", ", 002DD", "DLA, 037DD"})
    public void givenBenefitType_shouldMapCaseCodeAccordingly(String benefitCode, String expectedCaseCode) {
        roboticsWrapper.getSscsCaseData().getAppeal().getBenefitType().setCode(benefitCode);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.get("caseCode"), is(expectedCaseCode));
    }

    @Test
    @Parameters({"051DD, 051DD", "null, 002DD", ", 002DD", "001EE, 001EE", "070DD, 070DD", "037DD, 037DD"})
    public void givenCaseCodeOnCase_shouldSetRetrieveCaseCodeAccordingly(@Nullable String caseCode, String expectedCaseCode) {
        roboticsWrapper.getSscsCaseData().setCaseCode(caseCode);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.get("caseCode"), is(expectedCaseCode));
    }

    @Test
    public void mapRepFirstNameToDefaultValuesWhenSetToNull() {
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().getName().setFirstName(null);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertEquals(".", roboticsJson.getJSONObject("representative").get("firstName"));
    }

    @Test
    public void mapRepFirstNameToDefaultValuesWhenSetToBlank() {
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().getName().setFirstName("");

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertEquals(".", roboticsJson.getJSONObject("representative").get("firstName"));
    }

    @Test
    public void mapRepLastNameToDefaultValuesWhenSetToNull() {
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().getName().setLastName(null);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertEquals(".", roboticsJson.getJSONObject("representative").get("lastName"));
    }

    @Test
    public void mapRepLastNameToDefaultValuesWhenSetToBlank() {
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().getName().setLastName("");

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertEquals(".", roboticsJson.getJSONObject("representative").get("lastName"));
    }

    @Test
    public void skipRepWhenHasRepresentativeIsNull() {
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().setHasRepresentative(null);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertFalse(roboticsJson.has("representative"));
    }

    @Test
    public void givenLanguageInterpreterIsTrue_thenSetToLanguageInterpreterType() {
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setLanguages("My Language");
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setLanguageInterpreter("Yes");

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertEquals("My Language", roboticsJson.getJSONObject("hearingArrangements").get("languageInterpreter"));
    }

    @Test
    public void givenHearingArrangementIsNull_thenSetToExcludeDatesHearingLoopAndAccHearingRoom() {
        DateRange dateRange1 = DateRange.builder()
                .start("2018-06-30")
                .end("2018-06-30")
                .build();
        List<ExcludeDate> excludeDates = new ArrayList<>();
        excludeDates.add(ExcludeDate.builder()
                .value(dateRange1)
                .build());
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setArrangements(null);
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setExcludeDates(excludeDates);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertEquals("No", roboticsJson.getJSONObject("hearingArrangements").get("hearingLoop"));
        assertEquals("No", roboticsJson.getJSONObject("hearingArrangements").get("accessibleHearingRoom"));
        assertEquals("2018-06-30", roboticsJson.getJSONObject("hearingArrangements").getJSONArray("datesCantAttend").get(0));
    }

    @Test
    public void givenHearingArrangementIsNull_whenEmptyExcludedDate() {
        DateRange dateRange1 = DateRange.builder()
                .start("2018-06-30")
                .end("2018-06-30")
                .build();
        DateRange dateRange2 = DateRange.builder()
                .start("")
                .end("")
                .build();
        List<ExcludeDate> excludeDates = new ArrayList<>();
        excludeDates.add(ExcludeDate.builder()
                .value(dateRange1)
                .build());
        excludeDates.add(ExcludeDate.builder()
                .value(dateRange2)
                .build());
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setArrangements(null);
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setExcludeDates(excludeDates);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertEquals("No", roboticsJson.getJSONObject("hearingArrangements").get("hearingLoop"));
        assertEquals("No", roboticsJson.getJSONObject("hearingArrangements").get("accessibleHearingRoom"));
        assertEquals(1, roboticsJson.getJSONObject("hearingArrangements").getJSONArray("datesCantAttend").length());
        assertEquals("2018-06-30", roboticsJson.getJSONObject("hearingArrangements").getJSONArray("datesCantAttend").get(0));
    }

    @Test
    public void givenLanguageInterpreterIsFalse_thenDoNotSetLanguageInterpreter() {
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setLanguages("My Language");
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setLanguageInterpreter("No");

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertFalse(roboticsJson.getJSONObject("hearingArrangements").has("languageInterpreter"));
    }

    @Test
    public void givenLanguageInterpreterIsTrueAndInterpreterLanguageTypeIsNull_thenDoNotSetLanguageInterpreter() {
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setLanguages(null);
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setLanguageInterpreter("Yes");

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertFalse(roboticsJson.getJSONObject("hearingArrangements").has("languageInterpreter"));
    }

    @Test
    public void givenSignLanguageInterpreterIsTrue_thenSetToSignLanguageInterpreterType() {
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setSignLanguageType("My Language");
        List<String> arrangements = new ArrayList<>();
        arrangements.add("signLanguageInterpreter");
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setArrangements(arrangements);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertEquals("My Language", roboticsJson.getJSONObject("hearingArrangements").get("signLanguageInterpreter"));
    }

    @Test
    public void givenSignLanguageInterpreterIsFalse_thenDoNotSetSignLanguageInterpreter() {
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setSignLanguageType("My Language");
        List<String> arrangements = new ArrayList<>();
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setArrangements(arrangements);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertFalse(roboticsJson.getJSONObject("hearingArrangements").has("signLanguageInterpreter"));
    }

    @Test
    public void givenSignLanguageInterpreterIsTrueAndSignInterpreterLanguageTypeIsNull_thenDoNotSetSignLanguageInterpreter() {
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setSignLanguageType(null);
        List<String> arrangements = new ArrayList<>();
        arrangements.add("signLanguageInterpreter");
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setArrangements(arrangements);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertFalse(roboticsJson.getJSONObject("hearingArrangements").has("signLanguageInterpreter"));
    }

    @Test
    public void givenCcdCaseIdIsNull_thenDoNotSetCcdCaseId() {
        roboticsWrapper.setCcdCaseId(null);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        roboticsJsonValidator.validate(roboticsJson, caseId);

        assertFalse(roboticsJson.has("caseId"));
    }

    @Test
    public void givenAMissingRepresentative_thenProcessRobotics() {
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().setAddress(null);
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().setName(null);
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().setOrganisation(null);
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().setHasRepresentative("No");

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertFalse(roboticsJson.has("representative"));
    }

    @Test
    public void givenAnAppointee_thenProcessRoboticsWithAppellantPostCodeSetToVenueForAppointeePostcode() {
        given(airLookupService.lookupAirVenueNameByPostCode("TS1ABC", BenefitType.builder().code("PIP").build())).willReturn("Pip venue");

        Name appointeeName = Name.builder().title("Mrs").firstName("Ap").lastName("Pointee").build();
        Address address = roboticsWrapper.getSscsCaseData().getAppeal().getAppellant().getAddress();
        address.setPostcode("TS1ABC");

        Appointee appointee = Appointee.builder()
                .name(appointeeName)
                .address(address)
                .contact(roboticsWrapper.getSscsCaseData().getAppeal().getAppellant().getContact())
                .build();

        roboticsWrapper.getSscsCaseData().getAppeal().getAppellant().setIsAddressSameAsAppointee("Yes");

        roboticsWrapper.getSscsCaseData().getAppeal().getAppellant().setAppointee(appointee);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertTrue(roboticsJson.has("appointee"));
        assertEquals("Yes", roboticsJson.getJSONObject("appointee").getString("sameAddressAsAppellant"));
        assertEquals("Pip venue", roboticsJson.get("appellantPostCode"));
    }

    @Test
    public void findVenueHandleMissingFields() {
        roboticsWrapper.getSscsCaseData().getAppeal().setBenefitType(null);
        Optional<String> venue = roboticsJsonMapper.findVenueName(roboticsWrapper.getSscsCaseData());
        assertTrue(venue.isEmpty());
    }

    @Test
    public void findVenueHandleValidFields() {
        Optional<String> venue = roboticsJsonMapper.findVenueName(roboticsWrapper.getSscsCaseData());
        assertTrue(venue.isPresent());
    }

    @Test
    public void givenAnAppointeeWithEmptyDetails_thenProcessRobotics() {
        Name appointeeName = Name.builder().title(null).firstName(null).lastName(null).build();

        Appointee appointee = Appointee.builder()
                .name(appointeeName)
                .address(Address.builder().line1(null).line2(null).town(null).county(null).postcode(null).build())
                .contact(Contact.builder().email(null).phone(null).mobile(null).build())
                .identity(Identity.builder().dob(null).nino(null).build())
                .build();

        roboticsWrapper.getSscsCaseData().getAppeal().getAppellant().setAppointee(appointee);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertFalse(roboticsJson.has("appointee"));
    }


    @Test
    public void givenNoAppointee_thenProcessRobotics() {
        roboticsWrapper.getSscsCaseData().getAppeal().getAppellant().setAppointee(null);
        roboticsWrapper.getSscsCaseData().getAppeal().getAppellant().setIsAppointee("No");

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertFalse(roboticsJson.has("appointee"));
    }

    @Test
    public void givenCaseCreatedDate_thenAppealDateShouldGetUpdated() {
        String caseCreatedDate = "2019-08-01";

        roboticsWrapper.getSscsCaseData().setCaseCreated(caseCreatedDate);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertTrue(roboticsJson.has("appealDate"));

        assertEquals(caseCreatedDate, roboticsJson.get("appealDate"));
    }

    @Test
    public void givenCaseCreatedDateIsNull_thenAppealDateShouldBeCurrentDate() {
        roboticsWrapper.getSscsCaseData().setCaseCreated(null);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertTrue(roboticsJson.has("appealDate"));

        assertEquals(LocalDate.now().toString(), roboticsJson.get("appealDate"));
    }

    @Test
    public void givenStateIsAppealCreated_thenSetReadyToListFieldToNo() {

        roboticsWrapper.setState(State.APPEAL_CREATED);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertTrue(roboticsJson.has("isReadyToList"));
        assertEquals("No", roboticsJson.get("isReadyToList"));
    }

    @Test
    public void givenStateIsReadyToList_thenSetReadyToListFields() {
        DynamicListItem value = new DynamicListItem("DWP PIP (AE)", "DWP PIP (AE)");

        roboticsWrapper.setState(State.READY_TO_LIST);
        roboticsWrapper.getSscsCaseData().setDwpOriginatingOffice(new DynamicList(value, Collections.singletonList(value)));
        roboticsWrapper.getSscsCaseData().setDwpPresentingOffice(new DynamicList(value, Collections.singletonList(value)));
        roboticsWrapper.getSscsCaseData().setDwpIsOfficerAttending("Yes");
        roboticsWrapper.getSscsCaseData().setDwpUcb("Yes");

        String date = LocalDate.now().toString();
        roboticsWrapper.getSscsCaseData().setDwpResponseDate(date);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertTrue(roboticsJson.has("isReadyToList"));
        assertEquals(date, roboticsJson.get("dwpResponseDate"));
        assertEquals("PIP (AE)", roboticsJson.get("dwpIssuingOffice"));
        assertEquals("PIP (AE)", roboticsJson.get("dwpPresentingOffice"));
        assertEquals("Yes", roboticsJson.get("dwpIsOfficerAttending"));
        assertEquals("Yes", roboticsJson.get("dwpUcb"));
    }

    @Test
    public void givenStateIsReadyToListAndDwpOfficesAreNotSet_thenSetDefaultReadyToListFields() {
        String date = LocalDate.now().toString();
        roboticsWrapper.getSscsCaseData().setDwpResponseDate(date);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertTrue(roboticsJson.has("isReadyToList"));
        assertEquals(date, roboticsJson.get("dwpResponseDate"));
        assertEquals("DWP PIP (1)", roboticsJson.get("dwpIssuingOffice"));
        assertEquals("DWP PIP (1)", roboticsJson.get("dwpPresentingOffice"));
        assertEquals("No", roboticsJson.get("dwpIsOfficerAttending"));
        assertEquals("No", roboticsJson.get("dwpUcb"));
    }

    @Test
    @Parameters({"validAppeal, No", "readyToList, Yes", "null, No"})
    public void givenCreatedInGapsFrom_shouldSetRetrieveIsDigitalAccordingly(@Nullable String createdInGapsFrom, String expectedIsDigital) {
        roboticsWrapper.getSscsCaseData().setCreatedInGapsFrom(createdInGapsFrom);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.get("isDigital"), is(expectedIsDigital));
    }

    @Test
    public void shouldPopulateRoboticsWithUcFields() {
        initialiseElementDisputedLists();

        roboticsWrapper.getSscsCaseData().setJointParty("Yes");
        roboticsWrapper.getSscsCaseData().setJointPartyName(JointPartyName.builder().title("Mr").firstName("Harry").lastName("Hotspur").build());
        roboticsWrapper.getSscsCaseData().setJointPartyAddress(Address.builder().line1("The road").line2("Test").town("Bedrock").county("Bedfordshire").postcode("BD1 5LK").build());
        roboticsWrapper.getSscsCaseData().setJointPartyAddressSameAsAppellant("No");
        roboticsWrapper.getSscsCaseData().setElementsDisputedIsDecisionDisputedByOthers("Yes");
        roboticsWrapper.getSscsCaseData().setElementsDisputedLinkedAppealRef("12345678");
        roboticsWrapper.getSscsCaseData().setJointPartyIdentity(Identity.builder().nino("JT000000B").dob("2000-01-01").build());
        roboticsWrapper.getSscsCaseData().getAppeal().setHearingSubtype(HearingSubtype.builder()
                .wantsHearingTypeTelephone("Yes").hearingTelephoneNumber("07999888000").wantsHearingTypeVideo("Yes").hearingVideoEmail("m@test.com").wantsHearingTypeFaceToFace("No").build());

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertTrue(roboticsJson.has("jointParty"));
        assertEquals("No", roboticsJson.getJSONObject("jointParty").getString("sameAddressAsAppellant"));
        assertEquals("Mr", roboticsJson.getJSONObject("jointParty").get("title"));
        assertEquals("Harry", roboticsJson.getJSONObject("jointParty").get("firstName"));
        assertEquals("Hotspur", roboticsJson.getJSONObject("jointParty").get("lastName"));
        assertEquals("Bedrock", roboticsJson.getJSONObject("jointParty").get("townOrCity"));
        assertEquals("Bedfordshire", roboticsJson.getJSONObject("jointParty").get("county"));
        assertEquals("The road", roboticsJson.getJSONObject("jointParty").get("addressLine1"));
        assertEquals("Test", roboticsJson.getJSONObject("jointParty").get("addressLine2"));
        assertEquals("BD1 5LK", roboticsJson.getJSONObject("jointParty").get("postCode"));
        assertEquals("JT000000B", roboticsJson.getJSONObject("jointParty").get("nino"));
        assertEquals("2000-01-01", roboticsJson.getJSONObject("jointParty").get("dob"));

        assertEquals("firstIssueElementsDisputedGeneral", roboticsJson.getJSONObject("elementsDisputed").getJSONArray("general").get(0));
        assertEquals("firstIssueElementsDisputedSanctions", roboticsJson.getJSONObject("elementsDisputed").getJSONArray("sanctions").get(0));
        assertEquals("firstIssueElementsDisputedOverpayment", roboticsJson.getJSONObject("elementsDisputed").getJSONArray("overpayment").get(0));
        assertEquals("firstIssueElementsDisputedHousing", roboticsJson.getJSONObject("elementsDisputed").getJSONArray("housing").get(0));
        assertEquals("firstIssueElementsDisputedChildCare", roboticsJson.getJSONObject("elementsDisputed").getJSONArray("childCare").get(0));
        assertEquals("firstIssueElementsDisputedCare", roboticsJson.getJSONObject("elementsDisputed").getJSONArray("care").get(0));
        assertEquals("firstIssueElementsDisputedChildElement", roboticsJson.getJSONObject("elementsDisputed").getJSONArray("childElement").get(0));
        assertEquals("firstIssueElementsDisputedChildDisabled", roboticsJson.getJSONObject("elementsDisputed").getJSONArray("childDisabled").get(0));

        assertEquals("Yes", roboticsJson.get("ucDecisionDisputedByOthers"));
        assertEquals("Yes", roboticsJson.get("wantsHearingTypeTelephone"));
        assertEquals("Yes", roboticsJson.get("wantsHearingTypeVideo"));
        assertEquals("No", roboticsJson.get("wantsHearingTypeFaceToFace"));
    }

    @Test
    public void shouldPopulateRoboticsWithUcFieldsSameAddress() {
        initialiseElementDisputedLists();

        roboticsWrapper.getSscsCaseData().setJointParty("Yes");
        roboticsWrapper.getSscsCaseData().setJointPartyName(JointPartyName.builder().title("Mr").firstName("Harry").lastName("Hotspur").build());
        roboticsWrapper.getSscsCaseData().setJointPartyAddress(Address.builder().line1("The road").line2("Test").town("Bedrock").county("Bedfordshire").postcode("BD1 5LK").build());
        roboticsWrapper.getSscsCaseData().setJointPartyAddressSameAsAppellant("Yes");
        roboticsWrapper.getSscsCaseData().setElementsDisputedIsDecisionDisputedByOthers("Yes");
        roboticsWrapper.getSscsCaseData().setElementsDisputedLinkedAppealRef("12345678");
        roboticsWrapper.getSscsCaseData().setJointPartyIdentity(Identity.builder().nino("JT000000B").dob("2000-01-01").build());
        roboticsWrapper.getSscsCaseData().getAppeal().setHearingSubtype(HearingSubtype.builder()
                .wantsHearingTypeTelephone("Yes").hearingTelephoneNumber("07999888000").wantsHearingTypeVideo("Yes").hearingVideoEmail("m@test.com").wantsHearingTypeFaceToFace("No").build());

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertTrue(roboticsJson.has("jointParty"));
        assertEquals("Yes", roboticsJson.getJSONObject("jointParty").getString("sameAddressAsAppellant"));
        assertEquals("Mr", roboticsJson.getJSONObject("jointParty").get("title"));
        assertEquals("Harry", roboticsJson.getJSONObject("jointParty").get("firstName"));
        assertEquals("Hotspur", roboticsJson.getJSONObject("jointParty").get("lastName"));
        assertEquals("123 Hairy Lane", roboticsJson.getJSONObject("appellant").get("addressLine1"));
        assertEquals("Off Hairy Park", roboticsJson.getJSONObject("appellant").get("addressLine2"));
        assertEquals("Hairyfield", roboticsJson.getJSONObject("appellant").get("townOrCity"));
        assertEquals("Kent", roboticsJson.getJSONObject("appellant").get("county"));
        assertEquals("TN32 6PL", roboticsJson.getJSONObject("appellant").get("postCode"));
        assertEquals("JT000000B", roboticsJson.getJSONObject("jointParty").get("nino"));
        assertEquals("2000-01-01", roboticsJson.getJSONObject("jointParty").get("dob"));

        assertEquals("firstIssueElementsDisputedGeneral", roboticsJson.getJSONObject("elementsDisputed").getJSONArray("general").get(0));
        assertEquals("firstIssueElementsDisputedSanctions", roboticsJson.getJSONObject("elementsDisputed").getJSONArray("sanctions").get(0));
        assertEquals("firstIssueElementsDisputedOverpayment", roboticsJson.getJSONObject("elementsDisputed").getJSONArray("overpayment").get(0));
        assertEquals("firstIssueElementsDisputedHousing", roboticsJson.getJSONObject("elementsDisputed").getJSONArray("housing").get(0));
        assertEquals("firstIssueElementsDisputedChildCare", roboticsJson.getJSONObject("elementsDisputed").getJSONArray("childCare").get(0));
        assertEquals("firstIssueElementsDisputedCare", roboticsJson.getJSONObject("elementsDisputed").getJSONArray("care").get(0));
        assertEquals("firstIssueElementsDisputedChildElement", roboticsJson.getJSONObject("elementsDisputed").getJSONArray("childElement").get(0));
        assertEquals("firstIssueElementsDisputedChildDisabled", roboticsJson.getJSONObject("elementsDisputed").getJSONArray("childDisabled").get(0));

        assertEquals("Yes", roboticsJson.get("ucDecisionDisputedByOthers"));
        assertEquals("12345678", roboticsJson.get("linkedAppealRef"));
        assertEquals("Yes", roboticsJson.get("wantsHearingTypeTelephone"));
        assertEquals("Yes", roboticsJson.get("wantsHearingTypeVideo"));
        assertEquals("No", roboticsJson.get("wantsHearingTypeFaceToFace"));
    }

    @Test
    public void givenElementContainsMultipleIssues_thenCheckMultipleIssuesTransformedToArray() {
        elementsDisputedGeneralList.addAll(Arrays.asList(
                ElementDisputed.builder().value(ElementDisputedDetails.builder().issueCode("firstIssueElementsDisputedGeneral").build()).build(),
                ElementDisputed.builder().value(ElementDisputedDetails.builder().issueCode("secondIssueElementsDisputedGeneral").build()).build(),
                ElementDisputed.builder().value(ElementDisputedDetails.builder().issueCode("thirdIssueElementsDisputedGeneral").build()).build()));
        roboticsWrapper.getSscsCaseData().setElementsDisputedGeneral(elementsDisputedGeneralList);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertEquals("firstIssueElementsDisputedGeneral", roboticsJson.getJSONObject("elementsDisputed").getJSONArray("general").get(0));
        assertEquals("secondIssueElementsDisputedGeneral", roboticsJson.getJSONObject("elementsDisputed").getJSONArray("general").get(1));
        assertEquals("thirdIssueElementsDisputedGeneral", roboticsJson.getJSONObject("elementsDisputed").getJSONArray("general").get(2));
    }

    private void initialiseElementDisputedLists() {
        elementsDisputedGeneralList.add(ElementDisputed.builder().value(ElementDisputedDetails.builder().issueCode("firstIssueElementsDisputedGeneral").build()).build());
        elementsDisputedSanctionsList.add(ElementDisputed.builder().value(ElementDisputedDetails.builder().issueCode("firstIssueElementsDisputedSanctions").build()).build());
        elementsDisputedOverpaymentList.add(ElementDisputed.builder().value(ElementDisputedDetails.builder().issueCode("firstIssueElementsDisputedOverpayment").build()).build());
        elementsDisputedHousingList.add(ElementDisputed.builder().value(ElementDisputedDetails.builder().issueCode("firstIssueElementsDisputedHousing").build()).build());
        elementsDisputedChildCareList.add(ElementDisputed.builder().value(ElementDisputedDetails.builder().issueCode("firstIssueElementsDisputedChildCare").build()).build());
        elementsDisputedCareList.add(ElementDisputed.builder().value(ElementDisputedDetails.builder().issueCode("firstIssueElementsDisputedCare").build()).build());
        elementsDisputedChildElementList.add(ElementDisputed.builder().value(ElementDisputedDetails.builder().issueCode("firstIssueElementsDisputedChildElement").build()).build());
        elementsDisputedChildDisabledList.add(ElementDisputed.builder().value(ElementDisputedDetails.builder().issueCode("firstIssueElementsDisputedChildDisabled").build()).build());

        roboticsWrapper.getSscsCaseData().setElementsDisputedGeneral(elementsDisputedGeneralList);
        roboticsWrapper.getSscsCaseData().setElementsDisputedSanctions(elementsDisputedSanctionsList);
        roboticsWrapper.getSscsCaseData().setElementsDisputedOverpayment(elementsDisputedOverpaymentList);
        roboticsWrapper.getSscsCaseData().setElementsDisputedHousing(elementsDisputedHousingList);
        roboticsWrapper.getSscsCaseData().setElementsDisputedChildCare(elementsDisputedChildCareList);
        roboticsWrapper.getSscsCaseData().setElementsDisputedCare(elementsDisputedCareList);
        roboticsWrapper.getSscsCaseData().setElementsDisputedChildElement(elementsDisputedChildElementList);
        roboticsWrapper.getSscsCaseData().setElementsDisputedChildDisabled(elementsDisputedChildDisabledList);
    }

    @Test
    public void givenConfidentialityRequestOutcomeGrantedForAppellant_thenSetIsConfidentialFlag() {
        roboticsWrapper.setState(State.RESPONSE_RECEIVED);
        roboticsWrapper.getSscsCaseData().setConfidentialityRequestOutcomeAppellant(DatedRequestOutcome.builder().requestOutcome(RequestOutcome.GRANTED).build());
        roboticsWrapper.getSscsCaseData().setConfidentialityRequestOutcomeJointParty(null);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertEquals("Yes", roboticsJson.get("isConfidential"));
    }

    @Test
    public void givenConfidentialityRequestOutcomeGrantedForJointParty_thenSetIsConfidentialFlag() {
        roboticsWrapper.setState(State.RESPONSE_RECEIVED);
        roboticsWrapper.getSscsCaseData().setConfidentialityRequestOutcomeJointParty(DatedRequestOutcome.builder().requestOutcome(RequestOutcome.GRANTED).build());
        roboticsWrapper.getSscsCaseData().setConfidentialityRequestOutcomeAppellant(null);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertEquals("Yes", roboticsJson.get("isConfidential"));
    }

    @Test
    public void givenConfidentialityRequestOutcomeGrantedButStateNotResponseReceived_thenDoNotSetIsConfidentialFlag() {
        roboticsWrapper.setState(State.WITH_DWP);
        roboticsWrapper.getSscsCaseData().setConfidentialityRequestOutcomeAppellant(DatedRequestOutcome.builder().requestOutcome(RequestOutcome.GRANTED).build());
        roboticsWrapper.getSscsCaseData().setConfidentialityRequestOutcomeJointParty(DatedRequestOutcome.builder().requestOutcome(RequestOutcome.GRANTED).build());

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertFalse(roboticsJson.has("isConfidential"));
    }

    @Test
    public void givenCaseInResponseReceivedStateButConfidentialityRequestOutcomeNotGranted_thenDoNotSetIsConfidentialFlag() {
        roboticsWrapper.setState(State.RESPONSE_RECEIVED);
        roboticsWrapper.getSscsCaseData().setConfidentialityRequestOutcomeAppellant(DatedRequestOutcome.builder().requestOutcome(RequestOutcome.REFUSED).build());
        roboticsWrapper.getSscsCaseData().setConfidentialityRequestOutcomeJointParty(DatedRequestOutcome.builder().requestOutcome(RequestOutcome.REFUSED).build());

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertFalse(roboticsJson.has("isConfidential"));
    }

    @Test
    public void givenDlaCase_thenGetsDwpIssueOffice() {

        SscsCaseData sscsCaseData = buildCaseData("Test", "DLA", "Bradford DRT");
        sscsCaseData.getAppeal().getAppellant().setIsAppointee("Yes");
        roboticsWrapper = RoboticsWrapper
                .builder()
                .sscsCaseData(sscsCaseData)
                .ccdCaseId(123L).evidencePresent("Yes")
                .state(State.APPEAL_CREATED)
                .build();

        String date = LocalDate.now().toString();
        roboticsWrapper.getSscsCaseData().setDwpResponseDate(date);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        roboticsJsonValidator.validate(roboticsJson, caseId);

        assertEquals("037DD", roboticsJson.get("caseCode"));
        assertEquals("Bradford DRT", roboticsJson.get("dwpIssuingOffice"));
        assertEquals("Bradford DRT", roboticsJson.get("dwpPresentingOffice"));


    }
}
