package uk.gov.hmcts.reform.sscs.service;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static uk.gov.hmcts.reform.sscs.helper.ReferenceDataHelper.*;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.model.Language;

@Getter
@Setter
@Component
public class SignLanguagesService {
    private static final String JSON_DATA_LOCATION = "reference-data/sign-languages.json";
    public static final String SIGN_LANGUAGE_REFERENCE_TEMPLATE = "%s%s";
    public static final String SIGN_DIALECT_TEMPLATE = "-%s";

    private List<Language> signLanguages;
    private Map<Language, Language> signLanguagesHashMap;
    private Map<String, Language> signLanguagesCcdRefHashMap;

    public SignLanguagesService() {
        signLanguages = getReferenceData(JSON_DATA_LOCATION, new TypeReference<>() {});
        signLanguagesHashMap = generateHashMap(signLanguages);
        signLanguagesCcdRefHashMap = generateHashMapList(signLanguages);
    }

    public Language getLanguageByHmcReference(String reference) {
        return signLanguagesHashMap.get(new Language(reference));
    }

    public Language getSignLanguage(String language) {
        return signLanguagesCcdRefHashMap.get(language);
    }

    public String getSignLanguageReference(String language) {
        return getSignLanguageReference(getSignLanguage(language));
    }

    public String getSignLanguageReference(Language language) {
        return String.format(SIGN_LANGUAGE_REFERENCE_TEMPLATE,
                language.getReference(), getDialectReference(language));
    }

    private String getDialectReference(Language language) {
        if (isBlank(language.getDialectReference())) {
            return "";
        }
        return String.format(SIGN_DIALECT_TEMPLATE,language.getDialectReference());
    }
}
