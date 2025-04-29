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
public class PanelCategory {
    private String benefitIssueCode;
    private String category;
    private String specialismCount;
    private String fqpm;
    private String medicalMember;
    private List<String> johTiers;

    public PanelCategory(String benefitIssueCode, String specialismCount, String fqpm, String medicalMember) {
        this.benefitIssueCode = benefitIssueCode;
        this.specialismCount = specialismCount;
        this.fqpm = fqpm;
        this.medicalMember = medicalMember;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PanelCategory that = (PanelCategory) o;
        return Objects.equals(benefitIssueCode, that.benefitIssueCode)
                && Objects.equals(specialismCount, that.specialismCount)
                && Objects.equals(fqpm, that.fqpm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(benefitIssueCode, specialismCount, fqpm);
    }

}
