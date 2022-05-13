package uk.gov.hmcts.reform.sscs.model;

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
public class SessionCategoryMap implements Hashable {

    private BenefitCode benefitCode;
    private Issue issue;
    private boolean secondDoctor;
    private boolean fqpmRequired;
    private SessionCategory category;
    private Integer ticketOverride;

    private static final String SERVICE_CODE = "BBA3";

    public Integer getHash() {
        return getHash(benefitCode, issue, secondDoctor, fqpmRequired);
    }

    public static Integer getHash(BenefitCode benefitCode, Issue issue, boolean secondDoctor, boolean fqpmRequired) {
        return Objects.hash(benefitCode, issue, secondDoctor, fqpmRequired);
    }

    public String getCategoryTypeValue() {
        return String.format("%s-%03d", SERVICE_CODE, benefitCode.getCcdReference());
    }

    public String getCategorySubTypeValue() {
        return String.format("%s%s", getCategoryTypeValue(), issue.name());
    }


}
