package uk.gov.hmcts.reform.sscs.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils.buildCaseData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.DateRange;
import uk.gov.hmcts.reform.sscs.ccd.domain.ExcludeDate;
import uk.gov.hmcts.reform.sscs.domain.robotics.RoboticsWrapper;

public class RoboticsJsonMapperTest {

    private RoboticsJsonMapper roboticsJsonMapper = new RoboticsJsonMapper();
    private RoboticsWrapper appeal;
    private RoboticsJsonValidator roboticsJsonValidator = new RoboticsJsonValidator("/schema/sscs-robotics.json");

    @Before
    public void setup() {
        appeal = RoboticsWrapper
                .builder()
                .sscsCaseData(buildCaseData())
                .ccdCaseId(123L).venueName("Bromley").evidencePresent("Yes")
                .build();
    }

    @Test
    public void mapsAppealToRoboticsJson() {

        JSONObject roboticsJson = roboticsJsonMapper.map(appeal);

        roboticsJsonValidator.validate(roboticsJson);

        assertEquals(
            "If this fails, add an assertion below, do not just increment the number :)", 14,
            roboticsJson.length()
        );

        assertEquals("002DD", roboticsJson.get("caseCode"));
        assertEquals(123L, roboticsJson.get("caseId"));
        assertEquals("AB 22 55 66 B", roboticsJson.get("appellantNino"));
        assertEquals("Bromley", roboticsJson.get("appellantPostCode"));
        assertEquals(LocalDate.now().toString(), roboticsJson.get("appealDate"));
        assertEquals("2018-06-29", roboticsJson.get("mrnDate"));
        assertEquals("Lost my paperwork", roboticsJson.get("mrnReasonForBeingLate"));
        assertEquals("1", roboticsJson.get("pipNumber"));
        assertEquals("Oral", roboticsJson.get("hearingType"));
        assertEquals("Mr User Test", roboticsJson.get("hearingRequestParty"));
        assertEquals("Yes", roboticsJson.get("evidencePresent"));

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
    }

    @Test
    public void mapRepTitleToDefaultValuesWhenSetToNull() {

        appeal.getSscsCaseData().getAppeal().getRep().getName().setTitle(null);

        JSONObject roboticsJson = roboticsJsonMapper.map(appeal);

        assertEquals("s/m", roboticsJson.getJSONObject("representative").get("title"));
    }

    @Test
    public void mapRepFirstNameToDefaultValuesWhenSetToNull() {

        appeal.getSscsCaseData().getAppeal().getRep().getName().setFirstName(null);

        JSONObject roboticsJson = roboticsJsonMapper.map(appeal);

        assertEquals(".", roboticsJson.getJSONObject("representative").get("firstName"));
    }

    @Test
    public void mapRepLastNameToDefaultValuesWhenSetToNull() {

        appeal.getSscsCaseData().getAppeal().getRep().getName().setLastName(null);

        JSONObject roboticsJson = roboticsJsonMapper.map(appeal);

        assertEquals(".", roboticsJson.getJSONObject("representative").get("lastName"));
    }

    @Test
    public void givenLanguageInterpreterIsTrue_thenSetToLanguageInterpreterType() {

        appeal.getSscsCaseData().getAppeal().getHearingOptions().setLanguages("My Language");
        appeal.getSscsCaseData().getAppeal().getHearingOptions().setLanguageInterpreter("Yes");

        JSONObject roboticsJson = roboticsJsonMapper.map(appeal);

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
        appeal.getSscsCaseData().getAppeal().getHearingOptions().setArrangements(null);
        appeal.getSscsCaseData().getAppeal().getHearingOptions().setExcludeDates(excludeDates);

        JSONObject roboticsJson = roboticsJsonMapper.map(appeal);

        assertEquals("No", roboticsJson.getJSONObject("hearingArrangements").get("hearingLoop"));
        assertEquals("No", roboticsJson.getJSONObject("hearingArrangements").get("accessibleHearingRoom"));
        assertEquals("2018-06-30", roboticsJson.getJSONObject("hearingArrangements").getJSONArray("datesCantAttend").get(0));
    }

    @Test
    public void givenLanguageInterpreterIsFalse_thenDoNotSetLanguageInterpreter() {

        appeal.getSscsCaseData().getAppeal().getHearingOptions().setLanguages("My Language");
        appeal.getSscsCaseData().getAppeal().getHearingOptions().setLanguageInterpreter("No");

        JSONObject roboticsJson = roboticsJsonMapper.map(appeal);

        assertFalse(roboticsJson.getJSONObject("hearingArrangements").has("languageInterpreter"));
    }

    @Test
    public void givenLanguageInterpreterIsTrueAndInterpreterLanguageTypeIsNull_thenDoNotSetLanguageInterpreter() {

        appeal.getSscsCaseData().getAppeal().getHearingOptions().setLanguages(null);
        appeal.getSscsCaseData().getAppeal().getHearingOptions().setLanguageInterpreter("Yes");

        JSONObject roboticsJson = roboticsJsonMapper.map(appeal);

        assertFalse(roboticsJson.getJSONObject("hearingArrangements").has("languageInterpreter"));
    }

    @Test
    public void givenSignLanguageInterpreterIsTrue_thenSetToSignLanguageInterpreterType() {

        appeal.getSscsCaseData().getAppeal().getHearingOptions().setSignLanguageType("My Language");
        List<String> arrangements = new ArrayList<>();
        arrangements.add("signLanguageInterpreter");
        appeal.getSscsCaseData().getAppeal().getHearingOptions().setArrangements(arrangements);

        JSONObject roboticsJson = roboticsJsonMapper.map(appeal);

        assertEquals("My Language", roboticsJson.getJSONObject("hearingArrangements").get("signLanguageInterpreter"));
    }

    @Test
    public void givenSignLanguageInterpreterIsFalse_thenDoNotSetSignLanguageInterpreter() {

        appeal.getSscsCaseData().getAppeal().getHearingOptions().setSignLanguageType("My Language");
        List<String> arrangements = new ArrayList<>();
        appeal.getSscsCaseData().getAppeal().getHearingOptions().setArrangements(arrangements);

        JSONObject roboticsJson = roboticsJsonMapper.map(appeal);

        assertFalse(roboticsJson.getJSONObject("hearingArrangements").has("signLanguageInterpreter"));
    }

    @Test
    public void givenSignLanguageInterpreterIsTrueAndSignInterpreterLanguageTypeIsNull_thenDoNotSetSignLanguageInterpreter() {

        appeal.getSscsCaseData().getAppeal().getHearingOptions().setSignLanguageType(null);
        List<String> arrangements = new ArrayList<>();
        arrangements.add("signLanguageInterpreter");
        appeal.getSscsCaseData().getAppeal().getHearingOptions().setArrangements(arrangements);

        JSONObject roboticsJson = roboticsJsonMapper.map(appeal);

        assertFalse(roboticsJson.getJSONObject("hearingArrangements").has("signLanguageInterpreter"));
    }

    @Test
    public void givenCcdCaseIdIsNull_thenDoNotSetCcdCaseId() {
        appeal.setCcdCaseId(null);

        JSONObject roboticsJson = roboticsJsonMapper.map(appeal);

        roboticsJsonValidator.validate(roboticsJson);

        assertFalse(roboticsJson.has("caseId"));
    }

    @Test
    public void givenAMissingRepresentative_thenProcessRobotics() {

        appeal.getSscsCaseData().getAppeal().getRep().setAddress(null);
        appeal.getSscsCaseData().getAppeal().getRep().setName(null);
        appeal.getSscsCaseData().getAppeal().getRep().setOrganisation(null);
        appeal.getSscsCaseData().getAppeal().getRep().setHasRepresentative("No");

        JSONObject roboticsJson = roboticsJsonMapper.map(appeal);

        assertFalse(roboticsJson.has("representative"));

    }
}
