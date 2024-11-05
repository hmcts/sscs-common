package uk.gov.hmcts.reform.sscs.ccd.domain;

import static com.fasterxml.jackson.databind.DeserializationFeature.READ_ENUMS_USING_TO_STRING;
import static com.fasterxml.jackson.databind.DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_ENUMS_USING_TO_STRING;
import static org.junit.Assert.assertFalse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class GlobalSearchOtherReferencesTest {

    private LocalDate now = LocalDate.now();
    private ObjectMapper mapper;

    @Before
    public void setUp() {

        Jackson2ObjectMapperBuilder objectMapperBuilder =
                new Jackson2ObjectMapperBuilder()
                        .featuresToEnable(READ_ENUMS_USING_TO_STRING)
                        .featuresToEnable(READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
                        .featuresToEnable(WRITE_ENUMS_USING_TO_STRING)
                        .serializationInclusion(JsonInclude.Include.NON_ABSENT);

        mapper = objectMapperBuilder.createXmlMapper(false).build();
        mapper.findAndRegisterModules();
    }

    @Test
    public void otherReferencesForGlobalSearchNotPresentWhenNull() throws JsonProcessingException {
        Identity identity = Identity.builder().dob("1980-08-12").nino(null).build();
        Appellant appellant = Appellant.builder().identity(identity).build();
        Appeal appeal = Appeal.builder().appellant(appellant).build();
        SscsCaseData caseData = SscsCaseData.builder().appeal(appeal).caseReference(null).build();
        String jsonCaseData = mapper.writeValueAsString(caseData);

        assertFalse(jsonCaseData.contains("caseReference"));
        assertFalse(jsonCaseData.contains("nino"));
    }
}
