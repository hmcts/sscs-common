package uk.gov.hmcts.reform.sscs.reference.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
@Slf4j
public class PanelCategoryMap {
    private String benefitIssueCode;
    private String category;
    private String specialismCount;
    private String fqpm;
    private List<String> johTiers;

    public PanelCategoryMap(String benefitIssueCode, String specialism, String fqpm) {
        this.benefitIssueCode = benefitIssueCode;
        this.specialismCount = specialism;
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
                && Objects.equals(specialismCount, that.specialismCount)
                && Objects.equals(fqpm, that.fqpm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(benefitIssueCode, specialismCount, fqpm);
    }

}
