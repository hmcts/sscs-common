package uk.gov.hmcts.reform.sscs.ccd.deserialisation;

import static com.fasterxml.jackson.databind.DeserializationFeature.READ_ENUMS_USING_TO_STRING;
import static com.fasterxml.jackson.databind.DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_ENUMS_USING_TO_STRING;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import uk.gov.hmcts.reform.sscs.ccd.callback.Callback;
import uk.gov.hmcts.reform.sscs.ccd.domain.*;

@RunWith(MockitoJUnitRunner.class)
public class SscsCaseCallbackDeserializerTest {

    private ObjectMapper mapper;

    @Mock
    private ObjectMapper mockedMapper;

    private SscsCaseCallbackDeserializer sscsCaseCallbackDeserializer;

    @Before
    public void setUp() {

        Jackson2ObjectMapperBuilder objectMapperBuilder =
                new Jackson2ObjectMapperBuilder()
                        .featuresToEnable(READ_ENUMS_USING_TO_STRING)
                        .featuresToEnable(READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
                        .featuresToEnable(WRITE_ENUMS_USING_TO_STRING)
                        .serializationInclusion(JsonInclude.Include.NON_ABSENT);

        mapper = objectMapperBuilder.createXmlMapper(false).build();
        mapper.registerModule(new JavaTimeModule());
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

        assertEquals("2019-06-26", sscsInterlocDecisionDocument.getDocumentDateAdded());
        assertEquals("DecisionNotice.pdf", sscsInterlocDecisionDocument.getDocumentFileName());
        assertEquals("Decision Notice", sscsInterlocDecisionDocument.getDocumentType());
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
    public void should_deserialize_direction_issed_callback() throws IOException {
        sscsCaseCallbackDeserializer = new SscsCaseCallbackDeserializer(mapper);

        String path = getClass().getClassLoader().getResource("directionIssued.json").getFile();
        String json = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8.name());

        Callback<SscsCaseData> actualSscsCaseCallback = sscsCaseCallbackDeserializer.deserialize(json);

        SscsInterlocDirectionDocument sscsInterlocDirectionDocument = actualSscsCaseCallback.getCaseDetails().getCaseData().getSscsInterlocDirectionDocument();

        assertEquals("2019-06-26", sscsInterlocDirectionDocument.getDocumentDateAdded());
        assertEquals("DirectionNotice.pdf", sscsInterlocDirectionDocument.getDocumentFileName());
        assertEquals("Direction Notice", sscsInterlocDirectionDocument.getDocumentType());
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
    }
}
