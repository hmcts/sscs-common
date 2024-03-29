package uk.gov.hmcts.reform.sscs.reference.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;
import uk.gov.hmcts.reform.sscs.ccd.domain.SessionCategory;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class SessionCategoryMap {

    private BenefitCode benefitCode;
    private Issue issue;
    private boolean secondDoctor;
    private boolean fqpmRequired;
    private SessionCategory category;
    private Integer ticketOverride;

    public SessionCategoryMap(BenefitCode benefitCode, Issue issue,
                              boolean secondDoctor, boolean fqpmRequired) {
        this.benefitCode = benefitCode;
        this.issue = issue;
        this.secondDoctor = secondDoctor;
        this.fqpmRequired = fqpmRequired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SessionCategoryMap that = (SessionCategoryMap) o;
        return secondDoctor == that.secondDoctor
                && fqpmRequired == that.fqpmRequired
                && benefitCode == that.benefitCode
                && issue == that.issue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(benefitCode, issue, secondDoctor, fqpmRequired);
    }
}
