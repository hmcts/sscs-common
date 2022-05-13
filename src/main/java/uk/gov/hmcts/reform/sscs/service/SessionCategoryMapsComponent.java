package uk.gov.hmcts.reform.sscs.service;

import static uk.gov.hmcts.reform.sscs.helper.HashMapHelper.generateHashMap;
import static uk.gov.hmcts.reform.sscs.helper.ReferenceDataHelper.getReferenceData;
import static uk.gov.hmcts.reform.sscs.model.SessionCategoryMap.getHash;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;
import uk.gov.hmcts.reform.sscs.model.SessionCategoryMap;

@Getter
@Setter
@Component
public class SessionCategoryMapsComponent {
    private static final String JSON_DATA_LOCATION = "reference-data/session-category-map.json";

    private List<SessionCategoryMap> sessionCategoryMaps;
    private Map<Integer, SessionCategoryMap> hashMap;

    public SessionCategoryMapsComponent() {
        sessionCategoryMaps = getReferenceData(JSON_DATA_LOCATION, new TypeReference<>() {});
        hashMap = generateHashMap(sessionCategoryMaps);
    }

    public SessionCategoryMap getSessionCategory(String benefitCode, String issueCode, boolean secondDoctor, boolean fqpmRequired) {
        return getSessionCategory(BenefitCode.getBenefitCode(benefitCode), Issue.getIssue(issueCode), secondDoctor, fqpmRequired);
    }

    public SessionCategoryMap getSessionCategory(BenefitCode benefitCode, Issue issue, boolean secondDoctor, boolean fqpmRequired) {
        return hashMap.get(getHash(benefitCode, issue, secondDoctor, fqpmRequired));
    }
}
