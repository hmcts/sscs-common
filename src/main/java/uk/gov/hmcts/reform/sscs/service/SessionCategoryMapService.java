package uk.gov.hmcts.reform.sscs.service;

import static uk.gov.hmcts.reform.sscs.helper.ReferenceDataHelper.getReferenceData;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;
import uk.gov.hmcts.reform.sscs.model.SessionCategoryMap;

@Getter
@Setter
@Component
public class SessionCategoryMapService {
    private static final String JSON_DATA_LOCATION = "reference-data/session-category-map.json";

    private static final String SERVICE_CODE = "BBA3";

    private List<SessionCategoryMap> sessionCategoryMaps;
    private Map<Integer, SessionCategoryMap> hashMap;

    public SessionCategoryMapService() {
        sessionCategoryMaps = getReferenceData(JSON_DATA_LOCATION, new TypeReference<>() {});
        hashMap = generateHashMap(sessionCategoryMaps);
    }

    private Map<Integer, SessionCategoryMap> generateHashMap(List<SessionCategoryMap> sessionCategoryMaps) {
        return sessionCategoryMaps.stream().collect(Collectors.toMap(this::getHash, reference -> reference, (a, b) -> b));
    }

    public SessionCategoryMap getSessionCategory(String benefitCode, String issueCode, boolean secondDoctor, boolean fqpmRequired) {
        return getSessionCategory(BenefitCode.getBenefitCode(benefitCode), Issue.getIssue(issueCode), secondDoctor, fqpmRequired);
    }

    public SessionCategoryMap getSessionCategory(BenefitCode benefitCode, Issue issue, boolean secondDoctor, boolean fqpmRequired) {
        return hashMap.get(getHash(benefitCode, issue, secondDoctor, fqpmRequired));
    }

    public Integer getHash(SessionCategoryMap sessionCategoryMap) {
        return getHash(sessionCategoryMap.getBenefitCode(), sessionCategoryMap.getIssue(), sessionCategoryMap.isSecondDoctor(), sessionCategoryMap.isFqpmRequired());
    }

    public Integer getHash(BenefitCode benefitCode, Issue issue, boolean secondDoctor, boolean fqpmRequired) {
        return Objects.hash(benefitCode, issue, secondDoctor, fqpmRequired);
    }

    public String getCategoryTypeValue(SessionCategoryMap sessionCategoryMap) {
        return String.format("%s-%03d", SERVICE_CODE, sessionCategoryMap.getBenefitCode().getCcdReference());
    }

    public String getCategorySubTypeValue(SessionCategoryMap sessionCategoryMap) {
        return String.format("%s%s", getCategoryTypeValue(sessionCategoryMap), sessionCategoryMap.getIssue().name());
    }
}
