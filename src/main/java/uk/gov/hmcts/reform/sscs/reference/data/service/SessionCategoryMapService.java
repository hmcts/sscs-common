package uk.gov.hmcts.reform.sscs.reference.data.service;

import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.generateHashMap;
import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.getReferenceData;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;
import uk.gov.hmcts.reform.sscs.reference.data.model.SessionCategoryMap;

@Getter
@Setter
@Component
public class SessionCategoryMapService {
    private static final String JSON_DATA_LOCATION = "reference-data/session-category-map.json";

    private static final String SERVICE_CODE = "BBA3";
    public static final String CATEGORY_TYPE_TEMPLATE = "%s-%03d";
    public static final String CATEGORY_SUBTYPE_TEMPLATE = "%s%s";

    private List<SessionCategoryMap> sessionCategoryMaps;
    private Map<SessionCategoryMap, SessionCategoryMap> sessionCategoryHashMap;

    public SessionCategoryMapService() {
        sessionCategoryMaps = getReferenceData(JSON_DATA_LOCATION, new TypeReference<>() {});
        sessionCategoryHashMap = generateHashMap(sessionCategoryMaps);
    }


    public SessionCategoryMap getSessionCategory(String benefitCode, String issueCode,
                                                 boolean secondDoctor, boolean fqpmRequired) {
        return getSessionCategory(BenefitCode.getBenefitCode(benefitCode), Issue.getIssue(issueCode),
                secondDoctor, fqpmRequired);
    }

    public SessionCategoryMap getSessionCategory(BenefitCode benefitCode, Issue issue,
                                                 boolean secondDoctor, boolean fqpmRequired) {
        return sessionCategoryHashMap.get(new SessionCategoryMap(benefitCode, issue, secondDoctor, !benefitCode.equals(BenefitCode.IBC) && fqpmRequired));
    }

    public String getCategoryTypeValue(SessionCategoryMap sessionCategoryMap) {
        return String.format(CATEGORY_TYPE_TEMPLATE, SERVICE_CODE,
                sessionCategoryMap.getBenefitCode().getCcdReference());
    }

    public String getCategorySubTypeValue(SessionCategoryMap sessionCategoryMap) {
        return String.format(CATEGORY_SUBTYPE_TEMPLATE,
                getCategoryTypeValue(sessionCategoryMap), sessionCategoryMap.getIssue().name());
    }
}
