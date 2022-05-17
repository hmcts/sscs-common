package uk.gov.hmcts.reform.sscs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Language {
    private String reference;
    private String nameEn;
    private String dialectReference;
    private String dialectEn;
    private List<String> ccdReferences;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Language language = (Language) o;
        return reference.equals(language.reference) && dialectReference.equals(language.dialectReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference, dialectReference);
    }
}
