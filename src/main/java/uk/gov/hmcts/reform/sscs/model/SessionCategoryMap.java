package uk.gov.hmcts.reform.sscs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;
import uk.gov.hmcts.reform.sscs.ccd.domain.SessionCategory;

@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class SessionCategoryMap extends ReferenceData {
    private BenefitCode benefitCode;
    private Issue issue;
    private boolean secondDoctor;
    private boolean fqpmRequired;
    private SessionCategory category;
    private Integer ticketOverride;

    private static final String SERVICE_CODE = "BBA3";

    public static final String JSON_DATA_LOCATION = "reference-data/session-category-map.json";

    private static List<SessionCategoryMap> sessionCategoryMaps;
    private static Map<Integer, SessionCategoryMap> hashMap;

    static {
        sessionCategoryMaps = getReferenceData(JSON_DATA_LOCATION, new TypeReference<>() {});
        hashMap = generateHashMap(sessionCategoryMaps);
    }

    public static SessionCategoryMap getSessionCategory(String benefitCode, String issueCode, boolean secondDoctor, boolean fqpmRequired) {
        return getSessionCategory(BenefitCode.getBenefitCode(benefitCode), Issue.getIssue(issueCode), secondDoctor, fqpmRequired);
    }

    public static SessionCategoryMap getSessionCategory(BenefitCode benefitCode, Issue issue, boolean secondDoctor, boolean fqpmRequired) {
        return hashMap.get(getHash(benefitCode, issue, secondDoctor, fqpmRequired));
    }

    public String getCategoryTypeValue() {
        return String.format("%s-%03d", SERVICE_CODE, benefitCode.getCcdReference());
    }

    public String getCategorySubTypeValue() {
        return String.format("%s%s", getCategoryTypeValue(), issue.name());
    }

    @Override
    public Integer getHash() {
        return getHash(benefitCode, issue, secondDoctor, fqpmRequired);
    }

    private static Integer getHash(BenefitCode benefitCode, Issue issue, boolean secondDoctor, boolean fqpmRequired) {
        return Objects.hash(benefitCode, issue, secondDoctor, fqpmRequired);
    }
}
