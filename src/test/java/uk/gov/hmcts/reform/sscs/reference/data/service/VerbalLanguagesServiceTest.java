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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @DisplayName("There should be an entry for every language")
    @ParameterizedTest
    @CsvSource({"Acholi","Acholi","African","Afrikaans","Akan","Albanian","Algerian","Amharic","Arabic","Arabic",
            "Armenian","Assyrian","Ateso","Azerbajani (aka Nth Azari)","Bajan (West Indian)","Baluchi","Bambara",
            "Bardini","Bassa","Belorussian","Benba (Bemba)","Bengali","Benin/Edo","Berber","Bhutanese","Bihari","Bilin",
            "Bravanese","Brong","Bulgarian","Burmese","Cambellpuri","Cantonese","Cebuano","Chechen","Chichewa",
            "Chittagonain","Creole (English)","Creole (French)","Creole (Portuguese)","Creole (Spanish)","Czech",
            "Danish","Dari","Dinka","Dioula","Douala","Dutch","Efik","Esan","Estonian","Ewe","Ewondo","Fanti","Farsi",
            "Feyli","Fijian","Flemish","French","Fula","Ga","Gallacian","Georgian","German","Gorani","Greek","Gujarati",
            "Gurage","Hakka","Hausa","Hebrew","Herero","Hindi","Hindko","Hokkien","Hungarian","Ibibio","Igbo (Also Known As Ibo)",
            "Ilocano","Indian","Indonesian","Isoko","Italian","Jamaican","Japanese","Javanese","Kachi","Karon","Kashmiri",
            "Kenyan", "Khalanga","Khmer","Kibanjuni","Kichagga","Kikongo","Kikuyu","Kinyarwandan","Kirundi","Kiswahili",
            "Konkani","Korean", "Krio (Sierra Leone)","Kru","Kurmanji","Kutchi","Kyrgyz","Lango","Lango","Latvian","Lingala",
            "Lithuanian", "Luba (Tshiluba)","Lugandan","Luo","Lusoga","Macedonian","Maghreb","Malay","Malayalam","Maldivian",
            "Malinke", "Maltese","Mandarin","Mandinka","Marathi","Masaaba","Mende","Middle Eastern","Mina","Moldovan","Mongolian",
            "Monokutuba","Ndebele","Nepali","North African","Northern Hindko","Norwegian","Nyankole","Nzima","Oromo",
            "Pahari","Pakistani","Pampangan","Pangasinan","Pathwari","Patois","Polish","Portuguese","Pothohari","Punjabi",
            "Pushtu (Also Known As Pashto)","Roma","Romanian","Romany","Rukiga","Runyoro","Russian","Rutoro","Sakata",
            "Sarahuleh","Saraiki (Seraiki)","Sarpo","Senegal (French)","Serbo-Croatian","Setswana","Shina","Shona",
            "Sindhi","Sinhalese","Slovak","Slovenian","Somali","Sorani","Spanish","Susu","Swahili","Swedish","Sylheti",
            "Sylheti","Tagalog","Taiwanese","Tamil","Telugu","Temne","Thai","Tibetan","Tigre","Tigrinya","Toura",
            "Turkish","Turkmen","Twi","Uighur","Ukrainian","Urdu","Urohobo","Uzbek","Vietnamese","Visayan","Welsh",
            "West Flemish","Wolof","Xhosa","Yoruba","Zaghawa","Zaza","Zulu"})
    public void testVerbalLanguagesMap(String language) {
        Language languageResult = languagesService.getVerbalLanguage(language);
        assertThat(languageResult).isNotNull();
    }
}
