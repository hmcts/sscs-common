package uk.gov.hmcts.reform.sscs.reference.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
@Slf4j
public class PanelCategoryMap {
    private String benefitIssueCode;
    private String category;
    private String specialism;
    private String fqpm;
    private ArrayList<String> johTier;

    public PanelCategoryMap(String benefitIssueCode, String category, String specialism, String fqpm) {
        this.benefitIssueCode = benefitIssueCode;
        this.category = category;
        this.specialism = specialism;
        this.fqpm = fqpm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PanelCategoryMap that = (PanelCategoryMap) o;
        return Objects.equals(benefitIssueCode, that.benefitIssueCode)
                && Objects.equals(category, that.category)
                && Objects.equals(specialism, that.specialism)
                && Objects.equals(fqpm, that.fqpm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(benefitIssueCode, category, specialism, fqpm);
    }

}
