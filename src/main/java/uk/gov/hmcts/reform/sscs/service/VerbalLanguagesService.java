package uk.gov.hmcts.reform.sscs.service;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static uk.gov.hmcts.reform.sscs.helper.ReferenceDataHelper.*;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.model.Language;

@Getter
@Setter
@Component
public class VerbalLanguagesService {
    private static final String JSON_DATA_LOCATION = "reference-data/verbal-languages.json";
    public static final String VERBAL_LANGUAGE_REFERENCE_TEMPLATE = "%s-%s";

    private List<Language> verbalLanguages;
    private Map<String, Language> verbalLanguagesHashMap;

    public VerbalLanguagesService() {
        verbalLanguages = getReferenceData(JSON_DATA_LOCATION, new TypeReference<>() {});
        verbalLanguagesHashMap = verbalLanguages.stream()
                .flatMap(language ->
                        language.getCcdReferences().stream()
                        .map(ccdReference ->
                                Map.entry(ccdReference.toLowerCase(), language)))
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
    }

    public Language getVerbalLanguage(String language) {
        return verbalLanguagesHashMap.get(language);
    }

    public String getVerbalLanguageReference(String language) {
        return getVerbalLanguageReference(getVerbalLanguage(language));
    }

    public String getVerbalLanguageReference(Language language) {
        return String.format(VERBAL_LANGUAGE_REFERENCE_TEMPLATE,
                language.getReference(), getDialectReference(language));
    }

    private String getDialectReference(Language language) {
        if (isBlank(language.getDialectReference())) {
            return language.getReference();
        }
        return language.getDialectReference();
    }
}
