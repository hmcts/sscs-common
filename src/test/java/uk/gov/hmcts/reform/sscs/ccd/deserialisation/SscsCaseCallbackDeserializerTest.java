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
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import uk.gov.hmcts.reform.sscs.ccd.callback.Callback;
import uk.gov.hmcts.reform.sscs.ccd.domain.EventType;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.State;

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
    public void should_deserialize_callback_source_to_sscs_case_callback() throws IOException {
        sscsCaseCallbackDeserializer = new SscsCaseCallbackDeserializer(mapper);

        String path = getClass().getClassLoader().getResource("appealReceivedCallback.json").getFile();
        String json = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8.name());

        Callback<SscsCaseData> actualSscsCaseCallback = sscsCaseCallbackDeserializer.deserialize(json);

        assertEquals(EventType.APPEAL_RECEIVED, actualSscsCaseCallback.getEvent());
        assertEquals(State.APPEAL_CREATED, actualSscsCaseCallback.getCaseDetails().getState());
        assertEquals("12345656789", actualSscsCaseCallback.getCaseDetails().getCaseData().getCcdCaseId());
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
}
