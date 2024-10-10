package uk.gov.hmcts.reform.sscs.reference.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static java.util.Objects.nonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Language implements ReferenceList {
    private String reference;
    private String nameEn;
    private String mrdReference;
    private String dialectReference;
    private String dialectEn;
    @JsonProperty("ccdReferences")
    private List<String> referenceList;

    public Language(String reference) {
        this.reference = reference;
    }

    public Language(String reference, String dialectReference) {
        this.reference = reference;
        this.dialectReference = dialectReference;
    }

    public String getFullReference() {
        String fullReference = reference;
        if (nonNull(dialectReference)) {
            fullReference = String.format("%s-%s", reference, dialectReference);
        }

        if (nonNull(mrdReference)) {
            fullReference = mrdReference;
        }
        return fullReference;
    }

    public String getName() {
        if (nonNull(dialectReference)) {
            return dialectEn;
        }
        return nameEn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Language language = (Language) o;

        boolean referenceMatched = reference.equals(language.reference);
        String languageDialectReference = language.dialectReference;
        String languageMrdReference = language.mrdReference;

        if (languageDialectReference != null) {
            return referenceMatched && dialectReference.equals(languageDialectReference);
        } else if (languageMrdReference != null) {
            return referenceMatched && mrdReference.equals(languageMrdReference);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference, dialectReference);
    }
}
