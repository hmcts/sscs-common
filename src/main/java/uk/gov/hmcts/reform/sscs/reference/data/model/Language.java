package uk.gov.hmcts.reform.sscs.reference.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Language implements ReferenceList {
    private String reference;
    private String nameEn;
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
