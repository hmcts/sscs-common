package uk.gov.hmcts.reform.sscs.ccd.deserialisation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.gov.hmcts.reform.sscs.ccd.callback.Callback;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;

@RunWith(MockitoJUnitRunner.class)
public class SscsCaseCallbackDeserializerTest {

    @Mock
    private ObjectMapper mapper;
    @Mock
    private Callback<SscsCaseData> expectedSscsCaseCallback;

    private SscsCaseCallbackDeserializer sscsCaseCallbackDeserializer;

    @Before
    public void setUp() {
        sscsCaseCallbackDeserializer = new SscsCaseCallbackDeserializer(mapper);
    }

    @Test
    public void should_deserialize_callback_source_to_sscs_case_callback() throws IOException {

        String source = "callback";

        doReturn(expectedSscsCaseCallback)
            .when(mapper)
            .readValue(eq(source), isA(TypeReference.class));

        Callback<SscsCaseData> actualSscsCaseCallback = sscsCaseCallbackDeserializer.deserialize(source);

        assertEquals(expectedSscsCaseCallback, actualSscsCaseCallback);
    }

    @Test
    public void should_convert_checked_exception_to_runtime_on_error() throws IOException {

        String source = "callback";

        doThrow(mock(JsonProcessingException.class))
            .when(mapper)
            .readValue(eq(source), isA(TypeReference.class));

        assertThatThrownBy(() -> sscsCaseCallbackDeserializer.deserialize(source))
            .hasMessage("Could not deserialize callback")
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
