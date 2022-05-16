package uk.gov.hmcts.reform.sscs.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class JsonDataReader {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonDataReader() {

    }

    public static InputStream getResource(String filename) throws IOException {
        return new ClassPathResource(filename).getInputStream();
    }

    public static <T> T importObjectDataList(String filename, TypeReference<T> typeReference) throws IOException {
        return OBJECT_MAPPER.readValue(getResource(filename), typeReference);
    }
}
