package uk.gov.hmcts.reform.sscs.utility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;
import org.junit.jupiter.api.Test;


class TrimStringFieldsTest {

    TrimStringFields underTest = new TrimStringFields();
    JsonParser jp = mock(JsonParser.class);
    DeserializationContext dc = mock(DeserializationContext.class);

    @Test
    void shouldTrimStringsFields() throws IOException {

        // given
        JsonNode jsonNode = new TextNode("test   ");
        when(jp.readValueAsTree()).thenReturn(jsonNode);

        // when
        String deserializedText = (String) underTest.deserialize(jp, dc);

        // then
        assertThat(deserializedText).isEqualTo("test");
    }

    @Test
    void shouldReturnNullIfTrimStringsFieldsFails() throws IOException {

        // given
        when(jp.readValueAsTree()).thenThrow(new IOException("something went wrong"));

        // when
        String deserializedText = (String) underTest.deserialize(jp, dc);

        // then
        assertThat(deserializedText).isNull();
    }
}
