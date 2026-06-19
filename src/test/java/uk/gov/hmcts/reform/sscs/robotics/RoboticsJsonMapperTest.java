package uk.gov.hmcts.reform.sscs.robotics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.*;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.NO;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.YES;
import static uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils.buildCaseData;

import java.time.LocalDate;
import java.util.*;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import uk.gov.hmcts.reform.sscs.ccd.domain.*;
import uk.gov.hmcts.reform.sscs.service.AirLookupService;
import uk.gov.hmcts.reform.sscs.service.DwpAddressLookupService;

class RoboticsJsonMapperTest {

    private static final String CASE_ID = "12345678";
    private final RoboticsJsonValidator roboticsJsonValidator = new RoboticsJsonValidator(
        "/schema/sscs-robotics.json");
    private final DwpAddressLookupService dwpAddressLookupService = new DwpAddressLookupService();
    private final List<ElementDisputed> elementsDisputedGeneralList = new ArrayList<>();
    private final List<ElementDisputed> elementsDisputedSanctionsList = new ArrayList<>();
    private final List<ElementDisputed> elementsDisputedOverpaymentList = new ArrayList<>();
    private final List<ElementDisputed> elementsDisputedHousingList = new ArrayList<>();
    private final List<ElementDisputed> elementsDisputedChildCareList = new ArrayList<>();
    private final List<ElementDisputed> elementsDisputedCareList = new ArrayList<>();
    private final List<ElementDisputed> elementsDisputedChildElementList = new ArrayList<>();
    private final List<ElementDisputed> elementsDisputedChildDisabledList = new ArrayList<>();
    private final List<ElementDisputed> elementsDisputedLimitedWorkList = new ArrayList<>();
    private RoboticsJsonMapper roboticsJsonMapper;
    private RoboticsWrapper roboticsWrapper;
    private JSONObject roboticsJson;
    @Mock
    private AirLookupService airLookupService;

    @BeforeEach
    void setup() {
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

        given(
            airLookupService.lookupAirVenueNameByPostCode(anyString(), eq(sscsCaseData.getAppeal().getBenefitType()))).willReturn(
            "Bromley");
    }

    @Test
    void mapsAppealToRoboticsJson() {
        String date = LocalDate.now().toString();
        roboticsWrapper.getSscsCaseData().setDwpResponseDate(date);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        roboticsJsonValidator.validate(roboticsJson, CASE_ID);

        assertThat(roboticsJson.length())
            .as("If this fails, add an assertion below, do not just increment the number :)")
            .isEqualTo(25);

        assertThat(roboticsJson.get("caseCode")).isEqualTo("002DD");
        assertThat(roboticsJson.get("caseId")).isEqualTo(123L);
        assertThat(roboticsJson.get("appellantNino")).isEqualTo("AB 22 55 66 B");
        assertThat(roboticsJson.get("appellantPostCode")).isEqualTo("Bromley");
        assertThat(roboticsJson.get("appealDate")).isEqualTo(date);
        assertThat(roboticsJson.get("mrnDate")).isEqualTo("2018-06-29");
        assertThat(roboticsJson.get("mrnReasonForBeingLate")).isEqualTo("Lost my paperwork");
        assertThat(roboticsJson.get("pipNumber")).isEqualTo("DWP PIP (1)");
        assertThat(roboticsJson.get("hearingType")).isEqualTo("Oral");
        assertThat(roboticsJson.get("hearingRequestParty")).isEqualTo("Mr User Test");
        assertThat(roboticsJson.get("evidencePresent")).isEqualTo("Yes");
        assertThat(roboticsJson.get("receivedVia")).isEqualTo("Online");
        assertThat(roboticsJson.has("rpcEmail")).isTrue();
        assertThat(roboticsJson.has("isReadyToList")).isTrue();
        assertThat(roboticsJson.get("dwpResponseDate")).isEqualTo(date);
        assertThat(roboticsJson.get("dwpIssuingOffice")).isEqualTo("DWP PIP (1)");
        assertThat(roboticsJson.get("dwpPresentingOffice")).isEqualTo("DWP PIP (1)");
        assertThat(roboticsJson.get("dwpIsOfficerAttending")).isEqualTo("No");
        assertThat(roboticsJson.get("dwpUcb")).isEqualTo("No");
        assertThat(roboticsJson.get("wantsHearingTypeTelephone")).isEqualTo("Yes");

        assertThat(roboticsJson.getJSONObject("appointee").length())
            .as("If this fails, add an assertion below, do not just increment the number :)")
            .isEqualTo(11);

        assertThat(roboticsJson.getJSONObject("appointee").get("title")).isEqualTo("Mrs");
        assertThat(roboticsJson.getJSONObject("appointee").get("firstName")).isEqualTo("April");
        assertThat(roboticsJson.getJSONObject("appointee").get("lastName")).isEqualTo("Appointer");
        assertThat(roboticsJson.getJSONObject("appointee").get("sameAddressAsAppellant")).isEqualTo("No");
        assertThat(roboticsJson.getJSONObject("appointee").get("townOrCity")).isEqualTo("Apton");
        assertThat(roboticsJson.getJSONObject("appointee").get("phoneNumber")).isEqualTo("07700 900555");
        assertThat(roboticsJson.getJSONObject("appointee").get("county")).isEqualTo("Appshire");
        assertThat(roboticsJson.getJSONObject("appointee").get("addressLine1")).isEqualTo("42 Appointed Mews");
        assertThat(roboticsJson.getJSONObject("appointee").get("addressLine2")).isEqualTo("Apford");
        assertThat(roboticsJson.getJSONObject("appointee").get("postCode")).isEqualTo("AP12 4PA");
        assertThat(roboticsJson.getJSONObject("appointee").get("email")).isEqualTo("appointee@hmcts.net");

        assertThat(roboticsJson.getJSONObject("appellant").length())
            .as("If this fails, add an assertion below, do not just increment the number :)")
            .isEqualTo(10);

        assertThat(roboticsJson.getJSONObject("appellant").get("title")).isEqualTo("Mr");
        assertThat(roboticsJson.getJSONObject("appellant").get("firstName")).isEqualTo("User");
        assertThat(roboticsJson.getJSONObject("appellant").get("lastName")).isEqualTo("Test");
        assertThat(roboticsJson.getJSONObject("appellant").get("addressLine1")).isEqualTo("123 Hairy Lane");
        assertThat(roboticsJson.getJSONObject("appellant").get("addressLine2")).isEqualTo("Off Hairy Park");
        assertThat(roboticsJson.getJSONObject("appellant").get("townOrCity")).isEqualTo("Hairyfield");
        assertThat(roboticsJson.getJSONObject("appellant").get("county")).isEqualTo("Kent");
        assertThat(roboticsJson.getJSONObject("appellant").get("postCode")).isEqualTo("TN32 6PL");
        assertThat(roboticsJson.getJSONObject("appellant").get("phoneNumber")).isEqualTo("01234567890");
        assertThat(roboticsJson.getJSONObject("appellant").get("email")).isEqualTo("mail@email.com");

        assertThat(roboticsJson.getJSONObject("appointee").length())
            .as("If this fails, add an assertion below, do not just increment the number :)")
            .isEqualTo(11);

        assertThat(roboticsJson.getJSONObject("appointee").get("title")).isEqualTo("Mrs");
        assertThat(roboticsJson.getJSONObject("appointee").get("firstName")).isEqualTo("April");
        assertThat(roboticsJson.getJSONObject("appointee").get("lastName")).isEqualTo("Appointer");
        assertThat(roboticsJson.getJSONObject("appointee").get("sameAddressAsAppellant")).isEqualTo("No");
        assertThat(roboticsJson.getJSONObject("appointee").get("addressLine1")).isEqualTo("42 Appointed Mews");
        assertThat(roboticsJson.getJSONObject("appointee").get("addressLine2")).isEqualTo("Apford");
        assertThat(roboticsJson.getJSONObject("appointee").get("townOrCity")).isEqualTo("Apton");
        assertThat(roboticsJson.getJSONObject("appointee").get("county")).isEqualTo("Appshire");
        assertThat(roboticsJson.getJSONObject("appointee").get("postCode")).isEqualTo("AP12 4PA");
        assertThat(roboticsJson.getJSONObject("appointee").get("phoneNumber")).isEqualTo("07700 900555");
        assertThat(roboticsJson.getJSONObject("appointee").get("email")).isEqualTo("appointee@hmcts.net");

        assertThat(roboticsJson.getJSONObject("representative").length())
            .as("If this fails, add an assertion, do not just increment the number :)")
            .isEqualTo(11);

        assertThat(roboticsJson.getJSONObject("representative").get("title")).isEqualTo("Mrs");
        assertThat(roboticsJson.getJSONObject("representative").get("firstName")).isEqualTo("Wendy");
        assertThat(roboticsJson.getJSONObject("representative").get("lastName")).isEqualTo("Giles");
        assertThat(roboticsJson.getJSONObject("representative").get("organisation")).isEqualTo("HP Ltd");
        assertThat(roboticsJson.getJSONObject("representative").get("addressLine1")).isEqualTo("123 Hairy Lane");
        assertThat(roboticsJson.getJSONObject("representative").get("addressLine2")).isEqualTo("Off Hairy Park");
        assertThat(roboticsJson.getJSONObject("representative").get("townOrCity")).isEqualTo("Hairyfield");
        assertThat(roboticsJson.getJSONObject("representative").get("county")).isEqualTo("Kent");
        assertThat(roboticsJson.getJSONObject("representative").get("postCode")).isEqualTo("TN32 6PL");
        assertThat(roboticsJson.getJSONObject("representative").get("phoneNumber")).isEqualTo("01234567890");
        assertThat(roboticsJson.getJSONObject("representative").get("email")).isEqualTo("mail@email.com");

        assertThat(roboticsJson.getJSONObject("hearingArrangements").length())
            .as("If this fails, add an assertion below, do not just increment the number :)")
            .isEqualTo(6);

        assertThat(roboticsJson.getJSONObject("hearingArrangements").get("languageInterpreter")).isEqualTo("Spanish");
        assertThat(roboticsJson.getJSONObject("hearingArrangements").get("signLanguageInterpreter")).isEqualTo("A sign language");
        assertThat(roboticsJson.getJSONObject("hearingArrangements").get("hearingLoop")).isEqualTo("Yes");
        assertThat(roboticsJson.getJSONObject("hearingArrangements").get("accessibleHearingRoom")).isEqualTo("No");
        assertThat(roboticsJson.getJSONObject("hearingArrangements").get("other")).isEqualTo("Yes, this...");
        assertThat(roboticsJson.getJSONObject("hearingArrangements").getJSONArray("datesCantAttend").length()).isEqualTo(3);
        assertThat(roboticsJson.getJSONObject("hearingArrangements").getJSONArray("datesCantAttend").get(0)).isEqualTo(
            "2018-06-30");
        assertThat(roboticsJson.getJSONObject("hearingArrangements").getJSONArray("datesCantAttend").get(1)).isEqualTo(
            "2018-07-30");
        assertThat(roboticsJson.getJSONObject("hearingArrangements").getJSONArray("datesCantAttend").get(2)).isEqualTo(
            "2018-08-30");
        assertThat(roboticsJson.has("isConfidential")).isFalse();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void mapRepTitleToDefaultValuesWhenSetToNull(String title) {
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().getName().setTitle(title);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.getJSONObject("representative").get("title")).isEqualTo("s/m");
    }

    @ParameterizedTest
    @CsvSource({"PIP, 002DD", "ESA, 051DD", "null, 002DD", ", 002DD"})
    void givenBenefitType_shouldMapCaseCodeAccordingly(String benefitCode, String expectedCaseCode) {
        roboticsWrapper.getSscsCaseData().getAppeal().getBenefitType().setCode(benefitCode);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.get("caseCode")).isEqualTo(expectedCaseCode);
    }

    @ParameterizedTest
    @CsvSource({"051DD, 051DD", "null, 002DD", ", 002DD", "001EE, 001EE", "070DD, 070DD", "037DD, 037DD", "022DD, 022DD"})
    void givenCaseCodeOnCase_shouldSetRetrieveCaseCodeAccordingly(String caseCode, String expectedCaseCode) {
        if ("null".equals(caseCode)) {
            caseCode = null;
        }
        roboticsWrapper.getSscsCaseData().setCaseCode(caseCode);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.get("caseCode")).isEqualTo(expectedCaseCode);
    }

    @Test
    void mapRepFirstNameToDefaultValuesWhenSetToNull() {
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().getName().setFirstName(null);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.getJSONObject("representative").get("firstName")).isEqualTo(".");
    }

    @Test
    void mapRepFirstNameToDefaultValuesWhenSetToBlank() {
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().getName().setFirstName("");

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.getJSONObject("representative").get("firstName")).isEqualTo(".");
    }

    @Test
    void mapRepLastNameToDefaultValuesWhenSetToNull() {
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().getName().setLastName(null);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.getJSONObject("representative").get("lastName")).isEqualTo(".");
    }

    @Test
    void mapRepLastNameToDefaultValuesWhenSetToBlank() {
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().getName().setLastName("");

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.getJSONObject("representative").get("lastName")).isEqualTo(".");
    }

    @Test
    void skipRepWhenHasRepresentativeIsNull() {
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().setHasRepresentative(null);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.has("representative")).isFalse();
    }

    @Test
    void givenLanguageInterpreterIsTrue_thenSetToLanguageInterpreterType() {
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setLanguages("My Language");
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setLanguageInterpreter("Yes");

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.getJSONObject("hearingArrangements").get("languageInterpreter")).isEqualTo("My Language");
    }

    @Test
    void givenHearingArrangementIsNull_thenSetToExcludeDatesHearingLoopAndAccHearingRoom() {
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

        assertThat(roboticsJson.getJSONObject("hearingArrangements").get("hearingLoop")).isEqualTo("No");
        assertThat(roboticsJson.getJSONObject("hearingArrangements").get("accessibleHearingRoom")).isEqualTo("No");
        assertThat(roboticsJson.getJSONObject("hearingArrangements").getJSONArray("datesCantAttend").get(0)).isEqualTo(
            "2018-06-30");
    }

    @Test
    void givenHearingArrangementIsNull_whenEmptyExcludedDate() {
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

        assertThat(roboticsJson.getJSONObject("hearingArrangements").get("hearingLoop")).isEqualTo("No");
        assertThat(roboticsJson.getJSONObject("hearingArrangements").get("accessibleHearingRoom")).isEqualTo("No");
        assertThat(roboticsJson.getJSONObject("hearingArrangements").getJSONArray("datesCantAttend").length()).isEqualTo(1);
        assertThat(roboticsJson.getJSONObject("hearingArrangements").getJSONArray("datesCantAttend").get(0)).isEqualTo(
            "2018-06-30");
    }

    @Test
    void givenLanguageInterpreterIsFalse_thenDoNotSetLanguageInterpreter() {
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setLanguages("My Language");
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setLanguageInterpreter("No");

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.getJSONObject("hearingArrangements").has("languageInterpreter")).isFalse();
    }

    @Test
    void givenLanguageInterpreterIsTrueAndInterpreterLanguageTypeIsNull_thenDoNotSetLanguageInterpreter() {
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setLanguages(null);
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setLanguageInterpreter("Yes");

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.getJSONObject("hearingArrangements").has("languageInterpreter")).isFalse();
    }

    @Test
    void givenSignLanguageInterpreterIsTrue_thenSetToSignLanguageInterpreterType() {
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setSignLanguageType("My Language");
        List<String> arrangements = new ArrayList<>();
        arrangements.add("signLanguageInterpreter");
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setArrangements(arrangements);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.getJSONObject("hearingArrangements").get("signLanguageInterpreter")).isEqualTo("My Language");
    }

    @Test
    void givenSignLanguageInterpreterIsFalse_thenDoNotSetSignLanguageInterpreter() {
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setSignLanguageType("My Language");
        List<String> arrangements = new ArrayList<>();
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setArrangements(arrangements);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.getJSONObject("hearingArrangements").has("signLanguageInterpreter")).isFalse();
    }

    @Test
    void givenSignLanguageInterpreterIsTrueAndSignInterpreterLanguageTypeIsNull_thenDoNotSetSignLanguageInterpreter() {
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setSignLanguageType(null);
        List<String> arrangements = new ArrayList<>();
        arrangements.add("signLanguageInterpreter");
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setArrangements(arrangements);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.getJSONObject("hearingArrangements").has("signLanguageInterpreter")).isFalse();
    }

    @Test
    void givenCcdCaseIdIsNull_thenDoNotSetCcdCaseId() {
        roboticsWrapper.setCcdCaseId(null);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        roboticsJsonValidator.validate(roboticsJson, CASE_ID);

        assertThat(roboticsJson.has("caseId")).isFalse();
    }

    @Test
    void givenAMissingRepresentative_thenProcessRobotics() {
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().setAddress(null);
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().setName(null);
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().setOrganisation(null);
        roboticsWrapper.getSscsCaseData().getAppeal().getRep().setHasRepresentative("No");

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.has("representative")).isFalse();
    }

    @Test
    void givenAnAppointee_thenProcessRoboticsWithAppellantPostCodeSetToVenueForAppointeePostcode() {
        given(airLookupService.lookupAirVenueNameByPostCode("TS1ABC", BenefitType.builder().code("PIP").build())).willReturn(
            "Pip venue");

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

        assertThat(roboticsJson.has("appointee")).isTrue();
        assertThat(roboticsJson.getJSONObject("appointee").getString("sameAddressAsAppellant")).isEqualTo("Yes");
        assertThat(roboticsJson.get("appellantPostCode")).isEqualTo("Pip venue");
    }

    @Test
    void findVenueHandleMissingFields() {
        roboticsWrapper.getSscsCaseData().getAppeal().setBenefitType(null);
        Optional<String> venue = roboticsJsonMapper.findVenueName(roboticsWrapper.getSscsCaseData());
        assertThat(venue).isEmpty();
    }

    @Test
    void findVenueHandleValidFields() {
        Optional<String> venue = roboticsJsonMapper.findVenueName(roboticsWrapper.getSscsCaseData());
        assertThat(venue).isPresent();
    }

    @Test
    void givenAnAppointeeWithEmptyDetails_thenProcessRobotics() {
        Name appointeeName = Name.builder().title(null).firstName(null).lastName(null).build();

        Appointee appointee = Appointee.builder()
                                       .name(appointeeName)
                                       .address(Address
                                           .builder()
                                           .line1(null)
                                           .line2(null)
                                           .town(null)
                                           .county(null)
                                           .postcode(null)
                                           .build())
                                       .contact(Contact.builder().email(null).phone(null).mobile(null).build())
                                       .identity(Identity.builder().dob(null).nino(null).build())
                                       .build();

        roboticsWrapper.getSscsCaseData().getAppeal().getAppellant().setAppointee(appointee);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.has("appointee")).isFalse();
    }


    @Test
    void givenNoAppointee_thenProcessRobotics() {
        roboticsWrapper.getSscsCaseData().getAppeal().getAppellant().setAppointee(null);
        roboticsWrapper.getSscsCaseData().getAppeal().getAppellant().setIsAppointee("No");

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.has("appointee")).isFalse();
    }

    @Test
    void givenCaseCreatedDate_thenAppealDateShouldGetUpdated() {
        String caseCreatedDate = "2019-08-01";

        roboticsWrapper.getSscsCaseData().setCaseCreated(caseCreatedDate);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.has("appealDate")).isTrue();

        assertThat(roboticsJson.get("appealDate")).isEqualTo(caseCreatedDate);
    }

    @Test
    void givenCaseCreatedDateIsNull_thenAppealDateShouldBeCurrentDate() {
        roboticsWrapper.getSscsCaseData().setCaseCreated(null);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.has("appealDate")).isTrue();

        assertThat(roboticsJson.get("appealDate")).isEqualTo(LocalDate.now().toString());
    }

    @Test
    void givenStateIsAppealCreated_thenSetReadyToListFieldToNo() {

        roboticsWrapper.setState(State.APPEAL_CREATED);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.has("isReadyToList")).isTrue();
        assertThat(roboticsJson.get("isReadyToList")).isEqualTo("No");
    }

    @Test
    void givenStateIsReadyToList_thenSetReadyToListFields() {
        DynamicListItem value = new DynamicListItem("DWP PIP (AE)", "DWP PIP (AE)");

        roboticsWrapper.setState(State.READY_TO_LIST);
        roboticsWrapper.getSscsCaseData().setDwpOriginatingOffice(new DynamicList(value, Collections.singletonList(value)));
        roboticsWrapper.getSscsCaseData().setDwpPresentingOffice(new DynamicList(value, Collections.singletonList(value)));
        roboticsWrapper.getSscsCaseData().setDwpIsOfficerAttending("Yes");
        roboticsWrapper.getSscsCaseData().setDwpUcb("Yes");

        String date = LocalDate.now().toString();
        roboticsWrapper.getSscsCaseData().setDwpResponseDate(date);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.has("isReadyToList")).isTrue();
        assertThat(roboticsJson.get("dwpResponseDate")).isEqualTo(date);
        assertThat(roboticsJson.get("dwpIssuingOffice")).isEqualTo("PIP (AE)");
        assertThat(roboticsJson.get("dwpPresentingOffice")).isEqualTo("PIP (AE)");
        assertThat(roboticsJson.get("dwpIsOfficerAttending")).isEqualTo("Yes");
        assertThat(roboticsJson.get("dwpUcb")).isEqualTo("Yes");
    }

    @Test
    void givenStateIsReadyToListAndDwpOfficesAreNotSet_thenSetDefaultReadyToListFields() {
        String date = LocalDate.now().toString();
        roboticsWrapper.getSscsCaseData().setDwpResponseDate(date);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.has("isReadyToList")).isTrue();
        assertThat(roboticsJson.get("dwpResponseDate")).isEqualTo(date);
        assertThat(roboticsJson.get("dwpIssuingOffice")).isEqualTo("DWP PIP (1)");
        assertThat(roboticsJson.get("dwpPresentingOffice")).isEqualTo("DWP PIP (1)");
        assertThat(roboticsJson.get("dwpIsOfficerAttending")).isEqualTo("No");
        assertThat(roboticsJson.get("dwpUcb")).isEqualTo("No");
    }

    @ParameterizedTest
    @CsvSource({"validAppeal, No", "readyToList, Yes", "null, No"})
    void givenCreatedInGapsFrom_shouldSetRetrieveIsDigitalAccordingly(String createdInGapsFrom, String expectedIsDigital) {
        roboticsWrapper.getSscsCaseData().setCreatedInGapsFrom(createdInGapsFrom);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.get("isDigital")).isEqualTo(expectedIsDigital);
    }

    @Test
    void shouldPopulateRoboticsWithUcFields() {
        initialiseElementDisputedLists();

        roboticsWrapper.getSscsCaseData().getJointParty().setHasJointParty(YES);
        roboticsWrapper
            .getSscsCaseData()
            .getJointParty()
            .setName(Name.builder().title("Mr").firstName("Harry").lastName("Hotspur").build());
        roboticsWrapper
            .getSscsCaseData()
            .getJointParty()
            .setAddress(Address
                .builder()
                .line1("The road")
                .line2("Test")
                .town("Bedrock")
                .county("Bedfordshire")
                .postcode("BD1 5LK")
                .build());
        roboticsWrapper.getSscsCaseData().getJointParty().setJointPartyAddressSameAsAppellant(NO);
        roboticsWrapper.getSscsCaseData().setElementsDisputedIsDecisionDisputedByOthers("Yes");
        roboticsWrapper.getSscsCaseData().setElementsDisputedLinkedAppealRef("12345678");
        roboticsWrapper
            .getSscsCaseData()
            .getJointParty()
            .setIdentity(Identity.builder().nino("JT000000B").dob("2000-01-01").build());
        roboticsWrapper.getSscsCaseData().getAppeal().setHearingSubtype(HearingSubtype
            .builder()
            .wantsHearingTypeTelephone("Yes")
            .hearingTelephoneNumber("07999888000")
            .wantsHearingTypeVideo("Yes")
            .hearingVideoEmail("m@test.com")
            .wantsHearingTypeFaceToFace("No")
            .build());

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.has("jointParty")).isTrue();
        assertThat(roboticsJson.getJSONObject("jointParty").getString("sameAddressAsAppellant")).isEqualTo("No");
        assertThat(roboticsJson.getJSONObject("jointParty").get("title")).isEqualTo("Mr");
        assertThat(roboticsJson.getJSONObject("jointParty").get("firstName")).isEqualTo("Harry");
        assertThat(roboticsJson.getJSONObject("jointParty").get("lastName")).isEqualTo("Hotspur");
        assertThat(roboticsJson.getJSONObject("jointParty").get("townOrCity")).isEqualTo("Bedrock");
        assertThat(roboticsJson.getJSONObject("jointParty").get("county")).isEqualTo("Bedfordshire");
        assertThat(roboticsJson.getJSONObject("jointParty").get("addressLine1")).isEqualTo("The road");
        assertThat(roboticsJson.getJSONObject("jointParty").get("addressLine2")).isEqualTo("Test");
        assertThat(roboticsJson.getJSONObject("jointParty").get("postCode")).isEqualTo("BD1 5LK");
        assertThat(roboticsJson.getJSONObject("jointParty").get("nino")).isEqualTo("JT000000B");
        assertThat(roboticsJson.getJSONObject("jointParty").get("dob")).isEqualTo("2000-01-01");

        assertThat(roboticsJson.getJSONObject("elementsDisputed").getJSONArray("general").get(0)).isEqualTo(
            "firstIssueElementsDisputedGeneral");
        assertThat(roboticsJson.getJSONObject("elementsDisputed").getJSONArray("sanctions").get(0)).isEqualTo(
            "firstIssueElementsDisputedSanctions");
        assertThat(roboticsJson.getJSONObject("elementsDisputed").getJSONArray("overpayment").get(0)).isEqualTo(
            "firstIssueElementsDisputedOverpayment");
        assertThat(roboticsJson.getJSONObject("elementsDisputed").getJSONArray("housing").get(0)).isEqualTo(
            "firstIssueElementsDisputedHousing");
        assertThat(roboticsJson.getJSONObject("elementsDisputed").getJSONArray("childCare").get(0)).isEqualTo(
            "firstIssueElementsDisputedChildCare");
        assertThat(roboticsJson.getJSONObject("elementsDisputed").getJSONArray("care").get(0)).isEqualTo(
            "firstIssueElementsDisputedCare");
        assertThat(roboticsJson.getJSONObject("elementsDisputed").getJSONArray("childElement").get(0)).isEqualTo(
            "firstIssueElementsDisputedChildElement");
        assertThat(roboticsJson.getJSONObject("elementsDisputed").getJSONArray("childDisabled").get(0)).isEqualTo(
            "firstIssueElementsDisputedChildDisabled");

        assertThat(roboticsJson.get("ucDecisionDisputedByOthers")).isEqualTo("Yes");
        assertThat(roboticsJson.get("wantsHearingTypeTelephone")).isEqualTo("Yes");
        assertThat(roboticsJson.get("wantsHearingTypeVideo")).isEqualTo("Yes");
        assertThat(roboticsJson.get("wantsHearingTypeFaceToFace")).isEqualTo("No");
    }

    @Test
    void shouldPopulateRoboticsWithUcFieldsSameAddress() {
        initialiseElementDisputedLists();

        roboticsWrapper.getSscsCaseData().getJointParty().setHasJointParty(YES);
        roboticsWrapper
            .getSscsCaseData()
            .getJointParty()
            .setName(Name.builder().title("Mr").firstName("Harry").lastName("Hotspur").build());
        roboticsWrapper
            .getSscsCaseData()
            .getJointParty()
            .setAddress(Address
                .builder()
                .line1("The road")
                .line2("Test")
                .town("Bedrock")
                .county("Bedfordshire")
                .postcode("BD1 5LK")
                .build());
        roboticsWrapper.getSscsCaseData().getJointParty().setJointPartyAddressSameAsAppellant(YES);
        roboticsWrapper.getSscsCaseData().setElementsDisputedIsDecisionDisputedByOthers("Yes");
        roboticsWrapper.getSscsCaseData().setElementsDisputedLinkedAppealRef("12345678");
        roboticsWrapper
            .getSscsCaseData()
            .getJointParty()
            .setIdentity(Identity.builder().nino("JT000000B").dob("2000-01-01").build());
        roboticsWrapper.getSscsCaseData().getAppeal().setHearingSubtype(HearingSubtype
            .builder()
            .wantsHearingTypeTelephone("Yes")
            .hearingTelephoneNumber("07999888000")
            .wantsHearingTypeVideo("Yes")
            .hearingVideoEmail("m@test.com")
            .wantsHearingTypeFaceToFace("No")
            .build());

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.has("jointParty")).isTrue();
        assertThat(roboticsJson.getJSONObject("jointParty").getString("sameAddressAsAppellant")).isEqualTo("Yes");
        assertThat(roboticsJson.getJSONObject("jointParty").get("title")).isEqualTo("Mr");
        assertThat(roboticsJson.getJSONObject("jointParty").get("firstName")).isEqualTo("Harry");
        assertThat(roboticsJson.getJSONObject("jointParty").get("lastName")).isEqualTo("Hotspur");
        assertThat(roboticsJson.getJSONObject("appellant").get("addressLine1")).isEqualTo("123 Hairy Lane");
        assertThat(roboticsJson.getJSONObject("appellant").get("addressLine2")).isEqualTo("Off Hairy Park");
        assertThat(roboticsJson.getJSONObject("appellant").get("townOrCity")).isEqualTo("Hairyfield");
        assertThat(roboticsJson.getJSONObject("appellant").get("county")).isEqualTo("Kent");
        assertThat(roboticsJson.getJSONObject("appellant").get("postCode")).isEqualTo("TN32 6PL");
        assertThat(roboticsJson.getJSONObject("jointParty").get("nino")).isEqualTo("JT000000B");
        assertThat(roboticsJson.getJSONObject("jointParty").get("dob")).isEqualTo("2000-01-01");

        assertThat(roboticsJson.getJSONObject("elementsDisputed").getJSONArray("general").get(0)).isEqualTo(
            "firstIssueElementsDisputedGeneral");
        assertThat(roboticsJson.getJSONObject("elementsDisputed").getJSONArray("sanctions").get(0)).isEqualTo(
            "firstIssueElementsDisputedSanctions");
        assertThat(roboticsJson.getJSONObject("elementsDisputed").getJSONArray("overpayment").get(0)).isEqualTo(
            "firstIssueElementsDisputedOverpayment");
        assertThat(roboticsJson.getJSONObject("elementsDisputed").getJSONArray("housing").get(0)).isEqualTo(
            "firstIssueElementsDisputedHousing");
        assertThat(roboticsJson.getJSONObject("elementsDisputed").getJSONArray("childCare").get(0)).isEqualTo(
            "firstIssueElementsDisputedChildCare");
        assertThat(roboticsJson.getJSONObject("elementsDisputed").getJSONArray("care").get(0)).isEqualTo(
            "firstIssueElementsDisputedCare");
        assertThat(roboticsJson.getJSONObject("elementsDisputed").getJSONArray("childElement").get(0)).isEqualTo(
            "firstIssueElementsDisputedChildElement");
        assertThat(roboticsJson.getJSONObject("elementsDisputed").getJSONArray("childDisabled").get(0)).isEqualTo(
            "firstIssueElementsDisputedChildDisabled");

        assertThat(roboticsJson.get("ucDecisionDisputedByOthers")).isEqualTo("Yes");
        assertThat(roboticsJson.get("linkedAppealRef")).isEqualTo("12345678");
        assertThat(roboticsJson.get("wantsHearingTypeTelephone")).isEqualTo("Yes");
        assertThat(roboticsJson.get("wantsHearingTypeVideo")).isEqualTo("Yes");
        assertThat(roboticsJson.get("wantsHearingTypeFaceToFace")).isEqualTo("No");
    }

    @Test
    void givenElementContainsMultipleIssues_thenCheckMultipleIssuesTransformedToArray() {
        elementsDisputedGeneralList.addAll(Arrays.asList(
            ElementDisputed
                .builder()
                .value(ElementDisputedDetails.builder().issueCode("firstIssueElementsDisputedGeneral").build())
                .build(),
            ElementDisputed
                .builder()
                .value(ElementDisputedDetails.builder().issueCode("secondIssueElementsDisputedGeneral").build())
                .build(),
            ElementDisputed
                .builder()
                .value(ElementDisputedDetails.builder().issueCode("thirdIssueElementsDisputedGeneral").build())
                .build()));
        roboticsWrapper.getSscsCaseData().setElementsDisputedGeneral(elementsDisputedGeneralList);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.getJSONObject("elementsDisputed").getJSONArray("general").get(0)).isEqualTo(
            "firstIssueElementsDisputedGeneral");
        assertThat(roboticsJson.getJSONObject("elementsDisputed").getJSONArray("general").get(1)).isEqualTo(
            "secondIssueElementsDisputedGeneral");
        assertThat(roboticsJson.getJSONObject("elementsDisputed").getJSONArray("general").get(2)).isEqualTo(
            "thirdIssueElementsDisputedGeneral");
    }

    private void initialiseElementDisputedLists() {
        elementsDisputedGeneralList.add(ElementDisputed
            .builder()
            .value(ElementDisputedDetails.builder().issueCode("firstIssueElementsDisputedGeneral").build())
            .build());
        elementsDisputedSanctionsList.add(ElementDisputed
            .builder()
            .value(ElementDisputedDetails.builder().issueCode("firstIssueElementsDisputedSanctions").build())
            .build());
        elementsDisputedOverpaymentList.add(ElementDisputed
            .builder()
            .value(ElementDisputedDetails.builder().issueCode("firstIssueElementsDisputedOverpayment").build())
            .build());
        elementsDisputedHousingList.add(ElementDisputed
            .builder()
            .value(ElementDisputedDetails.builder().issueCode("firstIssueElementsDisputedHousing").build())
            .build());
        elementsDisputedChildCareList.add(ElementDisputed
            .builder()
            .value(ElementDisputedDetails.builder().issueCode("firstIssueElementsDisputedChildCare").build())
            .build());
        elementsDisputedCareList.add(ElementDisputed
            .builder()
            .value(ElementDisputedDetails.builder().issueCode("firstIssueElementsDisputedCare").build())
            .build());
        elementsDisputedChildElementList.add(ElementDisputed
            .builder()
            .value(ElementDisputedDetails.builder().issueCode("firstIssueElementsDisputedChildElement").build())
            .build());
        elementsDisputedChildDisabledList.add(ElementDisputed
            .builder()
            .value(ElementDisputedDetails.builder().issueCode("firstIssueElementsDisputedChildDisabled").build())
            .build());
        elementsDisputedLimitedWorkList.add(ElementDisputed
            .builder()
            .value(ElementDisputedDetails.builder().issueCode("firstIssueElementsDisputedLimitedWork").build())
            .build());

        roboticsWrapper.getSscsCaseData().setElementsDisputedGeneral(elementsDisputedGeneralList);
        roboticsWrapper.getSscsCaseData().setElementsDisputedSanctions(elementsDisputedSanctionsList);
        roboticsWrapper.getSscsCaseData().setElementsDisputedOverpayment(elementsDisputedOverpaymentList);
        roboticsWrapper.getSscsCaseData().setElementsDisputedHousing(elementsDisputedHousingList);
        roboticsWrapper.getSscsCaseData().setElementsDisputedChildCare(elementsDisputedChildCareList);
        roboticsWrapper.getSscsCaseData().setElementsDisputedCare(elementsDisputedCareList);
        roboticsWrapper.getSscsCaseData().setElementsDisputedChildElement(elementsDisputedChildElementList);
        roboticsWrapper.getSscsCaseData().setElementsDisputedChildDisabled(elementsDisputedChildDisabledList);
        roboticsWrapper.getSscsCaseData().setElementsDisputedLimitedWork(elementsDisputedLimitedWorkList);
    }

    @Test
    void givenConfidentialityRequestOutcomeGrantedForAppellant_thenSetIsConfidentialFlag() {
        roboticsWrapper
            .getSscsCaseData()
            .setConfidentialityRequestOutcomeAppellant(
                DatedRequestOutcome.builder().requestOutcome(RequestOutcome.GRANTED).build());
        roboticsWrapper.getSscsCaseData().setConfidentialityRequestOutcomeJointParty(null);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.get("isConfidential")).isEqualTo("Yes");
    }

    @Test
    void givenConfidentialCaseAndChildSupportBenefit_thenSetIsConfidentialFlag() {
        roboticsWrapper.getSscsCaseData().getExtendedSscsCaseData().setConfidentialCaseStatus(YesNoUnknown.YES);
        roboticsWrapper.getSscsCaseData().getAppeal().getBenefitType().setCode(CHILD_SUPPORT.getShortName());

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.get("isConfidential")).isEqualTo("Yes");
    }

    @Test
    void givenConfidentialCaseAndSscs5Benefit_thenSetIsConfidentialFlag() {
        roboticsWrapper.getSscsCaseData().getExtendedSscsCaseData().setConfidentialCaseStatus(YesNoUnknown.YES);
        roboticsWrapper.getSscsCaseData().getAppeal().getBenefitType().setCode(GUARDIANS_ALLOWANCE.getShortName());

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.get("isConfidential")).isEqualTo("Yes");
    }

    @Test
    void givenConfidentialCaseAndNonChildSupportBenefit_thenDoNotSetIsConfidentialFlag() {
        roboticsWrapper.getSscsCaseData().getExtendedSscsCaseData().setConfidentialCaseStatus(YesNoUnknown.YES);
        roboticsWrapper.getSscsCaseData().getAppeal().getBenefitType().setCode(PIP.getShortName());

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.has("isConfidential")).isFalse();
    }

    @Test
    void givenDlaCase_thenGetsDwpIssueOffice() {

        SscsCaseData sscsCaseData = buildCaseData("Test", "DLA", "Disability Benefit Centre 4");
        sscsCaseData.getAppeal().getAppellant().setIsAppointee("Yes");
        roboticsWrapper = RoboticsWrapper
            .builder()
            .sscsCaseData(sscsCaseData)
            .ccdCaseId(123L).evidencePresent("Yes")
            .state(State.APPEAL_CREATED)
            .build();

        String date = LocalDate.now().toString();
        roboticsWrapper.getSscsCaseData().setDwpResponseDate(date);

        given(
            airLookupService.lookupAirVenueNameByPostCode(anyString(), eq(sscsCaseData.getAppeal().getBenefitType()))).willReturn(
            "Bromley");

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        roboticsJsonValidator.validate(roboticsJson, CASE_ID);

        assertThat(roboticsJson.get("dwpIssuingOffice")).isEqualTo("Disability Benefit Centre 4");
        assertThat(roboticsJson.get("dwpPresentingOffice")).isEqualTo("Disability Benefit Centre 4");
    }

    @Test
    void givenIbaCase_thenGetsIbaIssueOffice() {

        SscsCaseData sscsCaseData = buildCaseData("Test", "infectedBloodCompensation", "IBCA");
        sscsCaseData.getAppeal().getAppellant().setIsAppointee("Yes");
        roboticsWrapper = RoboticsWrapper
            .builder()
            .sscsCaseData(sscsCaseData)
            .ccdCaseId(123L).evidencePresent("Yes")
            .state(State.APPEAL_CREATED)
            .build();

        String date = LocalDate.now().toString();
        roboticsWrapper.getSscsCaseData().setDwpResponseDate(date);

        given(
            airLookupService.lookupAirVenueNameByPostCode(anyString(), eq(sscsCaseData.getAppeal().getBenefitType()))).willReturn(
            "Bromley");

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        roboticsJsonValidator.validate(roboticsJson, CASE_ID);

        assertThat(roboticsJson.get("dwpIssuingOffice")).isEqualTo("IBCA");
        assertThat(roboticsJson.get("dwpPresentingOffice")).isEqualTo("IBCA");
    }

    @Test
    void shouldPopulateRoboticsWithOtherPartyDetails() {

        DateRange dateRange1 = DateRange.builder()
                                        .start("2021-12-30")
                                        .end("2021-12-30")
                                        .build();
        List<ExcludeDate> excludeDates = new ArrayList<>();
        excludeDates.add(ExcludeDate.builder()
                                    .value(dateRange1)
                                    .build());

        List<CcdValue<OtherParty>> otherPartyList = new ArrayList<>();
        CcdValue<OtherParty> ccdValue = CcdValue
            .<OtherParty>builder()
            .value(OtherParty
                .builder()
                .name(Name.builder().title("Mr").firstName("Max").lastName("Edwards").build())
                .address(Address
                    .builder()
                    .line1("123 My Road")
                    .line2("The Village")
                    .town("Chelmsford")
                    .county("Essex")
                    .postcode("CM1 1RP")
                    .build())
                .contact(Contact.builder().phone("01243551233").mobile("07000000001").email("test@email.com").build())
                .rep(Representative
                    .builder()
                    .hasRepresentative("Yes")
                    .name(Name.builder().title("Mrs").firstName("Wendy").lastName("Povey").build())
                    .address(Address
                        .builder()
                        .line1("456 My Road")
                        .line2("The Green")
                        .town("Whitham")
                        .county("Essex")
                        .postcode("CM10 2PE")
                        .build())
                    .contact(Contact.builder().phone("01243551444").mobile("07000000029").email("test2@email.com").build())
                    .organisation("My company")
                    .build())
                .hearingOptions(HearingOptions.builder().wantsToAttend("Yes").excludeDates(excludeDates).build())
                .build())
            .build();
        otherPartyList.add(ccdValue);

        roboticsWrapper.getSscsCaseData().setOtherParties(otherPartyList);
        roboticsWrapper.getSscsCaseData().setChildMaintenanceNumber("12345");
        roboticsWrapper.getSscsCaseData().getAppeal().getAppellant().setRole(Role.builder().name("Paying parent").build());
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setWantsToAttend("Yes");

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.get("childMaintenanceNumber")).isEqualTo("12345");
        assertThat(roboticsJson.get("appellantRole")).isEqualTo("Paying parent");

        assertThat(roboticsJson.has("otherParties")).isTrue();
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "title")).isEqualTo("Mr");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "firstName")).isEqualTo("Max");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "lastName")).isEqualTo("Edwards");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "phoneNumber")).isEqualTo("07000000001");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "email")).isEqualTo("test@email.com");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "addressLine1")).isEqualTo("123 My Road");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "addressLine2")).isEqualTo("The Village");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "townOrCity")).isEqualTo("Chelmsford");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "county")).isEqualTo("Essex");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "postCode")).isEqualTo("CM1 1RP");

        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "title")).isEqualTo("Mrs");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "firstName")).isEqualTo("Wendy");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "lastName")).isEqualTo("Povey");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "phoneNumber")).isEqualTo("07000000029");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "email")).isEqualTo("test2@email.com");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "addressLine1")).isEqualTo("456 My Road");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "addressLine2")).isEqualTo("The Green");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "townOrCity")).isEqualTo("Whitham");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "county")).isEqualTo("Essex");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "postCode")).isEqualTo("CM10 2PE");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "organisation")).isEqualTo("My company");

        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("hearingArrangements"))
            .getJSONArray("datesCantAttend")
            .get(0)).isEqualTo("2021-12-30");
    }

    @Test
    void givenOtherPartyWithNoAppointeeAndNoRep_thenPopulateRoboticsWithNoOtherPartyRepOrAppointeeDetails() {

        DateRange dateRange1 = DateRange.builder()
                                        .start("2021-12-30")
                                        .end("2021-12-30")
                                        .build();
        List<ExcludeDate> excludeDates = new ArrayList<>();
        excludeDates.add(ExcludeDate.builder()
                                    .value(dateRange1)
                                    .build());

        List<CcdValue<OtherParty>> otherPartyList = new ArrayList<>();
        CcdValue<OtherParty> ccdValue = CcdValue
            .<OtherParty>builder()
            .value(OtherParty
                .builder()
                .name(Name.builder().title("Mr").firstName("Max").lastName("Edwards").build())
                .address(Address
                    .builder()
                    .line1("123 My Road")
                    .line2("The Village")
                    .town("Chelmsford")
                    .county("Essex")
                    .postcode("CM1 1RP")
                    .build())
                .contact(Contact.builder().phone("01243551233").mobile("07000000001").email("test@email.com").build())
                .isAppointee("No")
                .appointee(Appointee.builder().build())
                .rep(Representative.builder().build())
                .hearingOptions(HearingOptions.builder().excludeDates(excludeDates).build())
                .build())
            .build();
        otherPartyList.add(ccdValue);

        roboticsWrapper.getSscsCaseData().setOtherParties(otherPartyList);
        roboticsWrapper.getSscsCaseData().setChildMaintenanceNumber("12345");
        roboticsWrapper.getSscsCaseData().getAppeal().getAppellant().setRole(Role.builder().name("Paying parent").build());

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.get("childMaintenanceNumber")).isEqualTo("12345");
        assertThat(roboticsJson.get("appellantRole")).isEqualTo("Paying parent");

        assertThat(roboticsJson.has("otherParties")).isTrue();
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "title")).isEqualTo("Mr");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "firstName")).isEqualTo("Max");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "lastName")).isEqualTo("Edwards");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "phoneNumber")).isEqualTo("07000000001");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "email")).isEqualTo("test@email.com");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "addressLine1")).isEqualTo("123 My Road");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "addressLine2")).isEqualTo("The Village");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "townOrCity")).isEqualTo("Chelmsford");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "county")).isEqualTo("Essex");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "postCode")).isEqualTo("CM1 1RP");

        assertThat(((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).has("otherPartyRepresentative")).isFalse();
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("hearingArrangements"))
            .getJSONArray("datesCantAttend")
            .get(0)).isEqualTo("2021-12-30");
    }

    @Test
    void givenOtherPartyHasAppointee_thenPopulateRoboticsWithAppointeeOtherPartyDetails() {

        DateRange dateRange1 = DateRange.builder()
                                        .start("2021-12-30")
                                        .end("2021-12-30")
                                        .build();
        List<ExcludeDate> excludeDates = new ArrayList<>();
        excludeDates.add(ExcludeDate.builder()
                                    .value(dateRange1)
                                    .build());

        List<CcdValue<OtherParty>> otherPartyList = new ArrayList<>();
        CcdValue<OtherParty> ccdValue = CcdValue
            .<OtherParty>builder()
            .value(OtherParty
                .builder()
                .name(Name.builder().title("Mr").firstName("Max").lastName("Edwards").build())
                .address(Address
                    .builder()
                    .line1("123 My Road")
                    .line2("The Village")
                    .town("Chelmsford")
                    .county("Essex")
                    .postcode("CM1 1RP")
                    .build())
                .contact(Contact.builder().phone("01243551233").mobile("07000000001").email("test@email.com").build())
                .isAppointee("Yes")
                .appointee(Appointee
                    .builder()
                    .name(Name.builder().title("Mr").firstName("Lucas").lastName("Moura").build())
                    .address(Address
                        .builder()
                        .line1("777 My Road")
                        .line2("The Square")
                        .town("Braintree")
                        .county("Essex")
                        .postcode("CM5 2XD")
                        .build())
                    .contact(Contact.builder().phone("01243551555").mobile("07000000028").email("test6@email.com").build())
                    .build())
                .rep(Representative
                    .builder()
                    .hasRepresentative("Yes")
                    .name(Name.builder().title("Mrs").firstName("Wendy").lastName("Povey").build())
                    .address(Address
                        .builder()
                        .line1("456 My Road")
                        .line2("The Green")
                        .town("Whitham")
                        .county("Essex")
                        .postcode("CM10 2PE")
                        .build())
                    .contact(Contact.builder().phone("01243551444").mobile("07000000029").email("test2@email.com").build())
                    .organisation("My company")
                    .build())
                .hearingOptions(HearingOptions.builder().excludeDates(excludeDates).build())
                .build())
            .build();
        otherPartyList.add(ccdValue);

        roboticsWrapper.getSscsCaseData().setOtherParties(otherPartyList);
        roboticsWrapper.getSscsCaseData().setChildMaintenanceNumber("12345");
        roboticsWrapper.getSscsCaseData().getAppeal().getAppellant().setRole(Role.builder().name("Paying parent").build());

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.get("childMaintenanceNumber")).isEqualTo("12345");
        assertThat(roboticsJson.get("appellantRole")).isEqualTo("Paying parent");

        assertThat(roboticsJson.has("otherParties")).isTrue();
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "title")).isEqualTo("Mr");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "firstName")).isEqualTo("Lucas");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "lastName")).isEqualTo("Moura");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "phoneNumber")).isEqualTo("07000000028");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "email")).isEqualTo("test6@email.com");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "addressLine1")).isEqualTo("777 My Road");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "addressLine2")).isEqualTo("The Square");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "townOrCity")).isEqualTo("Braintree");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "county")).isEqualTo("Essex");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "postCode")).isEqualTo("CM5 2XD");

        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "title")).isEqualTo("Mrs");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "firstName")).isEqualTo("Wendy");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "lastName")).isEqualTo("Povey");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "phoneNumber")).isEqualTo("07000000029");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "email")).isEqualTo("test2@email.com");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "addressLine1")).isEqualTo("456 My Road");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "addressLine2")).isEqualTo("The Green");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "townOrCity")).isEqualTo("Whitham");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "county")).isEqualTo("Essex");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "postCode")).isEqualTo("CM10 2PE");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "organisation")).isEqualTo("My company");

        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("hearingArrangements"))
            .getJSONArray("datesCantAttend")
            .get(0)).isEqualTo("2021-12-30");
    }

    @Test
    void shouldPopulateRoboticsWithMultipleOtherPartyDetails() {

        DateRange dateRange1 = DateRange.builder()
                                        .start("2021-12-30")
                                        .end("2021-12-30")
                                        .build();
        List<ExcludeDate> excludeDates = new ArrayList<>();
        excludeDates.add(ExcludeDate.builder().value(dateRange1).build());

        List<CcdValue<OtherParty>> otherPartyList = new ArrayList<>();
        CcdValue<OtherParty> ccdValue = CcdValue
            .<OtherParty>builder()
            .value(OtherParty
                .builder()
                .name(Name.builder().title("Mr").firstName("Max").lastName("Edwards").build())
                .address(Address
                    .builder()
                    .line1("123 My Road")
                    .line2("The Village")
                    .town("Chelmsford")
                    .county("Essex")
                    .postcode("CM1 1RP")
                    .build())
                .contact(Contact.builder().phone("01243551233").mobile("07000000001").email("test@email.com").build())
                .rep(Representative
                    .builder()
                    .hasRepresentative("Yes")
                    .name(Name.builder().title("Mrs").firstName("Wendy").lastName("Povey").build())
                    .address(Address
                        .builder()
                        .line1("456 My Road")
                        .line2("The Green")
                        .town("Whitham")
                        .county("Essex")
                        .postcode("CM10 2PE")
                        .build())
                    .contact(Contact.builder().phone("01243551444").mobile("07000000029").email("test2@email.com").build())
                    .organisation("My company")
                    .build())
                .hearingOptions(HearingOptions.builder().excludeDates(excludeDates).build())
                .build())
            .build();
        otherPartyList.add(ccdValue);

        CcdValue<OtherParty> ccdValue2 = CcdValue
            .<OtherParty>builder()
            .value(OtherParty
                .builder()
                .name(Name.builder().title("Mr").firstName("Henry").lastName("Fitch").build())
                .address(Address
                    .builder()
                    .line1("999 My Road")
                    .line2("The Square")
                    .town("Ingatestone")
                    .county("Essex")
                    .postcode("CM6 4DW")
                    .build())
                .contact(Contact.builder().phone("01243551299").mobile("07000000012").email("test3@email.com").build())
                .build())
            .build();
        otherPartyList.add(ccdValue2);

        roboticsWrapper.getSscsCaseData().setOtherParties(otherPartyList);
        roboticsWrapper.getSscsCaseData().setChildMaintenanceNumber("12345");
        roboticsWrapper.getSscsCaseData().getAppeal().getAppellant().setRole(Role.builder().name("Paying parent").build());

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.get("childMaintenanceNumber")).isEqualTo("12345");
        assertThat(roboticsJson.get("appellantRole")).isEqualTo("Paying parent");

        assertThat(roboticsJson.has("otherParties")).isTrue();
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "title")).isEqualTo("Mr");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "firstName")).isEqualTo("Max");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "lastName")).isEqualTo("Edwards");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "phoneNumber")).isEqualTo("07000000001");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "email")).isEqualTo("test@email.com");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "addressLine1")).isEqualTo("123 My Road");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "addressLine2")).isEqualTo("The Village");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "townOrCity")).isEqualTo("Chelmsford");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "county")).isEqualTo("Essex");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "postCode")).isEqualTo("CM1 1RP");

        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "title")).isEqualTo("Mrs");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "firstName")).isEqualTo("Wendy");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "lastName")).isEqualTo("Povey");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "phoneNumber")).isEqualTo("07000000029");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "email")).isEqualTo("test2@email.com");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "addressLine1")).isEqualTo("456 My Road");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "addressLine2")).isEqualTo("The Green");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "townOrCity")).isEqualTo("Whitham");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "county")).isEqualTo("Essex");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "postCode")).isEqualTo("CM10 2PE");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "organisation")).isEqualTo("My company");

        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("hearingArrangements"))
            .getJSONArray("datesCantAttend")
            .get(0)).isEqualTo("2021-12-30");

        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(1)).get("otherParty")).get(
            "title")).isEqualTo("Mr");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(1)).get("otherParty")).get(
            "firstName")).isEqualTo("Henry");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(1)).get("otherParty")).get(
            "lastName")).isEqualTo("Fitch");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(1)).get("otherParty")).get(
            "phoneNumber")).isEqualTo("07000000012");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(1)).get("otherParty")).get(
            "email")).isEqualTo("test3@email.com");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(1)).get("otherParty")).get(
            "addressLine1")).isEqualTo("999 My Road");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(1)).get("otherParty")).get(
            "addressLine2")).isEqualTo("The Square");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(1)).get("otherParty")).get(
            "townOrCity")).isEqualTo("Ingatestone");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(1)).get("otherParty")).get(
            "county")).isEqualTo("Essex");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(1)).get("otherParty")).get(
            "postCode")).isEqualTo("CM6 4DW");


        assertThat(((JSONObject) roboticsJson.getJSONArray("otherParties").get(1)).has("otherPartyRepresentative")).isFalse();
        assertThat(((JSONObject) roboticsJson.getJSONArray("otherParties").get(1)).has("hearingArrangements")).isFalse();
    }

    @Test
    void givenAppellantRoleIsOther_shouldPopulateRoboticsWithOtherPartyDetailsAndNoAppellantRole() {

        DateRange dateRange1 = DateRange.builder()
                                        .start("2021-12-30")
                                        .end("2021-12-30")
                                        .build();
        List<ExcludeDate> excludeDates = new ArrayList<>();
        excludeDates.add(ExcludeDate.builder()
                                    .value(dateRange1)
                                    .build());

        List<CcdValue<OtherParty>> otherPartyList = new ArrayList<>();
        CcdValue<OtherParty> ccdValue = CcdValue
            .<OtherParty>builder()
            .value(OtherParty
                .builder()
                .name(Name.builder().title("Mr").firstName("Max").lastName("Edwards").build())
                .address(Address
                    .builder()
                    .line1("123 My Road")
                    .line2("The Village")
                    .town("Chelmsford")
                    .county("Essex")
                    .postcode("CM1 1RP")
                    .build())
                .contact(Contact.builder().phone("01243551233").mobile("07000000001").email("test@email.com").build())
                .rep(Representative
                    .builder()
                    .hasRepresentative("Yes")
                    .name(Name.builder().title("Mrs").firstName("Wendy").lastName("Povey").build())
                    .address(Address
                        .builder()
                        .line1("456 My Road")
                        .line2("The Green")
                        .town("Whitham")
                        .county("Essex")
                        .postcode("CM10 2PE")
                        .build())
                    .contact(Contact.builder().phone("01243551444").mobile("07000000029").email("test2@email.com").build())
                    .organisation("My company")
                    .build())
                .hearingOptions(HearingOptions.builder().excludeDates(excludeDates).build())
                .build())
            .build();
        otherPartyList.add(ccdValue);

        roboticsWrapper.getSscsCaseData().setOtherParties(otherPartyList);
        roboticsWrapper.getSscsCaseData().setChildMaintenanceNumber("12345");
        roboticsWrapper.getSscsCaseData().getAppeal().getAppellant().setRole(Role.builder().name("Other").build());

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.get("childMaintenanceNumber")).isEqualTo("12345");
        assertThat(roboticsJson.has("appellantRole")).isFalse();

        assertThat(roboticsJson.has("otherParties")).isTrue();
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "title")).isEqualTo("Mr");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "firstName")).isEqualTo("Max");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "lastName")).isEqualTo("Edwards");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "phoneNumber")).isEqualTo("07000000001");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "email")).isEqualTo("test@email.com");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "addressLine1")).isEqualTo("123 My Road");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "addressLine2")).isEqualTo("The Village");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "townOrCity")).isEqualTo("Chelmsford");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "county")).isEqualTo("Essex");
        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherParty")).get(
            "postCode")).isEqualTo("CM1 1RP");

        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "title")).isEqualTo("Mrs");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "firstName")).isEqualTo("Wendy");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "lastName")).isEqualTo("Povey");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "phoneNumber")).isEqualTo("07000000029");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "email")).isEqualTo("test2@email.com");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "addressLine1")).isEqualTo("456 My Road");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "addressLine2")).isEqualTo("The Green");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "townOrCity")).isEqualTo("Whitham");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "county")).isEqualTo("Essex");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "postCode")).isEqualTo("CM10 2PE");
        assertThat(
            ((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("otherPartyRepresentative")).get(
                "organisation")).isEqualTo("My company");

        assertThat(((JSONObject) ((JSONObject) roboticsJson.getJSONArray("otherParties").get(0)).get("hearingArrangements"))
            .getJSONArray("datesCantAttend")
            .get(0)).isEqualTo("2021-12-30");
    }

    @ParameterizedTest
    @CsvSource({"No, No, Paper", "No, Yes, Oral", "Yes, No, Oral", "Yes, Yes, Oral"})
    void givenOtherPartyAndAppellantAttendingHearingPreference_thenSetHearingTypeCorrectly(String isAppellantAttending,
        String isOtherPartyAttending, String expectedResult) {

        List<CcdValue<OtherParty>> otherPartyList = new ArrayList<>();
        CcdValue<OtherParty> ccdValue = CcdValue
            .<OtherParty>builder()
            .value(OtherParty
                .builder()
                .name(Name.builder().title("Mr").firstName("Max").lastName("Edwards").build())
                .address(Address
                    .builder()
                    .line1("123 My Road")
                    .line2("The Village")
                    .town("Chelmsford")
                    .county("Essex")
                    .postcode("CM1 1RP")
                    .build())
                .contact(Contact.builder().phone("01243551233").mobile("07000000001").email("test@email.com").build())
                .hearingOptions(HearingOptions.builder().wantsToAttend(isAppellantAttending).build())
                .build())
            .build();
        otherPartyList.add(ccdValue);

        roboticsWrapper.getSscsCaseData().setOtherParties(otherPartyList);
        roboticsWrapper.getSscsCaseData().getAppeal().getHearingOptions().setWantsToAttend(isOtherPartyAttending);

        roboticsJson = roboticsJsonMapper.map(roboticsWrapper);

        assertThat(roboticsJson.get("hearingType")).isEqualTo(expectedResult);
    }
}