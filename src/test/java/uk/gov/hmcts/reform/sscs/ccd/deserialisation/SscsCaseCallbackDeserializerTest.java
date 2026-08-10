package uk.gov.hmcts.reform.sscs.ccd.deserialisation;

import static com.fasterxml.jackson.databind.DeserializationFeature.READ_ENUMS_USING_TO_STRING;
import static com.fasterxml.jackson.databind.DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_ENUMS_USING_TO_STRING;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.sscs.ccd.callback.Callback;
import uk.gov.hmcts.reform.sscs.ccd.domain.*;

@Slf4j
@ExtendWith(SpringExtension.class)
public class SscsCaseCallbackDeserializerTest {

    private ObjectMapper mapper;

    @Mock
    private ObjectMapper mockedMapper;

    private SscsCaseCallbackDeserializer sscsCaseCallbackDeserializer;

    @BeforeEach
    public void setUp() {

        Jackson2ObjectMapperBuilder objectMapperBuilder =
            new Jackson2ObjectMapperBuilder()
                .featuresToEnable(READ_ENUMS_USING_TO_STRING)
                .featuresToEnable(READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
                .featuresToEnable(WRITE_ENUMS_USING_TO_STRING)
                .serializationInclusion(JsonInclude.Include.NON_ABSENT);

        mapper = objectMapperBuilder.createXmlMapper(false).build();
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        mapper.findAndRegisterModules();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "reissueFurtherEvidenceCallbackWithEmptyDynamicList.json",
        "reissueFurtherEvidenceCallbackWithDynamicListAsPerAboutToStartCallback.json",
        "reissueFurtherEvidenceCallbackWithDynamicListAsPerMiddleEventCallback.json"
    })
    public void givenMiddleOrAnyOtherEventCallback_shouldDeserializeDynamicListCorrectly(String callbackFilename)
        throws IOException {
        sscsCaseCallbackDeserializer = new SscsCaseCallbackDeserializer(mapper);
        String file = Objects.requireNonNull(getClass().getClassLoader().getResource(
            "reissueFurtherEvidence/" + callbackFilename)).getFile();
        String json = FileUtils.readFileToString(new File(file), StandardCharsets.UTF_8.name());

        Callback<SscsCaseData> actualSscsCaseCallback = null;
        try {
            actualSscsCaseCallback = sscsCaseCallbackDeserializer.deserialize(json);
        } catch (Exception e) {
            fail("no expected exception here");
        }
        DynamicList reissueFurtherEvidenceDocument = actualSscsCaseCallback.getCaseDetails().getCaseData()
                .getReissueArtifactUi().getReissueFurtherEvidenceDocument();
        log.info(reissueFurtherEvidenceDocument.toString());
    }

    @Test
    public void givenAdminAppealWithdrawnEvent_shouldDeserializeAppealNotePad() throws IOException {
        sscsCaseCallbackDeserializer = new SscsCaseCallbackDeserializer(mapper);
        String file = Objects.requireNonNull(getClass().getClassLoader().getResource(
            "adminAppealWithdrawnCallback.json")).getFile();
        String json = FileUtils.readFileToString(new File(file), StandardCharsets.UTF_8.name());

        Callback<SscsCaseData> actualSscsCaseCallback = sscsCaseCallbackDeserializer.deserialize(json);

        NotePad expectedAppealNotePad = NotePad.builder()
            .notesCollection(Collections.singletonList(Note.builder()
                .value(NoteDetails.builder()
                    .noteDate("2019-09-01")
                    .noteDetail("this is a withdrawn note")
                    .build())
                .build()))
            .build();
        assertEquals(expectedAppealNotePad, actualSscsCaseCallback.getCaseDetails().getCaseData().getAppealNotePad());
    }

    @Test
    public void should_deserialize_callback_source_to_sscs_case_callback_with_latest_event() throws IOException {
        sscsCaseCallbackDeserializer = new SscsCaseCallbackDeserializer(mapper);

        String path = getClass().getClassLoader().getResource("responseReceivedCallback.json").getFile();
        String json = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8.name());

        Callback<SscsCaseData> actualSscsCaseCallback = sscsCaseCallbackDeserializer.deserialize(json);

        assertEquals(EventType.DWP_RESPOND, actualSscsCaseCallback.getEvent());
        assertEquals(State.APPEAL_CREATED, actualSscsCaseCallback.getCaseDetails().getState());
        assertEquals(EventType.DWP_RESPOND, actualSscsCaseCallback.getCaseDetails().getCaseData().getEvents().get(0).getValue().getEventType());
        assertEquals("12345656789", actualSscsCaseCallback.getCaseDetails().getCaseData().getCcdCaseId());
        assertEquals(Optional.of(Benefit.ESA), actualSscsCaseCallback.getCaseDetails().getCaseData().getBenefitType());
        assertTrue(actualSscsCaseCallback.getCaseDetails().getCaseData().isBenefitType(Benefit.ESA));
        assertEquals("Benefit", actualSscsCaseCallback.getCaseDetails().getCaseTypeId());

    }

    @Test
    public void should_deserialize_callback_and_sort_hearings_and_evidence_by_latest_date() throws IOException {
        sscsCaseCallbackDeserializer = new SscsCaseCallbackDeserializer(mapper);

        String path = getClass().getClassLoader().getResource("responseReceivedCallbackMultipleHearingsAndEvidence.json").getFile();
        String json = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8.name());

        Callback<SscsCaseData> actualSscsCaseCallback = sscsCaseCallbackDeserializer.deserialize(json);

        List<Hearing> hearings = actualSscsCaseCallback.getCaseDetails().getCaseData().getHearings();

        assertEquals("2018-07-01", hearings.get(0).getValue().getHearingDate());
        assertEquals("2018-06-01", hearings.get(1).getValue().getHearingDate());
        assertEquals("2018-05-01", hearings.get(2).getValue().getHearingDate());

        Evidence evidence = actualSscsCaseCallback.getCaseDetails().getCaseData().getEvidence();

        assertEquals("2019-04-12", evidence.getDocuments().get(0).getValue().getDateReceived());
        assertEquals("2019-03-10", evidence.getDocuments().get(1).getValue().getDateReceived());
        assertEquals("2019-02-12", evidence.getDocuments().get(2).getValue().getDateReceived());
    }

    @Test
    public void should_convert_checked_exception_to_runtime_on_error() throws IOException {
        sscsCaseCallbackDeserializer = new SscsCaseCallbackDeserializer(mockedMapper);

        String source = "callback";

        doThrow(mock(JsonProcessingException.class))
            .when(mockedMapper)
            .readValue(eq(source), isA(TypeReference.class));

        assertThatThrownBy(() -> sscsCaseCallbackDeserializer.deserialize(source))
            .hasMessage("Could not deserialize callback")
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void should_deserialize_appeal_to_proceed_callback() throws IOException {
        sscsCaseCallbackDeserializer = new SscsCaseCallbackDeserializer(mapper);

        String path = getClass().getClassLoader().getResource("decisionAppealToProceed.json").getFile();
        String json = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8.name());

        Callback<SscsCaseData> actualSscsCaseCallback = sscsCaseCallbackDeserializer.deserialize(json);

        SscsInterlocDecisionDocument sscsInterlocDecisionDocument = actualSscsCaseCallback.getCaseDetails().getCaseData().getSscsInterlocDecisionDocument();

        assertEquals("2019-06-26", sscsInterlocDecisionDocument.getDocumentDateAdded().toString());
        assertEquals("DecisionNotice.pdf", sscsInterlocDecisionDocument.getDocumentFileName());
        assertEquals("Decision Notice", sscsInterlocDecisionDocument.getDocumentType());
        assertEquals("2019-06-26",
            actualSscsCaseCallback.getCaseDetails().getCaseData().getDocumentStaging().getDateAdded().toString());
        assertEquals(FormType.SSCS1PE, actualSscsCaseCallback.getCaseDetails().getCaseData().getFormType());

        assertDwpDocumentCollectionDates(actualSscsCaseCallback);
    }

    private void assertDwpDocumentCollectionDates(Callback<SscsCaseData> actualSscsCaseCallback) {
        assertEquals(4, actualSscsCaseCallback.getCaseDetails().getCaseData().getDwpDocuments().size());
        assertEquals("AT38-1", actualSscsCaseCallback.getCaseDetails().getCaseData().getDwpDocuments().get(3).getValue().getDocumentFileName());
        assertEquals("2021-02-08T13:30:29.123", actualSscsCaseCallback.getCaseDetails().getCaseData().getDwpDocuments().get(3).getValue().getDocumentDateTimeAdded().toString());
        assertEquals("AT38-2", actualSscsCaseCallback.getCaseDetails().getCaseData().getDwpDocuments().get(2).getValue().getDocumentFileName());
        assertEquals("2021-02-08T13:00", actualSscsCaseCallback.getCaseDetails().getCaseData().getDwpDocuments().get(2).getValue().getDocumentDateTimeAdded().toString());
        assertEquals("AT38-3", actualSscsCaseCallback.getCaseDetails().getCaseData().getDwpDocuments().get(1).getValue().getDocumentFileName());
        assertEquals("2021-02-08T12:00", actualSscsCaseCallback.getCaseDetails().getCaseData().getDwpDocuments().get(1).getValue().getDocumentDateTimeAdded().toString());
        assertEquals("AT38-4", actualSscsCaseCallback.getCaseDetails().getCaseData().getDwpDocuments().get(0).getValue().getDocumentFileName());
        assertNull(actualSscsCaseCallback.getCaseDetails().getCaseData().getDwpDocuments().get(0).getValue().getDocumentDateTimeAdded());
        assertEquals("2021-02-08", actualSscsCaseCallback.getCaseDetails().getCaseData().getDwpDocuments().get(0).getValue().getDocumentDateAdded());
    }

    @Test
    public void should_deserialize_struck_out_callback() throws IOException {
        sscsCaseCallbackDeserializer = new SscsCaseCallbackDeserializer(mapper);

        String path = getClass().getClassLoader().getResource("struckOut.json").getFile();
        String json = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8.name());

        Callback<SscsCaseData> actualSscsCaseCallback = sscsCaseCallbackDeserializer.deserialize(json);

        SscsStrikeOutDocument sscsStrikeOutDocument = actualSscsCaseCallback.getCaseDetails().getCaseData().getSscsStrikeOutDocument();

        assertEquals("2019-06-26", sscsStrikeOutDocument.getDocumentDateAdded());
        assertEquals("StruckOutNotice.pdf", sscsStrikeOutDocument.getDocumentFileName());
        assertEquals("Struck Out Notice", sscsStrikeOutDocument.getDocumentType());
    }

    @Test
    public void should_deserialize_direction_issued_callback() throws IOException {
        sscsCaseCallbackDeserializer = new SscsCaseCallbackDeserializer(mapper);

        String file = Objects.requireNonNull(getClass().getClassLoader().getResource("directionIssued.json")).getFile();
        String json = FileUtils.readFileToString(new File(file), StandardCharsets.UTF_8.name());

        Callback<SscsCaseData> actualSscsCaseCallback = sscsCaseCallbackDeserializer.deserialize(json);

        SscsInterlocDirectionDocument sscsInterlocDirectionDocument = actualSscsCaseCallback.getCaseDetails().getCaseData().getSscsInterlocDirectionDocument();

        assertEquals("2019-06-26", sscsInterlocDirectionDocument.getDocumentDateAdded().toString());
        assertEquals("DirectionNotice.pdf", sscsInterlocDirectionDocument.getDocumentFileName());
        assertEquals("Direction Notice", sscsInterlocDirectionDocument.getDocumentType());
        assertEquals("2019-06-26", actualSscsCaseCallback.getCaseDetails().getCaseData().getDocumentStaging().getDateAdded().toString());

    }

    @Test
    public void should_deserialize_update_further_evidence_callback() throws IOException {
        sscsCaseCallbackDeserializer = new SscsCaseCallbackDeserializer(mapper);

        String path = getClass().getClassLoader().getResource("updateFurtherEvidence.json").getFile();
        String json = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8.name());

        Callback<SscsCaseData> actualSscsCaseCallback = sscsCaseCallbackDeserializer.deserialize(json);

        DirectionResponse directionResponse = actualSscsCaseCallback.getCaseDetails().getCaseData().getDirectionResponse();

        assertEquals("AdditionalEvidence.pdf", directionResponse.getDirectionResponses().get(0).getValue().getDocumentLink().getDocumentFilename());
        assertEquals("http://dm-store:4506/documents/5f574d09-1590-446e-bc02-1f2437688390", directionResponse.getDirectionResponses().get(0).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("http://dm-store:4506/documents/5f574d09-1590-446e-bc02-1f2437688390/binary", directionResponse.getDirectionResponses().get(0).getValue().getDocumentLink().getDocumentBinaryUrl());
        assertEquals(Optional.of(Benefit.PIP), actualSscsCaseCallback.getCaseDetails().getCaseData().getBenefitType());
        assertTrue(actualSscsCaseCallback.getCaseDetails().getCaseData().isBenefitType(Benefit.PIP));
        assertFalse(actualSscsCaseCallback.getCaseDetails().getCaseData().isBenefitType(Benefit.ESA));

    }

    @ParameterizedTest
    @ValueSource(strings = {"adminAppealWithdrawnCallback.json", "updateFurtherEvidence.json"})
    public void should_deserialise_and_serialise(final String jsonFileName) throws IOException, JSONException {
        sscsCaseCallbackDeserializer = new SscsCaseCallbackDeserializer(mapper);

        String path = getClass().getClassLoader().getResource(jsonFileName).getFile();
        String json = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8.name());
        Callback<SscsCaseData> actualSscsCaseCallback = sscsCaseCallbackDeserializer.deserialize(json);

        String valueAsString = mapper.writeValueAsString(actualSscsCaseCallback);

        JSONAssert.assertEquals(valueAsString, json,  JSONCompareMode.LENIENT);
    }
}
