package uk.gov.hmcts.reform.sscs.robotics;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import org.everit.json.schema.ValidationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.util.CollectionUtils;

public class RoboticsJsonValidatorTest {

    private static final String caseId = "12345678";

    private JSONObject jsonData = loadJsonObject("/schema/valid_robotics_agreed.json");

    private JSONObject jsonDataSingleOtherParty = loadJsonObject("/schema/valid_robotics_agreed_single_other_parties_sscs2.json");

    private final JSONObject jsonDataMultipleOtherParty = loadJsonObject("/schema/valid_robotics_agreed_multiple_other_parties_sscs2.json");

    private final JSONObject jsonDataSingleOtherPartySscs5 = loadJsonObject("/schema/valid_robotics_agreed_single_other_parties_sscs5.json");

    private final JSONObject jsonDataMultipleOtherPartySscs5 = loadJsonObject("/schema/valid_robotics_agreed_multiple_other_parties_sscs5.json");

    private RoboticsJsonValidator roboticsSchema = new RoboticsJsonValidator("/schema/sscs-robotics.json");

    private JSONObject loadJsonObject(String path) {
        return new JSONObject(
                new JSONTokener(getClass().getResourceAsStream(path)));
    }

    @Test
    public void givenValidInputAgreedWithAutomationTeam_thenValidateAgainstSchema() throws ValidationException {
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertTrue(CollectionUtils.isEmpty(actual));
    }

    @ParameterizedTest
    @ValueSource(strings = {"appellant", "appointee"})
    public void givenRoboticJsonWithDobForAppellantAndAppointee_shouldValidateSuccessfully(String person) throws IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "2018-08-12", person, "dob");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertTrue(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenRoboticJsonWithNoDobForAppellantOrAppointee_shouldValidateSuccessfully() {
        jsonData.getJSONObject("appellant").remove("dob");
        jsonData.getJSONObject("appointee").remove("dob");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertTrue(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenRoboticJsonWithEmptyStringForAppellantNino_shouldValidateSuccessfully() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "", "appellantNino");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenRoboticJsonWithEmptyStringForAppellantFirstName_shouldValidateSuccessfully() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "", "appellant", "firstName");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenRoboticJsonWithEmptyStringForAppellantLastName_shouldValidateSuccessfully() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "", "appellant", "lastName");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenRoboticJsonWithEmptyStringForAppellantAddressLine1_shouldValidateSuccessfully() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "", "appellant", "addressLine1");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenRoboticJsonWithEmptyStringForAppellantTownOrCity_shouldValidateSuccessfully() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "", "appellant", "townOrCity");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenRoboticJsonWithEmptyStringForAppellantCounty_shouldValidateSuccessfully() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "", "appellant", "county");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Yes", "No"})
    public void givenJsonWithTheSameAddressAsAppellantProperty_shouldValidateSuccessfully(String value) {
        jsonData.getJSONObject("appointee").put("sameAddressAsAppellant", value);
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertTrue(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenJsonWithNoTheSameAddressAsAppellantProp_shouldValidateSuccessfully() {
        jsonData.getJSONObject("appointee").remove("sameAddressAsAppellant");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertTrue(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenInvalidInputForCaseCode_throwExceptionWhenValidatingAgainstSchema() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "002CCC", "caseCode");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenInvalidInputForEmptyCaseCode_throwExceptionWhenValidatingAgainstSchema() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "", "caseCode");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenInvalidInputForPostCode_throwExceptionWhenValidatingAgainstSchema() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "B231ABXXX", "appellant", "postCode");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenInvalidInputForCaseCreatedDate_throwExceptionWhenValidatingAgainstSchema() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "2018/06/01", "caseCreatedDate");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenInvalidInputForMrnDate_throwExceptionWhenValidatingAgainstSchema() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "2018/06/02", "mrnDate");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenInvalidInputForAppealDate_throwExceptionWhenValidatingAgainstSchema() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "2018/06/03", "appealDate");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenEmptyInputForAppealDate_throwExceptionWhenValidatingAgainstSchema() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "", "appealDate");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenInvalidInputForHearingType_throwExceptionWhenValidatingAgainstSchema() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "Computer", "hearingType");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenEmptyInputForHearingType_throwExceptionWhenValidatingAgainstSchema() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "", "hearingType");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenOralInputForLanguageInterpreterWithNoHearingRequestParty_throwExceptionWhenValidatingAgainstSchema() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "Oral", "hearingType");
        jsonData = removeProperty(jsonData.toString(), "hearingRequestParty");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenPaperInputForLanguageInterpreterWithNoHearingRequestParty_doesNotThrowExceptionWhenValidatingAgainstSchema() throws IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "Paper", "hearingType");
        jsonData = removeProperty(jsonData.toString(), "hearingRequestParty");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertTrue(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenInvalidInputForWantsToAttendHearing_throwExceptionWhenValidatingAgainstSchema() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "Bla", "wantsToAttendHearing");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenInvalidInputForEvidencePresent_throwExceptionWhenValidatingAgainstSchema() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "Bla", "evidencePresent");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenEmptyInputForEvidencePresent_throwExceptionWhenValidatingAgainstSchema() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "", "evidencePresent");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenInvalidInputForHearingLoop_throwExceptionWhenValidatingAgainstSchema() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "Bla", "hearingArrangements", "hearingLoop");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenInvalidInputForAccessibleHearingRoom_throwExceptionWhenValidatingAgainstSchema() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "Bla", "hearingArrangements", "accessibleHearingRoom");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenInvalidInputForDisabilityAccess_throwExceptionWhenValidatingAgainstSchema() throws ValidationException, IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "Bla", "hearingArrangements", "disabilityAccess");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }
    
    @Test
    public void givenInvalidInputForHearingDatesCantAttend_throwExceptionWhenValidatingAgainstSchema() throws ValidationException {
        jsonData = new JSONObject(jsonData.toString().replace("2018-08-12", "2018/08/12"));
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertFalse(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenSingleInvlidInputThenContainsSingleErrorMessage() throws IOException {
        jsonData = updateEmbeddedProperty(jsonData.toString(), "", "appellantNino");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertEquals(1, actual.size());
        assertTrue(actual.contains("appellantNino is missing/not populated - please correct."));
    }

    @Test
    public void givenMultipleInvalidInputThenContainsMultipleErrorMessage() throws IOException {

        jsonData = updateEmbeddedProperty(jsonData.toString(), "", "appellantNino");
        jsonData = updateEmbeddedProperty(jsonData.toString(), "", "caseCode");
        jsonData = updateEmbeddedProperty(jsonData.toString(), "", "appellant", "firstName");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertEquals(3, actual.size());
        assertTrue(actual.contains("caseCode is invalid - please correct."));
        assertTrue(actual.contains("appellantNino is missing/not populated - please correct."));
        assertTrue(actual.contains("appellant.firstName is missing/not populated - please correct."));

    }

    @Test
    public void givenMultipleInvalidInputThenContainsMultipleErrorMessage2() throws IOException {

        jsonData = updateEmbeddedProperty(jsonData.toString(), "", "appellantNino");
        jsonData = updateEmbeddedProperty(jsonData.toString(), "", "caseCode");
        jsonData = updateEmbeddedProperty(jsonData.toString(), "", "appellant", "firstName");
        Set<String> actual = roboticsSchema.validate(jsonData, caseId);
        assertEquals(3, actual.size());
        assertTrue(actual.contains("caseCode is invalid - please correct."));
        assertTrue(actual.contains("appellantNino is missing/not populated - please correct."));
        assertTrue(actual.contains("appellant.firstName is missing/not populated - please correct."));

    }

    @Test
    public void givenValidInputSingleOtherPartyAgreedWithAutomationTeam_thenValidateAgainstSchema() throws ValidationException {
        Set<String> actual = roboticsSchema.validate(jsonDataSingleOtherParty, caseId);
        assertTrue(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenValidInputSingleOtherPartyForSscs5AgreedWithAutomationTeam_thenValidateAgainstSchema() throws ValidationException {
        Set<String> actual = roboticsSchema.validate(jsonDataSingleOtherPartySscs5, caseId);
        assertTrue(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenOtherPartyIsMissingFromOtherPartyObject_thenValidateAgainstSchema() throws ValidationException, IOException {

        jsonDataSingleOtherParty = removeObjectFromJsonArrayObject(jsonDataSingleOtherParty, "otherParties", 0, "otherParty");

        Set<String> actual = roboticsSchema.validate(jsonDataSingleOtherParty, caseId);
        assertEquals(1, actual.size());
        assertTrue(actual.contains("otherParties[0].otherParty is missing/not populated - please correct."));
    }

    @Test
    public void givenValidInputMultipleOtherPartyAgreedWithAutomationTeam_thenValidateAgainstSchema() throws ValidationException {
        Set<String> actual = roboticsSchema.validate(jsonDataMultipleOtherParty, caseId);
        assertTrue(CollectionUtils.isEmpty(actual));
    }

    @Test
    public void givenValidInputMultipleOtherPartyForSscs5AgreedWithAutomationTeam_thenValidateAgainstSchema() throws ValidationException {
        Set<String> actual = roboticsSchema.validate(jsonDataMultipleOtherPartySscs5, caseId);
        assertTrue(CollectionUtils.isEmpty(actual));
    }

    private static JSONObject updateEmbeddedProperty(String json, String value, String... keys) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Map map = objectMapper.readValue(json, Map.class);

        Map t = map;
        for (int i = 0; i < keys.length - 1; i++) {
            t = (Map) t.get(keys[i]);
        }

        //noinspection unchecked
        t.put(keys[keys.length - 1], value);

        return new JSONObject(objectMapper.writeValueAsString(map));
    }



    private static JSONObject removeProperty(String json, String key) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Map map = objectMapper.readValue(json, Map.class);

        map.remove(key);

        String jsonString = objectMapper.writeValueAsString(map);

        return new JSONObject(jsonString);
    }

    public static JSONObject removeObjectFromJsonArrayObject(JSONObject json, String jsonArrayKey, int arrayPosition, String keyToRemove) throws JsonProcessingException {
        JSONArray array = json.getJSONArray(jsonArrayKey);
        JSONObject o = (JSONObject) array.get(arrayPosition);

        ObjectMapper objectMapper = new ObjectMapper();

        Map map = objectMapper.readValue(o.toString(), Map.class);

        map.remove(keyToRemove);

        JSONArray otherPartyArray = new JSONArray();
        otherPartyArray.put(map);
        json.put(jsonArrayKey, otherPartyArray);

        return json;
    }

}
