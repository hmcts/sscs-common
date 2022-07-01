package uk.gov.hmcts.reform.sscs.reference.data.service;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.*;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.reference.data.model.Language;

@Getter
@Setter
@Component
public class VerbalLanguagesService {
    private static final String JSON_DATA_LOCATION = "reference-data/verbal-languages.json";
    public static final String VERBAL_LANGUAGE_REFERENCE_TEMPLATE = "%s%s";
    public static final String VERBAL_LANGUAGE_DIALECT_TEMPLATE = "-%s";

    private List<Language> verbalLanguages;
    private Map<Language, Language> verbalLanguagesHashMap;
    private Map<String, Language> verbalLanguagesCcdRefHashMap;

    public VerbalLanguagesService() {
        verbalLanguages = getReferenceData(JSON_DATA_LOCATION, new TypeReference<>() {});
        verbalLanguagesHashMap = generateHashMap(verbalLanguages);
        verbalLanguagesCcdRefHashMap = generateHashMapList(verbalLanguages);
    }

    public Language getByHmcReference(String reference, String dialectReference) {
        return verbalLanguagesHashMap.get(new Language(reference, dialectReference));
    }

    public Language getVerbalLanguage(String language) {
        if (isBlank(language)) {
            return null;
        }
        return verbalLanguagesCcdRefHashMap.get(language.toLowerCase(Locale.ROOT));
    }

    public String getVerbalLanguageReference(String language) {
        return getVerbalLanguageReference(getVerbalLanguage(language));
    }

    public String getVerbalLanguageReference(Language language) {
        if (isNull(language)) {
            return null;
        }
        return String.format(VERBAL_LANGUAGE_REFERENCE_TEMPLATE,
                language.getReference(), getDialectReference(language));
    }

    private String getDialectReference(Language language) {
        if (isBlank(language.getDialectReference())) {
            return "";
        }
        return String.format(VERBAL_LANGUAGE_DIALECT_TEMPLATE, language.getDialectReference());
    }
}
