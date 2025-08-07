package uk.gov.hmcts.reform.sscs.reference.data.service;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.getDuplicates;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sscs.reference.data.model.Language;

public class VerbalLanguagesServiceTest {

    VerbalLanguagesService languagesService;

    @BeforeEach
    public void setup() {
        languagesService = new VerbalLanguagesService();
    }

    @DisplayName("There should be no duplicate Verbal Languages")
    @Test
    public void testVerbalLanguagesDuplicates() {
        List<Language> languagesList = languagesService.getVerbalLanguages();
        Set<Language> result = new HashSet<>(languagesList);

        assertThat(result)
                .withFailMessage("There are the following duplicates:\n%s",
                        getDuplicates(languagesList))
                .hasSameSizeAs(languagesList);
    }

    @DisplayName("There should be no duplicates in the reference lists")
    @Test
    public void testVerbalLanguagesReferenceListDuplicates() {
        List<String> referencesList = languagesService.getVerbalLanguages()
                .stream()
                .map(Language::getReferenceList)
                .flatMap(Collection::stream)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        Set<String> result = new HashSet<>(referencesList);

        assertThat(result)
                .withFailMessage("There are the following duplicates:\n%s",
                        getDuplicates(referencesList))
                .hasSameSizeAs(referencesList);
    }
}
