package uk.gov.hmcts.reform.sscs.reference.data.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class LanguageTest {

    @Test
    void shouldGetFullReference_GivenNullMrdAndDialectReference() {
        var language = new Language("ara", null);

        assertEquals("ara", language.getFullReference());
    }

    @Test
    void shouldGetFullReference_GivenDialectReference() {
        var language = new Language("ara", "syrian");

        assertEquals("ara-syrian", language.getFullReference());
    }

    @Test
    void shouldGetFullReference_GivenMrdReference() {
        var language = new Language("slo", null);
        ReflectionTestUtils.setField(language, "mrdReference", "slk");

        assertEquals("slk", language.getFullReference());
    }

    @Test
    void shouldGetName_GivenNullDialectReference() {
        var language = new Language("ara", null);
        ReflectionTestUtils.setField(language, "dialectEn", "syrian");
        ReflectionTestUtils.setField(language, "nameEn", "arabic");

        assertEquals("arabic", language.getName());
    }

    @Test
    void shouldGetName_GivenDialectReference() {
        var language = new Language("ara", "egy");
        ReflectionTestUtils.setField(language, "dialectEn", "egyptian");
        ReflectionTestUtils.setField(language, "nameEn", "arabic");

        assertEquals("egyptian", language.getName());
    }
}
